/*
 * Copyright 2017 - 2021 Anton Tananaev (anton@traccar.org)
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
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DmtProtocolDecoder extends BaseProtocolDecoder {

    public DmtProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    public static final int MSG_HELLO = 0x00;
    public static final int MSG_HELLO_RESPONSE = 0x01;
    public static final int MSG_DATA_RECORD = 0x04;
    public static final int MSG_COMMIT = 0x05;
    public static final int MSG_COMMIT_RESPONSE = 0x06;
    public static final int MSG_DATA_RECORD_64 = 0x10;

    public static final int MSG_CANNED_REQUEST_1 = 0x14;
    public static final int MSG_CANNED_RESPONSE_1 = 0x15;
    public static final int MSG_CANNED_REQUEST_2 = 0x22;
    public static final int MSG_CANNED_RESPONSE_2 = 0x23;

    private void sendResponse(Channel channel, int type, ByteBuf content) {
        if (channel != null) {
            ByteBuf response = Unpooled.buffer();
            response.writeByte(0x02); response.writeByte(0x55); // header
            response.writeByte(type);
            response.writeShortLE(content != null ? content.readableBytes() : 0);
            if (content != null) {
                response.writeBytes(content);
                content.release();
            }
            channel.writeAndFlush(new NetworkMessage(response, channel.remoteAddress()));
        }
    }

    private List<Position> decodeFixed64(Channel channel, SocketAddress remoteAddress, ByteBuf buf) {

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        List<Position> positions = new LinkedList<>();

        while (buf.readableBytes() >= 64) {
            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            buf.readByte(); // type

            position.set(Position.KEY_INDEX, buf.readUnsignedIntLE());

            long time = buf.readUnsignedIntLE();
            position.setTime(new DateBuilder()
                    .setYear((int) (2000 + (time & 0x3F)))
                    .setMonth((int) (time >> 6) & 0xF)
                    .setDay((int) (time >> 10) & 0x1F)
                    .setHour((int) (time >> 15) & 0x1F)
                    .setMinute((int) (time >> 20) & 0x3F)
                    .setSecond((int) (time >> 26) & 0x3F)
                    .getDate());

            position.setLongitude(buf.readIntLE() * 0.0000001);
            position.setLatitude(buf.readIntLE() * 0.0000001);
            position.setSpeed(UnitsConverter.knotsFromCps(buf.readUnsignedShortLE()));
            position.setCourse(buf.readUnsignedByte() * 2);
            position.setAltitude(buf.readShortLE());

            buf.readUnsignedShortLE(); // position accuracy
            buf.readUnsignedByte(); // speed accuracy

            position.set(Position.KEY_EVENT, buf.readUnsignedByte());

            position.setValid(BitUtil.check(buf.readByte(), 0));

            position.set(Position.KEY_INPUT, buf.readUnsignedIntLE());
            position.set(Position.KEY_OUTPUT, buf.readUnsignedShortLE());

            for (int i = 1; i <= 5; i++) {
                position.set(Position.PREFIX_ADC + i, buf.readShortLE());
            }

            position.set(Position.KEY_DEVICE_TEMP, buf.readByte());

            buf.readShortLE(); // accelerometer x
            buf.readShortLE(); // accelerometer y
            buf.readShortLE(); // accelerometer z

            buf.skipBytes(8); // device id

            position.set(Position.KEY_PDOP, buf.readUnsignedShortLE() * 0.01);

            buf.skipBytes(2); // reserved

            buf.readUnsignedShortLE(); // checksum

            positions.add(position);
        }

        return positions;
    }

    private List<Position> decodeStandard(Channel channel, SocketAddress remoteAddress, ByteBuf buf) {

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        List<Position> positions = new LinkedList<>();

        while (buf.isReadable()) {
            int recordEnd = buf.readerIndex() + buf.readUnsignedShortLE();

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            position.set(Position.KEY_INDEX, buf.readUnsignedIntLE());

            position.setDeviceTime(new Date(1356998400000L + buf.readUnsignedIntLE() * 1000)); // since 1 Jan 2013

            position.set(Position.KEY_EVENT, buf.readUnsignedByte());

            while (buf.readerIndex() < recordEnd) {

                int fieldId = buf.readUnsignedByte();
                int fieldLength = buf.readUnsignedByte();
                int fieldEnd = buf.readerIndex() + (fieldLength == 255 ? buf.readUnsignedShortLE() : fieldLength);

                if (fieldId == 0) {

                    position.setFixTime(new Date(1356998400000L + buf.readUnsignedIntLE() * 1000));
                    position.setLatitude(buf.readIntLE() * 0.0000001);
                    position.setLongitude(buf.readIntLE() * 0.0000001);
                    position.setAltitude(buf.readShortLE());
                    position.setSpeed(UnitsConverter.knotsFromCps(buf.readUnsignedShortLE()));

                    buf.readUnsignedByte(); // speed accuracy

                    position.setCourse(buf.readUnsignedByte() * 2);

                    position.set(Position.KEY_PDOP, buf.readUnsignedByte() * 0.1);

                    position.setAccuracy(buf.readUnsignedByte());
                    position.setValid(buf.readUnsignedByte() != 0);

                } else if (fieldId == 2) {

                    int input = buf.readIntLE();
                    int output = buf.readUnsignedShortLE();
                    int status = buf.readUnsignedShortLE();

                    position.set(Position.KEY_IGNITION, BitUtil.check(input, 0));

                    if (!BitUtil.check(input, 1)) {
                        position.set(Position.KEY_ALARM, Position.ALARM_LOW_BATTERY);
                    } else if (BitUtil.check(input, 6)) {
                        position.set(Position.KEY_ALARM, Position.ALARM_TAMPERING);
                    }

                    position.set(Position.KEY_INPUT, input);
                    position.set(Position.KEY_OUTPUT, output);
                    position.set(Position.KEY_STATUS, status);

                } else if (fieldId == 6) {

                    while (buf.readerIndex() < fieldEnd) {
                        int number = buf.readUnsignedByte();
                        switch (number) {
                            case 1:
                                position.set(Position.KEY_BATTERY, buf.readUnsignedShortLE() * 0.001);
                                break;
                            case 2:
                                position.set(Position.KEY_POWER, buf.readUnsignedShortLE() * 0.01);
                                break;
                            case 3:
                                position.set(Position.KEY_DEVICE_TEMP, buf.readShortLE() * 0.01);
                                break;
                            case 4:
                                position.set(Position.KEY_RSSI, buf.readUnsignedShortLE());
                                break;
                            case 5:
                                position.set("solarPower", buf.readUnsignedShortLE() * 0.001);
                                break;
                            default:
                                position.set(Position.PREFIX_IO + number, buf.readUnsignedShortLE());
                                break;
                        }
                    }

                } else if (fieldId == 26) {

                    position.set(Position.KEY_ODOMETER_TRIP, buf.readUnsignedIntLE());
                    position.set("tripHours", buf.readUnsignedIntLE() * 1000);

                } else if (fieldId == 27) {

                    position.set(Position.KEY_ODOMETER, buf.readUnsignedIntLE());
                    position.set(Position.KEY_HOURS, buf.readUnsignedIntLE() * 1000);

                }

                buf.readerIndex(fieldEnd);

            }

            if (position.getFixTime() == null) {
                getLastLocation(position, position.getDeviceTime());
            }

            positions.add(position);
        }

        return positions;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        buf.skipBytes(2); // header

        int type = buf.readUnsignedByte();
        int length = buf.readUnsignedShortLE();

        if (type == MSG_HELLO) {

            buf.readUnsignedIntLE(); // device serial number

            DeviceSession deviceSession = getDeviceSession(
                    channel, remoteAddress, buf.readSlice(15).toString(StandardCharsets.US_ASCII));

            ByteBuf response = Unpooled.buffer();
            if (length == 51) {
                response.writeByte(0); // reserved
                response.writeIntLE(0); // reserved
            } else {
                response.writeIntLE((int) ((System.currentTimeMillis() - 1356998400000L) / 1000));
                response.writeIntLE(deviceSession != null ? 0 : 1); // flags
            }

            sendResponse(channel, MSG_HELLO_RESPONSE, response);

        } else if (type == MSG_COMMIT) {

            ByteBuf response = Unpooled.buffer(0);
            response.writeByte(1); // flags (success)
            sendResponse(channel, MSG_COMMIT_RESPONSE, response);

        } else if (type == MSG_CANNED_REQUEST_1) {

            ByteBuf response = Unpooled.buffer(0);
            response.writeBytes(new byte[12]);
            sendResponse(channel, MSG_CANNED_RESPONSE_1, response);

        } else if (type == MSG_CANNED_REQUEST_2) {

            sendResponse(channel, MSG_CANNED_RESPONSE_2, null);

        } else if (type == MSG_DATA_RECORD_64) {

            return decodeFixed64(channel, remoteAddress, buf);

        } else if (type == MSG_DATA_RECORD) {

            return decodeStandard(channel, remoteAddress, buf);

        }

        return null;
    }

}
