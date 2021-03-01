/*
 * Copyright 2019 - 2021 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.helper.BcdUtil;
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;
import org.traccar.model.WifiAccessPoint;

import java.net.SocketAddress;
import java.util.Calendar;
import java.util.TimeZone;

public class TopinProtocolDecoder extends BaseProtocolDecoder {

    public TopinProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    public static final int MSG_LOGIN = 0x01;
    public static final int MSG_GPS_2 = 0x08;
    public static final int MSG_GPS_OFFLINE_2 = 0x09;
    public static final int MSG_GPS = 0x10;
    public static final int MSG_GPS_OFFLINE = 0x11;
    public static final int MSG_STATUS = 0x13;
    public static final int MSG_WIFI_OFFLINE = 0x17;
    public static final int MSG_TIME_UPDATE = 0x30;
    public static final int MSG_WIFI = 0x69;

    private void sendResponse(Channel channel, int length, int type, ByteBuf content) {
        if (channel != null) {
            ByteBuf response = Unpooled.buffer();
            response.writeShort(0x7878);
            response.writeByte(length);
            response.writeByte(type);
            response.writeBytes(content);
            response.writeByte('\r');
            response.writeByte('\n');
            content.release();
            channel.writeAndFlush(new NetworkMessage(response, channel.remoteAddress()));
        }
    }

    private void updateTime(Channel channel, int type) {
        ByteBuf dateBuffer = Unpooled.buffer();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        dateBuffer.writeShort(calendar.get(Calendar.YEAR));
        dateBuffer.writeByte(calendar.get(Calendar.MONTH) + 1);
        dateBuffer.writeByte(calendar.get(Calendar.DAY_OF_MONTH));
        dateBuffer.writeByte(calendar.get(Calendar.HOUR_OF_DAY));
        dateBuffer.writeByte(calendar.get(Calendar.MINUTE));
        dateBuffer.writeByte(calendar.get(Calendar.SECOND));

        sendResponse(channel, dateBuffer.readableBytes(), type, dateBuffer);
    }

    private double readCoordinate(ByteBuf buf) {
        int degrees = buf.readUnsignedByte();
        boolean negative = (buf.getUnsignedByte(buf.readerIndex()) & 0xf0) > 0;
        int decimal = buf.readUnsignedMedium() & 0x0fffff;
        double result = degrees + decimal * 0.000001;
        return negative ? -result : result;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        buf.skipBytes(2); // header
        int length = buf.readUnsignedByte();

        int type = buf.readUnsignedByte();

        DeviceSession deviceSession;
        if (type == MSG_LOGIN) {
            String imei = ByteBufUtil.hexDump(buf.readSlice(8)).substring(1);
            deviceSession = getDeviceSession(channel, remoteAddress, imei);
            ByteBuf content = Unpooled.buffer();
            content.writeByte(deviceSession != null ? 0x01 : 0x44);
            sendResponse(channel, length, type, content);
            updateTime(channel, MSG_TIME_UPDATE);
            return null;
        } else {
            deviceSession = getDeviceSession(channel, remoteAddress);
            if (deviceSession == null) {
                return null;
            }
        }

        if (type == MSG_GPS_2 || type == MSG_GPS_OFFLINE_2) {

            if (buf.readableBytes() <= 2) {
                return null;
            }

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            DateBuilder dateBuilder = new DateBuilder()
                    .setDate(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte())
                    .setTime(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
            position.setTime(dateBuilder.getDate());

            position.setValid(type == MSG_GPS_2);
            position.setLatitude(readCoordinate(buf));
            position.setLongitude(readCoordinate(buf));

            buf.skipBytes(4 + 4); // second coordinates

            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedByte()));
            position.setCourse(buf.readUnsignedByte() * 2);

            position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());

            return position;

        } else if (type == MSG_GPS || type == MSG_GPS_OFFLINE) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            ByteBuf time = buf.slice(buf.readerIndex(), 6);

            Gt06ProtocolDecoder.decodeGps(position, buf, false, TimeZone.getTimeZone("UTC"));

            if (buf.readableBytes() >= 5) {
                position.setAltitude(buf.readShort());

                int alarms = buf.readUnsignedByte();
                if (BitUtil.check(alarms, 0)) {
                    position.set(Position.KEY_ALARM, Position.ALARM_VIBRATION);
                }
                if (BitUtil.check(alarms, 1)) {
                    position.set(Position.KEY_ALARM, Position.ALARM_OVERSPEED);
                }
                if (BitUtil.check(alarms, 4)) {
                    position.set(Position.KEY_ALARM, Position.ALARM_LOW_POWER);
                }
            }

            ByteBuf content = Unpooled.buffer();
            content.writeBytes(time);
            sendResponse(channel, length, type, content);

            return position;

        } else if (type == MSG_TIME_UPDATE) {

            updateTime(channel, type);

            return null;

        } else if (type == MSG_STATUS) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            getLastLocation(position, null);

            position.set(Position.KEY_BATTERY_LEVEL, buf.readUnsignedByte());
            position.set(Position.KEY_VERSION_FW, buf.readUnsignedByte());
            buf.readUnsignedByte(); // timezone
            int interval = buf.readUnsignedByte();
            if (length >= 7) {
                position.set(Position.KEY_RSSI, buf.readUnsignedByte());
            }

            ByteBuf content = Unpooled.buffer();
            content.writeByte(interval);
            sendResponse(channel, length, type, content);

            return position;

        } else if (type == MSG_WIFI || type == MSG_WIFI_OFFLINE) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            ByteBuf time = buf.readSlice(6);
            DateBuilder dateBuilder = new DateBuilder()
                    .setYear(BcdUtil.readInteger(time, 2))
                    .setMonth(BcdUtil.readInteger(time, 2))
                    .setDay(BcdUtil.readInteger(time, 2))
                    .setHour(BcdUtil.readInteger(time, 2))
                    .setMinute(BcdUtil.readInteger(time, 2))
                    .setSecond(BcdUtil.readInteger(time, 2));
            time.resetReaderIndex();

            getLastLocation(position, dateBuilder.getDate());

            Network network = new Network();
            for (int i = 0; i < length; i++) {
                String mac = String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                        buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte(),
                        buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
                network.addWifiAccessPoint(WifiAccessPoint.from(mac, buf.readUnsignedByte()));
            }

            int cellCount = buf.readUnsignedByte();
            int mcc = buf.readUnsignedShort();
            int mnc = buf.readUnsignedByte();
            for (int i = 0; i < cellCount; i++) {
                network.addCellTower(CellTower.from(
                        mcc, mnc, buf.readUnsignedShort(), buf.readUnsignedShort(), buf.readUnsignedByte()));
            }

            position.setNetwork(network);

            ByteBuf content = Unpooled.buffer();
            content.writeBytes(time);
            sendResponse(channel, length, type, content);

            return position;

        }

        return null;
    }

}
