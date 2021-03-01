/*
 * Copyright 2013 - 2021 Anton Tananaev (anton@traccar.org)
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
import org.traccar.Context;
import org.traccar.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.config.Keys;
import org.traccar.helper.BitUtil;
import org.traccar.helper.DataConverter;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtrackProtocolDecoder extends BaseProtocolDecoder {

    private static final int MIN_DATA_LENGTH = 40;

    private boolean longDate;
    private final boolean decimalFuel;
    private boolean custom;
    private String form;

    private ByteBuf photo;

    private final Map<Integer, String> alarmMap = new HashMap<>();

    public AtrackProtocolDecoder(Protocol protocol) {
        super(protocol);

        longDate = Context.getConfig().getBoolean(Keys.PROTOCOL_LONG_DATE.withPrefix(getProtocolName()));
        decimalFuel = Context.getConfig().getBoolean(Keys.PROTOCOL_DECIMAL_FUEL.withPrefix(getProtocolName()));

        custom = Context.getConfig().getBoolean(Keys.PROTOCOL_CUSTOM.withPrefix(getProtocolName()));
        form = Context.getConfig().getString(Keys.PROTOCOL_FORM.withPrefix(getProtocolName()));
        if (form != null) {
            custom = true;
        }

        String alarmMapString = Context.getConfig().getString(Keys.PROTOCOL_ALARM_MAP.withPrefix(getProtocolName()));
        if (alarmMapString != null) {
            for (String pair : alarmMapString.split(",")) {
                if (!pair.isEmpty()) {
                    alarmMap.put(
                            Integer.parseInt(pair.substring(0, pair.indexOf('='))),
                            pair.substring(pair.indexOf('=') + 1));
                }
            }
        }
    }

    public void setLongDate(boolean longDate) {
        this.longDate = longDate;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public void setForm(String form) {
        this.form = form;
    }

    private static void sendResponse(Channel channel, SocketAddress remoteAddress, long rawId, int index) {
        if (channel != null) {
            ByteBuf response = Unpooled.buffer(12);
            response.writeShort(0xfe02);
            response.writeLong(rawId);
            response.writeShort(index);
            channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
        }
    }

    private static String readString(ByteBuf buf) {
        String result = null;
        int index = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) 0);
        if (index > buf.readerIndex()) {
            result = buf.readSlice(index - buf.readerIndex()).toString(StandardCharsets.US_ASCII);
        }
        buf.readByte();
        return result;
    }

    private void decodeBeaconData(Position position, int mode, int mask, ByteBuf data) {
        int i = 1;
        while (data.isReadable()) {
            if (BitUtil.check(mask, 7)) {
                position.set("tag" + i + "Id", ByteBufUtil.hexDump(data.readSlice(6)));
            }
            switch (mode) {
                case 1:
                    if (BitUtil.check(mask, 6)) {
                        data.readUnsignedShort(); // major
                    }
                    if (BitUtil.check(mask, 5)) {
                        data.readUnsignedShort(); // minor
                    }
                    if (BitUtil.check(mask, 4)) {
                        data.readUnsignedByte(); // tx power
                    }
                    if (BitUtil.check(mask, 3)) {
                        position.set("tag" + i + "Rssi", data.readUnsignedByte());
                    }
                    break;
                case 2:
                    if (BitUtil.check(mask, 6)) {
                        data.readUnsignedShort(); // battery voltage
                    }
                    if (BitUtil.check(mask, 5)) {
                        position.set("tag" + i + "Temp", data.readUnsignedShort());
                    }
                    if (BitUtil.check(mask, 4)) {
                        data.readUnsignedByte(); // tx power
                    }
                    if (BitUtil.check(mask, 3)) {
                        position.set("tag" + i + "Rssi", data.readUnsignedByte());
                    }
                    break;
                case 3:
                    if (BitUtil.check(mask, 6)) {
                        position.set("tag" + i + "Humidity", data.readUnsignedShort());
                    }
                    if (BitUtil.check(mask, 5)) {
                        position.set("tag" + i + "Temp", data.readUnsignedShort());
                    }
                    if (BitUtil.check(mask, 3)) {
                        position.set("tag" + i + "Rssi", data.readUnsignedByte());
                    }
                    if (BitUtil.check(mask, 2)) {
                        data.readUnsignedShort();
                    }
                    break;
                case 4:
                    if (BitUtil.check(mask, 6)) {
                        int hardwareId = data.readUnsignedByte();
                        if (BitUtil.check(mask, 5)) {
                            switch (hardwareId) {
                                case 1:
                                case 4:
                                    data.skipBytes(11); // fuel
                                    break;
                                case 2:
                                    data.skipBytes(2); // temperature
                                    break;
                                case 3:
                                    data.skipBytes(6); // temperature and luminosity
                                    break;
                                case 5:
                                    data.skipBytes(10); // temperature, humidity, luminosity and pressure
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (BitUtil.check(mask, 4)) {
                        data.skipBytes(9); // name
                    }
                    break;
                default:
                    break;
            }
            i += 1;
        }
    }

    private void readTextCustomData(Position position, String data, String form) {
        CellTower cellTower = new CellTower();
        String[] keys = form.substring(1).split("%");
        String[] values = data.split(",|\r\n");
        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            switch (keys[i]) {
                case "SA":
                    position.set(Position.KEY_SATELLITES, Integer.parseInt(values[i]));
                    break;
                case "MV":
                    position.set(Position.KEY_POWER, Integer.parseInt(values[i]) * 0.1);
                    break;
                case "BV":
                    position.set(Position.KEY_BATTERY, Integer.parseInt(values[i]) * 0.1);
                    break;
                case "GQ":
                    cellTower.setSignalStrength(Integer.parseInt(values[i]));
                    break;
                case "CE":
                    cellTower.setCellId(Long.parseLong(values[i]));
                    break;
                case "LC":
                    cellTower.setLocationAreaCode(Integer.parseInt(values[i]));
                    break;
                case "CN":
                    if (values[i].length() > 3) {
                        cellTower.setMobileCountryCode(Integer.parseInt(values[i].substring(0, 3)));
                        cellTower.setMobileNetworkCode(Integer.parseInt(values[i].substring(3)));
                    }
                    break;
                case "PC":
                    position.set(Position.PREFIX_COUNT + 1, Integer.parseInt(values[i]));
                    break;
                case "AT":
                    position.setAltitude(Integer.parseInt(values[i]));
                    break;
                case "RP":
                    position.set(Position.KEY_RPM, Integer.parseInt(values[i]));
                    break;
                case "GS":
                    position.set(Position.KEY_RSSI, Integer.parseInt(values[i]));
                    break;
                case "DT":
                    position.set(Position.KEY_ARCHIVE, Integer.parseInt(values[i]) == 1);
                    break;
                case "VN":
                    position.set(Position.KEY_VIN, values[i]);
                    break;
                case "TR":
                    position.set(Position.KEY_THROTTLE, Integer.parseInt(values[i]));
                    break;
                case "ET":
                    position.set(Position.KEY_COOLANT_TEMP, Integer.parseInt(values[i]));
                    break;
                case "FL":
                    position.set(Position.KEY_FUEL_LEVEL, Integer.parseInt(values[i]));
                    break;
                case "FC":
                    position.set(Position.KEY_FUEL_CONSUMPTION, Integer.parseInt(values[i]));
                    break;
                case "AV1":
                    position.set(Position.PREFIX_ADC + 1, Integer.parseInt(values[i]));
                    break;
                case "CD":
                    position.set(Position.KEY_ICCID, values[i]);
                    break;
                case "EH":
                    position.set(Position.KEY_HOURS, UnitsConverter.msFromHours(Integer.parseInt(values[i]) * 0.1));
                    break;
                case "IA":
                    position.set("intakeTemp", Integer.parseInt(values[i]));
                    break;
                case "EL":
                    position.set(Position.KEY_ENGINE_LOAD, Integer.parseInt(values[i]));
                    break;
                case "HA":
                    if (Integer.parseInt(values[i]) > 0) {
                        position.set(Position.KEY_ALARM, Position.ALARM_ACCELERATION);
                    }
                    break;
                case "HB":
                    if (Integer.parseInt(values[i]) > 0) {
                        position.set(Position.KEY_ALARM, Position.ALARM_BRAKING);
                    }
                    break;
                case "HC":
                    if (Integer.parseInt(values[i]) > 0) {
                        position.set(Position.KEY_ALARM, Position.ALARM_CORNERING);
                    }
                    break;
                case "MT":
                    position.set(Position.KEY_MOTION, Integer.parseInt(values[i]) > 0);
                    break;
                case "BC":
                    String[] beaconValues = values[i].split(":");
                    decodeBeaconData(
                            position, Integer.parseInt(beaconValues[0]), Integer.parseInt(beaconValues[1]),
                            Unpooled.wrappedBuffer(DataConverter.parseHex(beaconValues[2])));
                    break;
                default:
                    break;
            }
        }

        if (cellTower.getMobileCountryCode() != null
                && cellTower.getMobileNetworkCode() != null
                && cellTower.getCellId() != null
                && cellTower.getLocationAreaCode() != null) {
            position.setNetwork(new Network(cellTower));
        } else if (cellTower.getSignalStrength() != null) {
            position.set(Position.KEY_RSSI, cellTower.getSignalStrength());
        }
    }

    private void readBinaryCustomData(Position position, ByteBuf buf, String form) {
        CellTower cellTower = new CellTower();
        String[] keys = form.substring(1).split("%");
        for (String key : keys) {
            switch (key) {
                case "SA":
                    position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());
                    break;
                case "MV":
                    position.set(Position.KEY_POWER, buf.readUnsignedShort() * 0.1);
                    break;
                case "BV":
                    position.set(Position.KEY_BATTERY, buf.readUnsignedShort() * 0.1);
                    break;
                case "GQ":
                    cellTower.setSignalStrength((int) buf.readUnsignedByte());
                    break;
                case "CE":
                    cellTower.setCellId(buf.readUnsignedInt());
                    break;
                case "LC":
                    cellTower.setLocationAreaCode(buf.readUnsignedShort());
                    break;
                case "CN":
                    int combinedMobileCodes = (int) (buf.readUnsignedInt() % 100000); // cccnn
                    cellTower.setMobileCountryCode(combinedMobileCodes / 100);
                    cellTower.setMobileNetworkCode(combinedMobileCodes % 100);
                    break;
                case "RL":
                    buf.readUnsignedByte(); // rxlev
                    break;
                case "PC":
                    position.set(Position.PREFIX_COUNT + 1, buf.readUnsignedInt());
                    break;
                case "AT":
                    position.setAltitude(buf.readUnsignedInt());
                    break;
                case "RP":
                    position.set(Position.KEY_RPM, buf.readUnsignedShort());
                    break;
                case "GS":
                    position.set(Position.KEY_RSSI, buf.readUnsignedByte());
                    break;
                case "DT":
                    position.set(Position.KEY_ARCHIVE, buf.readUnsignedByte() == 1);
                    break;
                case "VN":
                    position.set(Position.KEY_VIN, readString(buf));
                    break;
                case "MF":
                    buf.readUnsignedShort(); // mass air flow rate
                    break;
                case "EL":
                    buf.readUnsignedByte(); // engine load
                    break;
                case "TR":
                    position.set(Position.KEY_THROTTLE, buf.readUnsignedByte());
                    break;
                case "ET":
                    position.set(Position.PREFIX_TEMP + 1, buf.readUnsignedShort());
                    break;
                case "FL":
                    position.set(Position.KEY_FUEL_LEVEL, buf.readUnsignedByte());
                    break;
                case "ML":
                    buf.readUnsignedByte(); // mil status
                    break;
                case "FC":
                    position.set(Position.KEY_FUEL_CONSUMPTION, buf.readUnsignedInt());
                    break;
                case "CI":
                    readString(buf); // format string
                    break;
                case "AV1":
                    position.set(Position.PREFIX_ADC + 1, buf.readUnsignedShort());
                    break;
                case "NC":
                    readString(buf); // gsm neighbor cell info
                    break;
                case "SM":
                    buf.readUnsignedShort(); // max speed between reports
                    break;
                case "GL":
                    readString(buf); // google link
                    break;
                case "MA":
                    readString(buf); // mac address
                    break;
                case "PD":
                    buf.readUnsignedByte(); // pending code status
                    break;
                case "CD":
                    position.set(Position.KEY_ICCID, readString(buf));
                    break;
                case "CM":
                    buf.readLong(); // imsi
                    break;
                case "GN":
                    buf.skipBytes(60); // g sensor data
                    break;
                case "GV":
                    buf.skipBytes(6); // maximum g force
                    break;
                case "ME":
                    buf.readLong(); // imei
                    break;
                case "IA":
                    buf.readUnsignedByte(); // intake air temperature
                    break;
                case "MP":
                    buf.readUnsignedByte(); // manifold absolute pressure
                    break;
                default:
                    break;
            }
        }

        if (cellTower.getMobileCountryCode() != null
            && cellTower.getMobileNetworkCode() != null
            && cellTower.getCellId() != null && cellTower.getCellId() != 0
            && cellTower.getLocationAreaCode() != null) {
            position.setNetwork(new Network(cellTower));
        } else if (cellTower.getSignalStrength() != null) {
            position.set(Position.KEY_RSSI, cellTower.getSignalStrength());
        }
    }

    private static final Pattern PATTERN_INFO = new PatternBuilder()
            .text("$INFO=")
            .number("(d+),")                     // unit id
            .expression("([^,]+),")              // model
            .expression("([^,]+),")              // firmware version
            .number("d+,")                       // imei
            .number("d+,")                       // imsi
            .number("d+,")                       // sim card id
            .number("(d+),")                     // power
            .number("(d+),")                     // battery
            .number("(d+),")                     // satellites
            .number("d+,")                       // gsm status
            .number("(d+),")                     // rssi
            .number("d+,")                       // connection status
            .number("d+")                        // antenna status
            .any()
            .compile();

    private Position decodeInfo(Channel channel, SocketAddress remoteAddress, String sentence) {

        Position position = new Position(getProtocolName());

        getLastLocation(position, null);

        DeviceSession deviceSession;

        if (sentence.startsWith("$INFO")) {

            Parser parser = new Parser(PATTERN_INFO, sentence);
            if (!parser.matches()) {
                return null;
            }

            deviceSession = getDeviceSession(channel, remoteAddress, parser.next());

            position.set("model", parser.next());
            position.set(Position.KEY_VERSION_FW, parser.next());
            position.set(Position.KEY_POWER, parser.nextInt() * 0.1);
            position.set(Position.KEY_BATTERY, parser.nextInt() * 0.1);
            position.set(Position.KEY_SATELLITES, parser.nextInt());
            position.set(Position.KEY_RSSI, parser.nextInt());

        } else {

            deviceSession = getDeviceSession(channel, remoteAddress);

            position.set(Position.KEY_RESULT, sentence);

        }

        if (deviceSession == null) {
            return null;
        } else {
            position.setDeviceId(deviceSession.getDeviceId());
            return position;
        }
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .number("(d+),")                     // date and time
            .number("d+,")                       // rtc date and time
            .number("d+,")                       // device date and time
            .number("(-?d+),")                   // longitude
            .number("(-?d+),")                   // latitude
            .number("(d+),")                     // course
            .number("(d+),")                     // report id
            .number("(d+.?d*),")                 // odometer
            .number("(d+),")                     // hdop
            .number("(d+),")                     // inputs
            .number("(d+),")                     // speed
            .number("(d+),")                     // outputs
            .number("(d+),")                     // adc
            .number("([^,]+)?,")                 // driver
            .number("(d+),")                     // temp1
            .number("(d+),")                     // temp2
            .expression("[^,]*,")                // text message
            .expression("(.*)")                  // custom data
            .optional(2)
            .compile();

    private List<Position> decodeText(Channel channel, SocketAddress remoteAddress, String sentence) {

        int startIndex = -1;
        for (int i = 0; i < 4; i++) {
            startIndex = sentence.indexOf(',', startIndex + 1);
        }
        int endIndex = sentence.indexOf(',', startIndex + 1);

        String imei = sentence.substring(startIndex + 1, endIndex);
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        List<Position> positions = new LinkedList<>();
        String[] lines = sentence.substring(endIndex + 1).split("\r\n");

        for (String line : lines) {
            Position position = decodeTextLine(deviceSession, line);
            if (position != null) {
                positions.add(position);
            }
        }

        return positions;
    }


    private Position decodeTextLine(DeviceSession deviceSession, String sentence) {

        Parser parser = new Parser(PATTERN, sentence);
        if (!parser.matches()) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.setValid(true);

        String time = parser.next();
        if (time.length() >= 14) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                position.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            position.setTime(new Date(Long.parseLong(time) * 1000));
        }

        position.setLongitude(parser.nextInt() * 0.000001);
        position.setLatitude(parser.nextInt() * 0.000001);
        position.setCourse(parser.nextInt());

        position.set(Position.KEY_EVENT, parser.nextInt());
        position.set(Position.KEY_ODOMETER, parser.nextDouble() * 100);
        position.set(Position.KEY_HDOP, parser.nextInt() * 0.1);
        position.set(Position.KEY_INPUT, parser.nextInt());

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextInt()));

        position.set(Position.KEY_OUTPUT, parser.nextInt());
        position.set(Position.PREFIX_ADC + 1, parser.nextInt());

        if (parser.hasNext()) {
            position.set(Position.KEY_DRIVER_UNIQUE_ID, parser.next());
        }

        position.set(Position.PREFIX_TEMP + 1, parser.nextInt());
        position.set(Position.PREFIX_TEMP + 2, parser.nextInt());

        if (custom) {
            String data = parser.next();
            String form = this.form;
            if (form == null) {
                form = data.substring(0, data.indexOf(',')).substring("%CI".length());
                data = data.substring(data.indexOf(',') + 1);
            }
            readTextCustomData(position, data, form);
        }

        return position;
    }

    private Position decodePhoto(DeviceSession deviceSession, ByteBuf buf, long id) {

        long time = buf.readUnsignedInt();
        int index = buf.readUnsignedByte();
        int count = buf.readUnsignedByte();

        if (photo == null) {
            photo = Unpooled.buffer();
        }
        photo.writeBytes(buf.readSlice(buf.readUnsignedShort()));

        if (index == count - 1) {
            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            getLastLocation(position, new Date(time * 1000));

            position.set(Position.KEY_IMAGE, Context.getMediaManager().writeFile(String.valueOf(id), photo, "jpg"));
            photo.release();
            photo = null;

            return position;
        }

        return null;
    }

    private List<Position> decodeBinary(DeviceSession deviceSession, ByteBuf buf) {

        List<Position> positions = new LinkedList<>();

        while (buf.readableBytes() >= MIN_DATA_LENGTH) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            if (longDate) {

                DateBuilder dateBuilder = new DateBuilder()
                        .setDate(buf.readUnsignedShort(), buf.readUnsignedByte(), buf.readUnsignedByte())
                        .setTime(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
                position.setTime(dateBuilder.getDate());

                buf.skipBytes(7 + 7);

            } else {
                position.setFixTime(new Date(buf.readUnsignedInt() * 1000));
                position.setDeviceTime(new Date(buf.readUnsignedInt() * 1000));
                buf.readUnsignedInt(); // send time
            }

            position.setValid(true);
            position.setLongitude(buf.readInt() * 0.000001);
            position.setLatitude(buf.readInt() * 0.000001);
            position.setCourse(buf.readUnsignedShort());

            int type = buf.readUnsignedByte();
            position.set(Position.KEY_TYPE, type);
            position.set(Position.KEY_ALARM, alarmMap.get(type));

            position.set(Position.KEY_ODOMETER, buf.readUnsignedInt() * 100);
            position.set(Position.KEY_HDOP, buf.readUnsignedShort() * 0.1);
            position.set(Position.KEY_INPUT, buf.readUnsignedByte());

            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShort()));

            position.set(Position.KEY_OUTPUT, buf.readUnsignedByte());
            position.set(Position.PREFIX_ADC + 1, buf.readUnsignedShort() * 0.001);

            position.set(Position.KEY_DRIVER_UNIQUE_ID, readString(buf));

            position.set(Position.PREFIX_TEMP + 1, buf.readShort() * 0.1);
            position.set(Position.PREFIX_TEMP + 2, buf.readShort() * 0.1);

            String message = readString(buf);
            if (message != null && !message.isEmpty()) {
                Pattern pattern = Pattern.compile("FULS:F=(\\p{XDigit}+) t=(\\p{XDigit}+) N=(\\p{XDigit}+)");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    int value = Integer.parseInt(matcher.group(3), decimalFuel ? 10 : 16);
                    position.set(Position.KEY_FUEL_LEVEL, value * 0.1);
                } else {
                    position.set("message", message);
                }
            }

            if (custom) {
                String form = this.form;
                if (form == null) {
                    form = readString(buf).trim().substring("%CI".length());
                }
                readBinaryCustomData(position, buf, form);
            }

            positions.add(position);

        }

        return positions;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        if (buf.getUnsignedShort(buf.readerIndex()) == 0xfe02) {
            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage(buf.retain(), remoteAddress)); // keep-alive message
            }
            return null;
        } else if (buf.getByte(buf.readerIndex()) == '$') {
            return decodeInfo(channel, remoteAddress, buf.toString(StandardCharsets.US_ASCII).trim());
        } else if (buf.getByte(buf.readerIndex() + 2) == ',') {
            return decodeText(channel, remoteAddress, buf.toString(StandardCharsets.US_ASCII).trim());
        } else {

            String prefix = buf.readCharSequence(2, StandardCharsets.US_ASCII).toString();
            buf.readUnsignedShort(); // checksum
            buf.readUnsignedShort(); // length
            int index = buf.readUnsignedShort();

            long id = buf.readLong();
            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, String.valueOf(id));
            if (deviceSession == null) {
                return null;
            }

            sendResponse(channel, remoteAddress, id, index);

            if (prefix.equals("@R")) {
                return decodePhoto(deviceSession, buf, id);
            } else {
                return decodeBinary(deviceSession, buf);
            }

        }
    }

}
