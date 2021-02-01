/*
 * Copyright 2017 - 2020 Anton Tananaev (anton@traccar.org)
 * Portions of OBD2 Copyright 2020 Adam Romberg (adam@powertogrow.com)
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

/* README REGARDING OBD2 PIDS:
    The Xirgo documentation allows for certain OBD2 variables to be read if you pay for more data.
    Any variables that may be effected by this will reference this note.
*/

package org.traccar.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.Context;
import org.traccar.DeviceSession;
import org.traccar.Protocol;
import org.traccar.config.Keys;
import org.traccar.helper.DataConverter;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Xt2400ProtocolDecoder extends BaseProtocolDecoder {

    public Xt2400ProtocolDecoder(Protocol protocol) {
        super(protocol);

        String config = Context.getConfig().getString(Keys.PROTOCOL_CONFIG.withPrefix(getProtocolName()));
        if (config != null) {
            setConfig(config);
        }
    }

    private static final Map<Integer, Integer> TAG_LENGTH_MAP = new HashMap<>();

    static {
        int[] l1 = {
                0x01, 0x04, 0x0b, 0x0c, 0x0d, 0x12, 0x13, 0x16,
                0x17, 0x1c, 0x1f, 0x23, 0x2c, 0x2d, 0x30, 0x31,
                0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
                0x3a, 0x3b, 0x3c, 0x3d, 0x3e, 0x3f, 0x40, 0x41,
                0x53, 0x66, 0x69, 0x6a, 0x6d, 0x93, 0x94, 0x96,
                0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa,
                0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1, 0xb2,
                0xc7
        };
        int[] l2 = {
                0x05, 0x09, 0x0a, 0x14, 0x15, 0x1d, 0x1e, 0x24,
                0x26, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48,
                0x49, 0x57, 0x58, 0x59, 0x5a, 0x6b, 0x6f, 0x79,
                0x7A, 0x7B, 0x7C, 0x7E, 0x7F, 0x80, 0x81, 0x82,
                0x83, 0x84, 0x85, 0x86, 0xb3, 0xb4, 0xb5, 0xb6,
                0xb7, 0xb8, 0xb9, 0xba, 0xc3, 0xc8, 0xc9, 0xca,
                0x7d
        };
        int[] l4 = {
                0x03, 0x06, 0x07, 0x08, 0x0e, 0x0f, 0x10, 0x11,
                0x18, 0x19, 0x1a, 0x1b, 0x20, 0x21, 0x22, 0x2e,
                0x2f, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50,
                0x51, 0x52, 0x54, 0x55, 0x56, 0x5b, 0x5c, 0x5d,
                0x5e, 0x5f, 0x60, 0x61, 0x62, 0x68, 0x6e, 0x71,
                0x72, 0x74, 0x75, 0x76, 0x77, 0x78, 0x87,
                0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f,
                0x90, 0x91, 0x92, 0x9c, 0x9d, 0x9e, 0x9f, 0xa0,
                0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2,
                0xc4, 0xcc, 0xcd, 0xce
        };

        for (int i : l1) {
            TAG_LENGTH_MAP.put(i, 1);
        }
        for (int i : l2) {
            TAG_LENGTH_MAP.put(i, 2);
        }
        for (int i : l4) {
            TAG_LENGTH_MAP.put(i, 4);
        }

        TAG_LENGTH_MAP.put(0X73, 17);
        TAG_LENGTH_MAP.put(0x95, 24);
        TAG_LENGTH_MAP.put(0xD0, 21);
        TAG_LENGTH_MAP.put(0XD1, 20);
        TAG_LENGTH_MAP.put(0x65, 17);


    }

    private static int getTagLength(int tag) {
        Integer length = TAG_LENGTH_MAP.get(tag);
        if (length == null) {
            throw new IllegalArgumentException(String.format("Unknown tag: 0x%02X", tag));
        }
        return length;
    }

    private final Map<Short, byte[]> formats = new HashMap<>();

    public void setConfig(String configString) {
        Pattern pattern = Pattern.compile(":wycfg pcr\\[\\d+] ([0-9a-fA-F]{2})[0-9a-fA-F]{2}([0-9a-fA-F]+)");
        Matcher matcher = pattern.matcher(configString);
        while (matcher.find()) {
            formats.put(Short.parseShort(matcher.group(1), 16), DataConverter.parseHex(matcher.group(2)));
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        byte[] format = null;
        if (formats.size() > 1) {
            format = formats.get(buf.getUnsignedByte(buf.readerIndex()));
        } else if (!formats.isEmpty()) {
            format = formats.values().iterator().next();
        }

        if (format == null) {
            return null;
        }

        Position position = new Position(getProtocolName());

        for (byte b : format) {
            int tag = b & 0xFF;
            switch (tag) {
                case 0x03:
                    DeviceSession deviceSession = getDeviceSession(
                            channel, remoteAddress, String.valueOf(buf.readUnsignedInt()));
                    if (deviceSession == null) {
                        return null;
                    }
                    position.setDeviceId(deviceSession.getDeviceId());
                    break;
                case 0x04: // Reason Code (Old-school stuff)
                    position.set(Position.KEY_EVENT, buf.readUnsignedByte());
                    break;
                case 0x05: //Pkt Serial Number
                    position.set(Position.KEY_INDEX, buf.readUnsignedShort());
                    break;
                case 0x06: // UnixTime
                    position.setTime(new Date(buf.readUnsignedInt() * 1000));
                    break;
                case 0x07:  // Lat
                    position.setLatitude(buf.readInt() * 0.000001);
                    break;
                case 0x08: // Lon
                    position.setLongitude(buf.readInt() * 0.000001);
                    break;
                case 0x09: //Altitude
                    position.setAltitude(buf.readShort() * 0.1);
                    break;
                case 0x0a: // Heading
                    position.setCourse(buf.readShort() * 0.1);
                    break;
                case 0x0b: // Speed from GPS unit
                    position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedByte()));
                    break;
                case 0x10: // GPS Trip Odometer
                    position.set(Position.KEY_ODOMETER_TRIP, buf.readUnsignedInt());
                    break;
                case 0x12: // HDOP
                    position.set(Position.KEY_HDOP, buf.readUnsignedByte() * 0.1);
                    break;
                case 0x13: // Number of Sattalites
                    position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());
                    break;
                case 0x14: // Cell Signal Strength
                    position.set(Position.KEY_RSSI, buf.readShort());
                    break;
                case 0x16: // Battery voltage of unit
                    position.set(Position.KEY_BATTERY, buf.readUnsignedByte() * 0.1);
                    break;
                case 0x17: // Battery voltage of car
                    position.set(Position.KEY_POWER, buf.readUnsignedByte() * 0.1);
                    break;
                case 0x57: // OBD2 speed.
                    position.set(Position.KEY_OBD_SPEED, UnitsConverter.knotsFromKph(buf.readUnsignedShort())*.1);
                    break;
                case 0x54: // OBD2 readout of ODM
                    position.set(Position.KEY_OBD_ODOMETER, buf.readShort());
                    break;
                case 0x55: // OBD2 readout of Total Fuel Used
                    position.set(Position.KEY_FUEL_USED, buf.readUnsignedShort());
                    break;
                case 0x56:  // OBD2 readout of Total Engine Hours
                    position.set(Position.KEY_HOURS, buf.readUnsignedShort());
                    break;
                case 0x58: // OBD2 RPM
                    position.set(Position.KEY_RPM, buf.readUnsignedShort());
                    break;
                case 0x5a: // Fuel Level Percent (MIGHT BE PROPRIATERY PID (see note on top of file)
                    position.set(Position.KEY_FUEL_LEVEL, buf.readUnsignedShort() * .1);
                    break;
                case 0x65: // VIN of vehicle
                    position.set(Position.KEY_VIN, buf.readSlice(17).toString(StandardCharsets.US_ASCII));
                    break;
                case 0x6C: // OBD2 failure reason
                    buf.readUnsignedByte(); // mil
                    int ecuCount = buf.readUnsignedByte();
                    for (int i = 0; i < ecuCount; i++) {
                        buf.readUnsignedByte(); // ecu id
                        buf.skipBytes(buf.readUnsignedByte() * 6);
                    }
                    break;
                case 0x73: // Firmware version
                    position.set(Position.KEY_VERSION_FW, buf.readSlice(16).toString(StandardCharsets.US_ASCII).trim());
                    break;
                default:
                    buf.skipBytes(getTagLength(tag));
                    break;
            }
        }

        if (position.getLatitude() != 0 && position.getLongitude() != 0) {
            position.setValid(true);
        } else {
            getLastLocation(position, position.getDeviceTime());
        }

        return position;
    }

}
