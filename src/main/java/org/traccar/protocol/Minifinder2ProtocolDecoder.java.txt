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
import org.traccar.helper.BitUtil;
import org.traccar.helper.Checksum;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;
import org.traccar.model.WifiAccessPoint;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Minifinder2ProtocolDecoder extends BaseProtocolDecoder {

    public Minifinder2ProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    public static final int MSG_DATA = 0x01;
    public static final int MSG_RESPONSE = 0x7F;

    private String decodeAlarm(int code) {
        if (BitUtil.check(code, 0)) {
            return Position.ALARM_LOW_BATTERY;
        }
        if (BitUtil.check(code, 1)) {
            return Position.ALARM_OVERSPEED;
        }
        if (BitUtil.check(code, 2)) {
            return Position.ALARM_FALL_DOWN;
        }
        if (BitUtil.check(code, 8)) {
            return Position.ALARM_POWER_OFF;
        }
        if (BitUtil.check(code, 9)) {
            return Position.ALARM_POWER_ON;
        }
        if (BitUtil.check(code, 12)) {
            return Position.ALARM_SOS;
        }
        return null;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        buf.readUnsignedByte(); // header
        int flags = buf.readUnsignedByte();
        buf.readUnsignedShortLE(); // length
        buf.readUnsignedShortLE(); // checksum
        int index = buf.readUnsignedShortLE();
        int type = buf.readUnsignedByte();

        if (BitUtil.check(flags, 4) && channel != null) {

            ByteBuf content = Unpooled.buffer();
            content.writeByte(MSG_RESPONSE);
            content.writeByte(1); // key length
            content.writeByte(0); // success

            ByteBuf response = Unpooled.buffer();
            response.writeByte(0xAB); // header
            response.writeByte(0x00); // properties
            response.writeShortLE(content.readableBytes());
            response.writeShortLE(Checksum.crc16(Checksum.CRC16_XMODEM, content.nioBuffer()));
            response.writeShortLE(index);
            response.writeBytes(content);
            content.release();

            channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
        }

        if (type == MSG_DATA) {

            List<Position> positions = new LinkedList<>();
            Set<Integer> keys = new HashSet<>();
            boolean hasLocation = false;
            Position position = new Position(getProtocolName());

            DeviceSession deviceSession = null;

            while (buf.isReadable()) {
                int endIndex = buf.readUnsignedByte() + buf.readerIndex();
                int key = buf.readUnsignedByte();

                if (keys.contains(key)) {
                    if (!hasLocation) {
                        getLastLocation(position, null);
                    }
                    positions.add(position);
                    keys.clear();
                    hasLocation = false;
                    position = new Position(getProtocolName());
                }
                keys.add(key);

                switch (key) {
                    case 0x01:
                        deviceSession = getDeviceSession(
                                channel, remoteAddress, buf.readCharSequence(15, StandardCharsets.US_ASCII).toString());

                        position.setDeviceId(deviceSession.getDeviceId());
                        break;
                    case 0x02:
                        position.set(Position.KEY_ALARM, decodeAlarm(buf.readIntLE()));
                        break;
                    case 0x14:
                        position.set(Position.KEY_BATTERY_LEVEL, buf.readUnsignedByte());
                        position.set(Position.KEY_BATTERY, buf.readUnsignedShortLE() * 0.001);
                        break;
                    case 0x20:
                        hasLocation = true;
                        position.setLatitude(buf.readIntLE() * 0.0000001);
                        position.setLongitude(buf.readIntLE() * 0.0000001);
                        position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShortLE()));
                        position.setCourse(buf.readUnsignedShortLE());
                        position.setAltitude(buf.readShortLE());
                        position.setValid(buf.readUnsignedShortLE() > 0);
                        position.set(Position.KEY_ODOMETER, buf.readUnsignedIntLE());
                        position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());
                        break;
                    case 0x21:
                        int mcc = buf.readUnsignedShortLE();
                        int mnc = buf.readUnsignedByte();
                        if (position.getNetwork() == null) {
                            position.setNetwork(new Network());
                        }
                        while (buf.readerIndex() < endIndex) {
                            int rssi = buf.readByte();
                            position.getNetwork().addCellTower(CellTower.from(
                                    mcc, mnc, buf.readUnsignedShortLE(), buf.readUnsignedShortLE(), rssi));
                        }
                        break;
                    case 0x22:
                        if (position.getNetwork() == null) {
                            position.setNetwork(new Network());
                        }
                        while (buf.readerIndex() < endIndex) {
                            int rssi = buf.readByte();
                            String mac = ByteBufUtil.hexDump(buf.readSlice(6)).replaceAll("(..)", "$1:");
                            position.getNetwork().addWifiAccessPoint(WifiAccessPoint.from(
                                    mac.substring(0, mac.length() - 1), rssi));
                        }
                        break;
                    case 0x23:
                        position.set("tagId", ByteBufUtil.hexDump(buf.readSlice(6)));
                        position.setLatitude(buf.readIntLE() * 0.0000001);
                        position.setLongitude(buf.readIntLE() * 0.0000001);
                        hasLocation = true;
                        break;
                    case 0x24:
                        position.setTime(new Date(buf.readUnsignedIntLE() * 1000));
                        long status = buf.readUnsignedIntLE();
                        position.set(Position.KEY_BATTERY_LEVEL, BitUtil.from(status, 24));
                        position.set(Position.KEY_STATUS, status);
                        break;
                    case 0x28:
                        int beaconFlags = buf.readUnsignedByte();
                        position.set("tagId", ByteBufUtil.hexDump(buf.readSlice(6)));
                        position.set("tagRssi", buf.readUnsignedByte());
                        buf.readUnsignedByte(); // 1m rssi
                        if (BitUtil.check(beaconFlags, 7)) {
                            position.setLatitude(buf.readIntLE() * 0.0000001);
                            position.setLongitude(buf.readIntLE() * 0.0000001);
                            hasLocation = true;
                        }
                        if (BitUtil.check(beaconFlags, 6)) {
                            position.set("description", buf.readCharSequence(
                                    endIndex - buf.readerIndex(), StandardCharsets.US_ASCII).toString());
                        }
                        break;
                    case 0x30:
                        buf.readUnsignedInt(); // timestamp
                        position.set(Position.KEY_STEPS, buf.readUnsignedInt());
                        break;
                    case 0x40:
                        buf.readUnsignedIntLE(); // timestamp
                        int heartRate = buf.readUnsignedByte();
                        if (heartRate > 1) {
                            position.set(Position.KEY_HEART_RATE, heartRate);
                        }
                        break;
                    default:
                        break;
                }
                buf.readerIndex(endIndex);
            }

            if (!hasLocation) {
                getLastLocation(position, null);
            }
            positions.add(position);

            if (deviceSession != null) {
                for (Position p : positions) {
                    p.setDeviceId(deviceSession.getDeviceId());
                }
            } else {
                return null;
            }

            return positions;

        }

        return null;
    }

}
