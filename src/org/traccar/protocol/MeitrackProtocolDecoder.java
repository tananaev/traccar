/*
 * Copyright 2012 - 2018 Anton Tananaev (anton@traccar.org)
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
import org.traccar.Context;
import org.traccar.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.helper.Checksum;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class MeitrackProtocolDecoder extends BaseProtocolDecoder {

    private ByteBuf photo;

    public MeitrackProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("$$").expression(".")          // flag
            .number("d+,")                       // length
            .number("(d+),")                     // imei
            .number("xxx,")                      // command
            .number("d+,").optional()
            .number("(d+),")                     // event
            .number("(-?d+.d+),")                // latitude
            .number("(-?d+.d+),")                // longitude
            .number("(dd)(dd)(dd)")              // date (yymmdd)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("([AV]),")                   // validity
            .number("(d+),")                     // satellites
            .number("(d+),")                     // rssi
            .number("(d+.?d*),")                 // speed
            .number("(d+),")                     // course
            .number("(d+.?d*),")                 // hdop
            .number("(-?d+),")                   // altitude
            .number("(d+),")                     // odometer
            .number("(d+),")                     // runtime
            .number("(d+)|")                     // mcc
            .number("(d+)|")                     // mnc
            .number("(x+)|")                     // lac
            .number("(x+),")                     // cid
            .number("(x+),")                     // state
            .number("(x+)?|")                    // adc1
            .number("(x+)?|")                    // adc2
            .number("(x+)?|")                    // adc3
            .number("(x+)|")                     // battery
            .number("(x+)?,")                    // power
            .groupBegin()
            .expression("([^,]+)?,").optional()  // event specific
            .expression("[^,]*,")                // reserved
            .number("(d+)?,")                    // protocol
            .number("(x{4})?")                   // fuel
            .groupBegin()
            .number(",(x{6}(?:|x{6})*)?")        // temperature
            .groupBegin()
            .number(",(d+)")                     // data count
            .expression(",([^*]*)")              // data
            .groupEnd("?")
            .groupEnd("?")
            .or()
            .any()
            .groupEnd()
            .text("*")
            .number("xx")
            .text("\r\n").optional()
            .compile();

    private String decodeAlarm(int event) {
        switch (event) {
            case 1:
                return Position.ALARM_SOS;
            case 17:
                return Position.ALARM_LOW_BATTERY;
            case 18:
                return Position.ALARM_LOW_POWER;
            case 19:
                return Position.ALARM_OVERSPEED;
            case 20:
                return Position.ALARM_GEOFENCE_ENTER;
            case 21:
                return Position.ALARM_GEOFENCE_EXIT;
            case 22:
                return Position.ALARM_POWER_RESTORED;
            case 23:
                return Position.ALARM_POWER_CUT;
            case 36:
                return Position.ALARM_TOW;
            case 44:
                return Position.ALARM_JAMMING;
            case 78:
                return Position.ALARM_ACCIDENT;
            case 90:
            case 91:
                return Position.ALARM_CORNERING;
            case 129:
                return Position.ALARM_BRAKING;
            case 130:
                return Position.ALARM_ACCELERATION;
            case 135:
                return Position.ALARM_FATIGUE_DRIVING;
            default:
                return null;
        }
    }

    private Position decodeRegular(Channel channel, SocketAddress remoteAddress, ByteBuf buf) {

        Parser parser = new Parser(PATTERN, buf.toString(StandardCharsets.US_ASCII));
        if (!parser.matches()) {
            return null;
        }

        Position position = new Position(getProtocolName());

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }
        position.setDeviceId(deviceSession.getDeviceId());

        int event = parser.nextInt(0);
        position.set(Position.KEY_EVENT, event);
        position.set(Position.KEY_ALARM, decodeAlarm(event));

        switch (event) {
            case 2:
            case 3:
            case 4:
                position.set(Position.KEY_IGNITION, true);
                break;

            case 10:
            case 11:
            case 12:
                position.set(Position.KEY_IGNITION, false);
                break;

            default:
                break;
        }

        position.setLatitude(parser.nextDouble(0));
        position.setLongitude(parser.nextDouble(0));

        position.setTime(parser.nextDateTime());

        position.setValid(parser.next().equals("A"));

        position.set(Position.KEY_SATELLITES, parser.nextInt());
        int rssi = parser.nextInt(0);

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble(0)));
        position.setCourse(parser.nextDouble(0));

        position.set(Position.KEY_HDOP, parser.nextDouble());

        position.setAltitude(parser.nextDouble(0));

        position.set(Position.KEY_ODOMETER, parser.nextInt(0));
        position.set("runtime", parser.next());

        position.setNetwork(new Network(CellTower.from(
                parser.nextInt(0), parser.nextInt(0), parser.nextHexInt(0), parser.nextHexInt(0), rssi)));

        position.set(Position.KEY_STATUS, parser.next());

        for (int i = 1; i <= 3; i++) {
            if (parser.hasNext()) {
                position.set(Position.PREFIX_ADC + i, parser.nextHexInt(0));
            }
        }

        String deviceModel = Context.getIdentityManager().getById(deviceSession.getDeviceId()).getModel();
        if (deviceModel == null) {
            deviceModel = "";
        }
        switch (deviceModel.toUpperCase()) {
            case "MVT340":
            case "MVT380":
                position.set(Position.KEY_BATTERY, parser.nextHexInt(0) * 3.0 * 2.0 / 1024.0);
                position.set(Position.KEY_POWER, parser.nextHexInt(0) * 3.0 * 16.0 / 1024.0);
                break;
            case "MT90":
                position.set(Position.KEY_BATTERY, parser.nextHexInt(0) * 3.3 * 2.0 / 4096.0);
                position.set(Position.KEY_POWER, parser.nextHexInt(0));
                break;
            case "T1":
            case "T3":
            case "MVT100":
            case "MVT600":
            case "MVT800":
            case "TC68":
            case "TC68S":
                position.set(Position.KEY_BATTERY, parser.nextHexInt(0) * 3.3 * 2.0 / 4096.0);
                position.set(Position.KEY_POWER, parser.nextHexInt(0) * 3.3 * 16.0 / 4096.0);
                break;
            case "T311":
            case "T322X":
            case "T333":
            case "T355":
                position.set(Position.KEY_BATTERY, parser.nextHexInt(0) / 100.0);
                position.set(Position.KEY_POWER, parser.nextHexInt(0) / 100.0);
                break;
            default:
                position.set(Position.KEY_BATTERY, parser.nextHexInt(0));
                position.set(Position.KEY_POWER, parser.nextHexInt(0));
                break;
        }

        String eventData = parser.next();
        if (eventData != null && !eventData.isEmpty()) {
            switch (event) {
                case 37:
                    position.set(Position.KEY_DRIVER_UNIQUE_ID, eventData);
                    break;
                default:
                    position.set("eventData", eventData);
                    break;
            }
        }

        int protocol = parser.nextInt(0);

        if (parser.hasNext()) {
            String fuel = parser.next();
            position.set(Position.KEY_FUEL_LEVEL,
                    Integer.parseInt(fuel.substring(0, 2), 16) + Integer.parseInt(fuel.substring(2), 16) * 0.01);
        }

        if (parser.hasNext()) {
            for (String temp : parser.next().split("\\|")) {
                int index = Integer.parseInt(temp.substring(0, 2), 16);
                if (protocol >= 3) {
                    double value = (short) Integer.parseInt(temp.substring(2), 16);
                    position.set(Position.PREFIX_TEMP + index, value * 0.01);
                } else {
                    double value = Byte.parseByte(temp.substring(2, 4), 16);
                    value += (value < 0 ? -0.01 : 0.01) * Integer.parseInt(temp.substring(4), 16);
                    position.set(Position.PREFIX_TEMP + index, value);
                }
            }
        }

        if (parser.hasNext(2)) {
            parser.nextInt(); // count
            decodeDataFields(position, parser.next().split(","));
        }

        return position;
    }

    private void decodeDataFields(Position position, String[] values) {

        if (values.length > 1 && !values[1].isEmpty()) {
            position.set("tempData", values[1]);
        }

        if (values.length > 5 && !values[5].isEmpty()) {
            String[] data = values[5].split("\\|");
            boolean started = data[0].charAt(1) == '0';
            position.set("taximeterOn", started);
            position.set("taximeterStart", data[1]);
            if (data.length > 2) {
                position.set("taximeterEnd", data[2]);
                position.set("taximeterDistance", Integer.parseInt(data[3]));
                position.set("taximeterFare", Integer.parseInt(data[4]));
                position.set("taximeterTrip", data[5]);
                position.set("taximeterWait", data[6]);
            }
        }

    }

    private List<Position> decodeBinaryC(Channel channel, SocketAddress remoteAddress, ByteBuf buf) {
        List<Position> positions = new LinkedList<>();

        String flag = buf.toString(2, 1, StandardCharsets.US_ASCII);
        int index = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) ',');

        String imei = buf.toString(index + 1, 15, StandardCharsets.US_ASCII);
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        buf.skipBytes(index + 1 + 15 + 1 + 3 + 1 + 2 + 2 + 4);

        while (buf.readableBytes() >= 0x34) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            position.set(Position.KEY_EVENT, buf.readUnsignedByte());

            position.setLatitude(buf.readIntLE() * 0.000001);
            position.setLongitude(buf.readIntLE() * 0.000001);

            position.setTime(new Date((946684800 + buf.readUnsignedIntLE()) * 1000)); // 946684800 = 2000-01-01

            position.setValid(buf.readUnsignedByte() == 1);

            position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());
            int rssi = buf.readUnsignedByte();

            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShortLE()));
            position.setCourse(buf.readUnsignedShortLE());

            position.set(Position.KEY_HDOP, buf.readUnsignedShortLE() * 0.1);

            position.setAltitude(buf.readUnsignedShortLE());

            position.set(Position.KEY_ODOMETER, buf.readUnsignedIntLE());
            position.set("runtime", buf.readUnsignedIntLE());

            position.setNetwork(new Network(CellTower.from(
                    buf.readUnsignedShortLE(), buf.readUnsignedShortLE(),
                    buf.readUnsignedShortLE(), buf.readUnsignedShortLE(),
                    rssi)));

            position.set(Position.KEY_STATUS, buf.readUnsignedShortLE());

            position.set(Position.PREFIX_ADC + 1, buf.readUnsignedShortLE());
            position.set(Position.KEY_BATTERY, buf.readUnsignedShortLE() * 0.01);
            position.set(Position.KEY_POWER, buf.readUnsignedShortLE());

            buf.readUnsignedIntLE(); // geo-fence

            positions.add(position);
        }

        if (channel != null) {
            StringBuilder command = new StringBuilder("@@");
            command.append(flag).append(27 + positions.size() / 10).append(",");
            command.append(imei).append(",CCC,").append(positions.size()).append("*");
            int checksum = 0;
            for (int i = 0; i < command.length(); i += 1) {
                checksum += command.charAt(i);
            }
            command.append(String.format("%02x", checksum & 0xff).toUpperCase());
            command.append("\r\n");
            channel.writeAndFlush(new NetworkMessage(command.toString(), remoteAddress)); // delete processed data
        }

        return positions;
    }

    private List<Position> decodeBinaryE(Channel channel, SocketAddress remoteAddress, ByteBuf buf) {
        List<Position> positions = new LinkedList<>();

        buf.readerIndex(buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) ',') + 1);
        String imei = buf.readSlice(15).toString(StandardCharsets.US_ASCII);
        buf.skipBytes(1 + 3 + 1);

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        buf.readUnsignedIntLE(); // remaining cache
        int count = buf.readUnsignedShortLE();

        for (int i = 0; i < count; i++) {
            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            buf.readUnsignedShortLE(); // length
            buf.readUnsignedShortLE(); // index

            int paramCount = buf.readUnsignedByte();
            for (int j = 0; j < paramCount; j++) {
                int id = buf.readUnsignedByte();
                switch (id) {
                    case 0x01:
                        position.set(Position.KEY_EVENT, buf.readUnsignedByte());
                        break;
                    case 0x05:
                        position.setValid(buf.readUnsignedByte() > 0);
                        break;
                    case 0x06:
                        position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());
                        break;
                    case 0x07:
                        position.set(Position.KEY_RSSI, buf.readUnsignedByte());
                        break;
                    default:
                        buf.readUnsignedByte();
                        break;
                }
            }

            paramCount = buf.readUnsignedByte();
            for (int j = 0; j < paramCount; j++) {
                int id = buf.readUnsignedByte();
                switch (id) {
                    case 0x08:
                        position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShortLE()));
                        break;
                    case 0x09:
                        position.setCourse(buf.readUnsignedShortLE());
                        break;
                    case 0x0B:
                        position.setAltitude(buf.readShortLE());
                        break;
                    case 0x19:
                        position.set(Position.KEY_BATTERY, buf.readUnsignedShortLE() * 0.01);
                        break;
                    case 0x1A:
                        position.set(Position.KEY_POWER, buf.readUnsignedShortLE() * 0.01);
                        break;
                    default:
                        buf.readUnsignedShortLE();
                        break;
                }
            }

            paramCount = buf.readUnsignedByte();
            for (int j = 0; j < paramCount; j++) {
                int id = buf.readUnsignedByte();
                switch (id) {
                    case 0x02:
                        position.setLatitude(buf.readIntLE() * 0.000001);
                        break;
                    case 0x03:
                        position.setLongitude(buf.readIntLE() * 0.000001);
                        break;
                    case 0x04:
                        position.setTime(new Date((946684800 + buf.readUnsignedIntLE()) * 1000)); // 2000-01-01
                        break;
                    case 0x0D:
                        position.set("runtime", buf.readUnsignedIntLE());
                        break;
                    default:
                        buf.readUnsignedIntLE();
                        break;
                }
            }

            paramCount = buf.readUnsignedByte();
            for (int j = 0; j < paramCount; j++) {
                buf.readUnsignedByte(); // id
                buf.skipBytes(buf.readUnsignedByte()); // value
            }

            positions.add(position);
        }

        return positions;
    }

    private void requestPhotoPacket(Channel channel, SocketAddress socketAddress, String imei, String file, int index) {
        if (channel != null) {
            String content = "D00," + file + "," + index;
            int length = 1 + imei.length() + 1 + content.length() + 5;
            String response = String.format("@@O%02d,%s,%s*", length, imei, content);
            response += Checksum.sum(response) + "\r\n";
            channel.writeAndFlush(new NetworkMessage(response, socketAddress));
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        int index = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) ',');
        String imei = buf.toString(index + 1, 15, StandardCharsets.US_ASCII);
        index = buf.indexOf(index + 1, buf.writerIndex(), (byte) ',');
        String type = buf.toString(index + 1, 3, StandardCharsets.US_ASCII);

        switch (type) {
            case "D00":
                if (photo == null) {
                    photo = Unpooled.buffer();
                }

                index = index + 1 + type.length() + 1;
                int endIndex =  buf.indexOf(index, buf.writerIndex(), (byte) ',');
                String file = buf.toString(index, endIndex - index, StandardCharsets.US_ASCII);
                index = endIndex + 1;
                endIndex =  buf.indexOf(index, buf.writerIndex(), (byte) ',');
                int total = Integer.parseInt(buf.toString(index, endIndex - index, StandardCharsets.US_ASCII));
                index = endIndex + 1;
                endIndex = buf.indexOf(index, buf.writerIndex(), (byte) ',');
                int current = Integer.parseInt(buf.toString(index, endIndex - index, StandardCharsets.US_ASCII));

                buf.readerIndex(endIndex + 1);
                photo.writeBytes(buf.readSlice(buf.readableBytes() - 1 - 2 - 2));

                if (current == total - 1) {
                    Position position = new Position(getProtocolName());
                    position.setDeviceId(getDeviceSession(channel, remoteAddress, imei).getDeviceId());

                    getLastLocation(position, null);

                    position.set(Position.KEY_IMAGE, Context.getMediaManager().writeFile(imei, photo, "jpg"));
                    photo.release();
                    photo = null;

                    return position;
                } else {
                    if ((current + 1) % 8 == 0) {
                        requestPhotoPacket(channel, remoteAddress, imei, file, current + 1);
                    }
                    return null;
                }
            case "D03":
                photo = Unpooled.buffer();
                requestPhotoPacket(channel, remoteAddress, imei, "camera_picture.jpg", 0);
                return null;
            case "CCC":
                return decodeBinaryC(channel, remoteAddress, buf);
            case "CCE":
                return decodeBinaryE(channel, remoteAddress, buf);
            default:
                return decodeRegular(channel, remoteAddress, buf);
        }
    }

}
