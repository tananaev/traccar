/*
 * Copyright 2013 - 2018 Anton Tananaev (anton@traccar.org)
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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.Context;
import org.traccar.DeviceSession;
import org.traccar.helper.BitUtil;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TeltonikaProtocolDecoder extends BaseProtocolDecoder {

    private boolean connectionless;
    private boolean extended;

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public TeltonikaProtocolDecoder(TeltonikaProtocol protocol, boolean connectionless) {
        super(protocol);
        this.connectionless = connectionless;
        this.extended = Context.getConfig().getBoolean(getProtocolName() + ".extended");
    }

    private DeviceSession parseIdentification(Channel channel, SocketAddress remoteAddress, ChannelBuffer buf) {

        int length = buf.readUnsignedShort();
        String imei = buf.toString(buf.readerIndex(), length, StandardCharsets.US_ASCII);
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);

        if (channel != null) {
            ChannelBuffer response = ChannelBuffers.directBuffer(1);
            if (deviceSession != null) {
                response.writeByte(1);
            } else {
                response.writeByte(0);
            }
            channel.write(response);
        }
        return deviceSession;
    }

    public static final int CODEC_GH3000 = 0x07;
    public static final int CODEC_FM4X00 = 0x08;
    public static final int CODEC_12 = 0x0C;
    public static final int CODEC_16 = 0x10;

    private void decodeSerial(Position position, ChannelBuffer buf) {

        getLastLocation(position, null);

        position.set(Position.KEY_TYPE, buf.readUnsignedByte());

        position.set(Position.KEY_RESULT, buf.readBytes(buf.readInt()).toString(StandardCharsets.US_ASCII));

    }

    private long readValue(ChannelBuffer buf, int length, boolean signed) {
        switch (length) {
            case 1:
                return signed ? buf.readByte() : buf.readUnsignedByte();
            case 2:
                return signed ? buf.readShort() : buf.readUnsignedShort();
            case 4:
                return signed ? buf.readInt() : buf.readUnsignedInt();
            default:
                return buf.readLong();
        }
    }

    private void decodeOtherParameter(Position position, int id, ChannelBuffer buf, int length) {
        switch (id) {
            case 1:
            case 2:
            case 3:
            case 4:
                position.set("di" + id, readValue(buf, length, false));
                break;
            case 9:
                position.set(Position.PREFIX_ADC + 1, readValue(buf, length, false));
                break;
            case 17:
                position.set("axisX", readValue(buf, length, true));
                break;
            case 18:
                position.set("axisY", readValue(buf, length, true));
                break;
            case 19:
                position.set("axisZ", readValue(buf, length, true));
                break;
            case 21:
                position.set(Position.KEY_RSSI, readValue(buf, length, false));
                break;
            case 66:
                double power = readValue(buf, length, false) * 0.001;
                position.set(Position.KEY_POWER, power);
                if (power < 1.0) {
                    position.set(Position.KEY_EXTERNAL_BATTERY_DISCONNECT, true);
                }
                break;
            case 67:
                double battery = readValue(buf, length, false) * 0.001;
                position.set(Position.KEY_BATTERY, battery);
                if (battery < 2.0) {
                    position.set(Position.KEY_INTERNAL_BATTERY_LOW, true);
                }
                break;
            case 72:
                position.set(Position.PREFIX_TEMP + 1, readValue(buf, length, true) * 0.1);
                break;
            case 73:
                position.set(Position.PREFIX_TEMP + 2, readValue(buf, length, true) * 0.1);
                break;
            case 74:
                position.set(Position.PREFIX_TEMP + 3, readValue(buf, length, true) * 0.1);
                break;
            case 78:
                long driverUniqueId = readValue(buf, length, false);
                if (driverUniqueId != 0) {
                    position.set(Position.KEY_DRIVER_UNIQUE_ID, String.format("%016X", driverUniqueId));
                }
                break;
            case 80:
                position.set("workMode", readValue(buf, length, false));
                break;
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
                String driver = id == 129 || id == 132 ? "" : position.getString("driver1");
                position.set("driver" + (id >= 132 ? 2 : 1),
                        driver + buf.readBytes(length).toString(StandardCharsets.US_ASCII).trim());
                break;
            case 179:
                position.set(Position.PREFIX_OUT + 1, readValue(buf, length, false) == 1);
                break;
            case 180:
                position.set(Position.PREFIX_OUT + 2, readValue(buf, length, false) == 1);
                break;
            case 181:
                position.set(Position.KEY_PDOP, readValue(buf, length, false) * 0.1);
                break;
            case 182:
                position.set(Position.KEY_HDOP, readValue(buf, length, false) * 0.1);
                break;
            case 216:
                position.set(Position.KEY_ODOMETER, readValue(buf, length, false));
            case 236:
                if (readValue(buf, length, false) == 1) {
                    position.set(Position.KEY_ALARM, Position.ALARM_OVERSPEED);
                }
                break;
            case 237:
                position.set(Position.KEY_MOTION, readValue(buf, length, false) == 0);
                break;
            case 238:
                switch ((int) readValue(buf, length, false)) {
                    case 1:
                        position.set(Position.KEY_ALARM, Position.ALARM_ACCELERATION);
                        break;
                    case 2:
                        position.set(Position.KEY_ALARM, Position.ALARM_BRAKING);
                        break;
                    case 3:
                        position.set(Position.KEY_ALARM, Position.ALARM_CORNERING);
                        break;
                    default:
                        break;
                }
                break;
            case 239:
                position.set(Position.KEY_IGNITION, readValue(buf, length, false) == 1);
                break;
            case 240:
                position.set(Position.KEY_MOTION, readValue(buf, length, false) == 1);
                break;
            case 241:
                position.set(Position.KEY_OPERATOR, readValue(buf, length, false));
                break;
            default:
                position.set(Position.PREFIX_IO + id, readValue(buf, length, false));
                break;
        }
    }

    private void decodeGh3000Parameter(Position position, int id, ChannelBuffer buf, int length) {
        switch (id) {
            case 1:
                position.set(Position.KEY_BATTERY_LEVEL, readValue(buf, length, false));
                break;
            case 2:
                position.set("usbConnected", readValue(buf, length, false) == 1);
                break;
            case 5:
                position.set("uptime", readValue(buf, length, false));
                break;
            case 20:
                position.set(Position.KEY_HDOP, readValue(buf, length, false) * 0.1);
                break;
            case 21:
                position.set(Position.KEY_VDOP, readValue(buf, length, false) * 0.1);
                break;
            case 22:
                position.set(Position.KEY_PDOP, readValue(buf, length, false) * 0.1);
                break;
            case 67:
                position.set(Position.KEY_BATTERY, readValue(buf, length, false) * 0.001);
                break;
            case 221:
                position.set("button", readValue(buf, length, false));
                break;
            case 222:
                if (readValue(buf, length, false) == 1) {
                    position.set(Position.KEY_ALARM, Position.ALARM_SOS);
                }
                break;
            case 240:
                position.set(Position.KEY_MOTION, readValue(buf, length, false) == 1);
                break;
            case 244:
                position.set(Position.KEY_ROAMING, readValue(buf, length, false) == 1);
                break;
            default:
                position.set(Position.PREFIX_IO + id, readValue(buf, length, false));
                break;
        }
    }

    private void decodeParameter(Position position, int id, ChannelBuffer buf, int length, int codec) {
        if (codec == CODEC_GH3000) {
            decodeGh3000Parameter(position, id, buf, length);
        } else {
            decodeOtherParameter(position, id, buf, length);
        }
    }

    private void decodeNetwork(Position position) {
        long cid = position.getLong(Position.PREFIX_IO + 205);
        int lac = position.getInteger(Position.PREFIX_IO + 206);
        if (cid != 0 && lac != 0) {
            CellTower cellTower = CellTower.fromLacCid(lac, cid);
            long operator = position.getInteger(Position.KEY_OPERATOR);
            if (operator != 0) {
                cellTower.setOperator(operator);
            }
            position.setNetwork(new Network(cellTower));
        }
    }

    private void decodeLocation(Position position, ChannelBuffer buf, int codec) {

        int globalMask = 0x0f;

        if (codec == CODEC_GH3000) {

            long time = buf.readUnsignedInt() & 0x3fffffff;
            time += 1167609600; // 2007-01-01 00:00:00

            globalMask = buf.readUnsignedByte();
            if (BitUtil.check(globalMask, 0)) {

                position.setTime(new Date(time * 1000));

                int locationMask = buf.readUnsignedByte();

                if (BitUtil.check(locationMask, 0)) {
                    position.setLatitude(buf.readFloat());
                    position.setLongitude(buf.readFloat());
                }

                if (BitUtil.check(locationMask, 1)) {
                    position.setAltitude(buf.readUnsignedShort());
                }

                if (BitUtil.check(locationMask, 2)) {
                    position.setCourse(buf.readUnsignedByte() * 360.0 / 256);
                }

                if (BitUtil.check(locationMask, 3)) {
                    position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedByte()));
                }

                if (BitUtil.check(locationMask, 4)) {
                    int satellites = buf.readUnsignedByte();
                    position.set(Position.KEY_SATELLITES, satellites);
                    position.setValid(satellites >= 3);
                }

                if (BitUtil.check(locationMask, 5)) {
                    CellTower cellTower = CellTower.fromLacCid(buf.readUnsignedShort(), buf.readUnsignedShort());

                    if (BitUtil.check(locationMask, 6)) {
                        cellTower.setSignalStrength((int) buf.readUnsignedByte());
                    }

                    if (BitUtil.check(locationMask, 7)) {
                        cellTower.setOperator(buf.readUnsignedInt());
                    }

                    position.setNetwork(new Network(cellTower));

                } else {
                    if (BitUtil.check(locationMask, 6)) {
                        position.set(Position.KEY_RSSI, buf.readUnsignedByte());
                    }
                    if (BitUtil.check(locationMask, 7)) {
                        position.set(Position.KEY_OPERATOR, buf.readUnsignedInt());
                    }
                }

            } else {

                getLastLocation(position, new Date(time * 1000));

            }

        } else {

            position.setTime(new Date(buf.readLong()));

            position.set("priority", buf.readUnsignedByte());

            position.setLongitude(buf.readInt() / 10000000.0);
            position.setLatitude(buf.readInt() / 10000000.0);
            position.setAltitude(buf.readShort());
            position.setCourse(buf.readUnsignedShort());

            int satellites = buf.readUnsignedByte();
            position.set(Position.KEY_SATELLITES, satellites);

            position.setValid(satellites != 0);

            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShort()));

            position.set(Position.KEY_EVENT, buf.readUnsignedByte());

            buf.readUnsignedByte(); // total IO data records

        }

        // Read 1 byte data
        if (BitUtil.check(globalMask, 1)) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf, 1, codec);
            }
        }

        // Read 2 byte data
        if (BitUtil.check(globalMask, 2)) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf, 2, codec);
            }
        }

        // Read 4 byte data
        if (BitUtil.check(globalMask, 3)) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf, 4, codec);
            }
        }

        // Read 8 byte data
        if (codec == CODEC_FM4X00 || codec == CODEC_16) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeOtherParameter(position, buf.readUnsignedByte(), buf, 8);
            }
        }

        // Read 16 byte data
        if (extended) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                position.set(Position.PREFIX_IO + buf.readUnsignedByte(), ChannelBuffers.hexDump(buf.readBytes(16)));
            }
        }

        decodeNetwork(position);

    }

    private List<Position> parseData(
            Channel channel, SocketAddress remoteAddress, ChannelBuffer buf, int locationPacketId, String... imei) {
        List<Position> positions = new LinkedList<>();

        if (!connectionless) {
            buf.readUnsignedInt(); // data length
        }

        int codec = buf.readUnsignedByte();
        int count = buf.readUnsignedByte();

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);

        if (deviceSession == null) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            Position position = new Position(getProtocolName());

            position.setDeviceId(deviceSession.getDeviceId());

            if (codec == CODEC_12) {
                decodeSerial(position, buf);
            } else {
                decodeLocation(position, buf, codec);
            }

            positions.add(position);
        }

        if (channel != null) {
            if (connectionless) {
                ChannelBuffer response = ChannelBuffers.dynamicBuffer();
                response.writeShort(5);
                response.writeShort(0);
                response.writeByte(0x01);
                response.writeByte(locationPacketId);
                response.writeByte(count);
                channel.write(response, remoteAddress);
            } else {
                ChannelBuffer response = ChannelBuffers.dynamicBuffer();
                response.writeInt(count);
                channel.write(response);
            }
        }

        return positions;
    }

    @Override
    protected Object decode(Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) msg;

        if (connectionless) {
            return decodeUdp(channel, remoteAddress, buf);
        } else {
            return decodeTcp(channel, remoteAddress, buf);
        }
    }

    private Object decodeTcp(Channel channel, SocketAddress remoteAddress, ChannelBuffer buf) throws Exception {

        if (buf.getUnsignedShort(0) > 0) {
            parseIdentification(channel, remoteAddress, buf);
        } else {
            buf.skipBytes(4);
            return parseData(channel, remoteAddress, buf, 0);
        }

        return null;
    }

    private Object decodeUdp(Channel channel, SocketAddress remoteAddress, ChannelBuffer buf) throws Exception {

        buf.readUnsignedShort(); // length
        buf.readUnsignedShort(); // packet id
        buf.readUnsignedByte(); // packet type
        int locationPacketId = buf.readUnsignedByte();
        String imei = buf.readBytes(buf.readUnsignedShort()).toString(StandardCharsets.US_ASCII);

        return parseData(channel, remoteAddress, buf, locationPacketId, imei);

    }

}
