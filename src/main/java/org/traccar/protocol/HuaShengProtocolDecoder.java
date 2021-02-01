/*
 * Copyright 2016 - 2020 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;
import org.traccar.model.WifiAccessPoint;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class HuaShengProtocolDecoder extends BaseProtocolDecoder {

    public HuaShengProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    public static final int MSG_POSITION = 0xAA00;
    public static final int MSG_POSITION_RSP = 0xFF01;
    public static final int MSG_LOGIN = 0xAA02;
    public static final int MSG_LOGIN_RSP = 0xFF03;
    public static final int MSG_UPFAULT = 0xAA12;
    public static final int MSG_UPFAULT_RSP = 0xFF13;
    public static final int MSG_HSO_REQ = 0x0002;
    public static final int MSG_HSO_RSP = 0x0003;

    private void sendResponse(Channel channel, int type, int index, ByteBuf content) {
        if (channel != null) {
            ByteBuf response = Unpooled.buffer();
            response.writeByte(0xC0);
            response.writeShort(0x0100);
            response.writeShort(12 + (content != null ? content.readableBytes() : 0));
            response.writeShort(type);
            response.writeShort(0);
            response.writeInt(index);
            if (content != null) {
                response.writeBytes(content);
                content.release();
            }
            response.writeByte(0xC0);
            channel.writeAndFlush(new NetworkMessage(response, channel.remoteAddress()));
        }
    }

    private String decodeAlarm(int event) {
        switch (event) {
            case 4:
                return Position.ALARM_FATIGUE_DRIVING;
            case 6:
                return Position.ALARM_SOS;
            case 7:
                return Position.ALARM_BRAKING;
            case 8:
                return Position.ALARM_ACCELERATION;
            case 9:
                return Position.ALARM_CORNERING;
            case 10:
                return Position.ALARM_ACCIDENT;
            case 16:
                return Position.ALARM_REMOVING;
            default:
                return null;
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        buf.skipBytes(1); // start marker
        buf.readUnsignedByte(); // flag
        buf.readUnsignedByte(); // reserved
        buf.readUnsignedShort(); // length

        int type = buf.readUnsignedShort();

        buf.readUnsignedShort(); // checksum
        int index = buf.readInt();

        if (type == MSG_LOGIN) {

            while (buf.readableBytes() > 4) {
                int subtype = buf.readUnsignedShort();
                int length = buf.readUnsignedShort() - 4;
                if (subtype == 0x0003) {
                    String imei = buf.readCharSequence(length, StandardCharsets.US_ASCII).toString();
                    DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
                    if (deviceSession != null && channel != null) {
                        ByteBuf content = Unpooled.buffer();
                        content.writeByte(0); // success
                        sendResponse(channel, MSG_LOGIN_RSP, index, content);
                    }
                } else {
                    buf.skipBytes(length);
                }
            }

        } else if (type == MSG_HSO_REQ) {

            sendResponse(channel, MSG_HSO_RSP, index, null);

        } else if (type == MSG_UPFAULT) {

            return decodeFaultCodes(channel, remoteAddress, buf);

        } else if (type == MSG_POSITION) {

            return decodePosition(channel, remoteAddress, buf, index);

        }

        return null;
    }

    private Position decodeFaultCodes(
            Channel channel, SocketAddress remoteAddress, ByteBuf buf) {

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        getLastLocation(position, null);

        buf.readUnsignedShort(); // type
        buf.readUnsignedShort(); // length

        StringBuilder codes = new StringBuilder();
        while (buf.readableBytes() > 2) {
            String value = ByteBufUtil.hexDump(buf.readSlice(2));
            int digit = Integer.parseInt(value.substring(0, 1), 16);
            char prefix;
            switch (digit >> 2) {
                default:
                    prefix = 'P';
                    break;
                case 1:
                    prefix = 'C';
                    break;
                case 2:
                    prefix = 'B';
                    break;
                case 3:
                    prefix = 'U';
                    break;
            }
            codes.append(prefix).append(digit % 4).append(value.substring(1));
            if (buf.readableBytes() > 2) {
                codes.append(' ');
            }
        }

        position.set(Position.KEY_DTCS, codes.toString());

        return position;
    }

    private Position decodePosition(
            Channel channel, SocketAddress remoteAddress, ByteBuf buf, int index) {

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        int status = buf.readUnsignedShort();

        position.setValid(BitUtil.check(status, 15));

        position.set(Position.KEY_STATUS, status);
        position.set(Position.KEY_IGNITION, BitUtil.check(status, 14));

        int event = buf.readUnsignedShort();
        position.set(Position.KEY_ALARM, decodeAlarm(event));
        position.set(Position.KEY_EVENT, event);

        String time = buf.readCharSequence(12, StandardCharsets.US_ASCII).toString();

        DateBuilder dateBuilder = new DateBuilder()
                .setYear(Integer.parseInt(time.substring(0, 2)))
                .setMonth(Integer.parseInt(time.substring(2, 4)))
                .setDay(Integer.parseInt(time.substring(4, 6)))
                .setHour(Integer.parseInt(time.substring(6, 8)))
                .setMinute(Integer.parseInt(time.substring(8, 10)))
                .setSecond(Integer.parseInt(time.substring(10, 12)));
        position.setTime(dateBuilder.getDate());

        position.setLongitude(buf.readInt() * 0.00001);
        position.setLatitude(buf.readInt() * 0.00001);

        position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShort()));
        position.setCourse(buf.readUnsignedShort());
        position.setAltitude(buf.readUnsignedShort());

        position.set(Position.KEY_ODOMETER, buf.readUnsignedShort() * 1000);

        Network network = new Network();

        while (buf.readableBytes() > 4) {
            int subtype = buf.readUnsignedShort();
            int length = buf.readUnsignedShort() - 4;
            switch (subtype) {
                case 0x0001:
                    position.set(Position.KEY_COOLANT_TEMP, buf.readUnsignedByte() - 40);
                    position.set(Position.KEY_RPM, buf.readUnsignedShort());
                    position.set("averageSpeed", buf.readUnsignedByte());
                    buf.readUnsignedShort(); // interval fuel consumption
                    position.set(Position.KEY_FUEL_CONSUMPTION, buf.readUnsignedShort() * 0.01);
                    position.set(Position.KEY_ODOMETER_TRIP, buf.readUnsignedShort());
                    position.set(Position.KEY_POWER, buf.readUnsignedShort() * 0.01);
                    position.set(Position.KEY_FUEL_LEVEL, buf.readUnsignedByte() * 0.4);
                    buf.readUnsignedInt(); // trip id
                    break;
                case 0x0005:
                    position.set(Position.KEY_RSSI, buf.readUnsignedByte());
                    position.set(Position.KEY_HDOP, buf.readUnsignedByte());
                    buf.readUnsignedInt(); // run time
                    break;
                case 0x0009:
                    position.set(
                            Position.KEY_VIN, buf.readCharSequence(length, StandardCharsets.US_ASCII).toString());
                    break;
                case 0x0011:
                    position.set(Position.KEY_HOURS, buf.readUnsignedInt() * 0.05);
                    break;
                case 0x0020:
                    String[] cells = buf.readCharSequence(
                            length, StandardCharsets.US_ASCII).toString().split("\\+");
                    for (String cell : cells) {
                        String[] values = cell.split("@");
                        network.addCellTower(CellTower.from(
                                Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                                Integer.parseInt(values[2], 16), Integer.parseInt(values[3], 16)));
                    }
                    break;
                case 0x0021:
                    String[] points = buf.readCharSequence(
                            length, StandardCharsets.US_ASCII).toString().split("\\+");
                    for (String point : points) {
                        String[] values = point.split("@");
                        network.addWifiAccessPoint(WifiAccessPoint.from(values[0], Integer.parseInt(values[1])));
                    }
                    break;
                default:
                    buf.skipBytes(length);
                    break;
            }
        }

        if (network.getCellTowers() != null || network.getWifiAccessPoints() != null) {
            position.setNetwork(network);
        }

        sendResponse(channel, MSG_POSITION_RSP, index, null);

        return position;
    }

}
