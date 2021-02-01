/*
 * Copyright 2012 - 2021 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.DataConverter;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gps103ProtocolDecoder extends BaseProtocolDecoder {

    private int photoPackets = 0;
    private ByteBuf photo;

    public Gps103ProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("imei:")
            .number("(d+),")                     // imei
            .expression("([^,]+),")              // alarm
            .groupBegin()
            .number("(dd)/?(dd)/?(dd) ?")        // local date (yymmdd)
            .number("(dd):?(dd)(?:dd)?,")        // local time (hhmmss)
            .or()
            .number("d*,")
            .groupEnd()
            .expression("([^,]+)?,")             // rfid
            .groupBegin()
            .text("L,,,")
            .number("(x+),,")                    // lac
            .number("(x+),,,")                   // cid
            .or()
            .text("F,")
            .groupBegin()
            .number("(dd)(dd)(dd)(?:.d+)?")      // time utc (hhmmss)
            .or()
            .number("(?:d{1,5}.d+)?")
            .groupEnd()
            .text(",")
            .expression("([AV]),")               // validity
            .expression("([NS]),").optional()
            .number("(d+)(dd.d+),")              // latitude (ddmm.mmmm)
            .expression("([NS]),").optional()
            .expression("([EW]),").optional()
            .number("(d+)(dd.d+),")              // longitude (dddmm.mmmm)
            .expression("([EW])?,").optional()
            .number("(d+.?d*)?").optional()      // speed
            .number(",(d+.?d*)?").optional()     // course
            .number(",(-?d+.?d*)?").optional()   // altitude
            .number(",([01])?").optional()       // ignition
            .number(",([01])?").optional()       // door
            .groupBegin()
            .number(",(?:(d+.d+)%)?")            // fuel 1
            .number(",(?:(d+.d+)%|d+)?")         // fuel 2
            .groupEnd("?")
            .number(",([-+]?d+)?").optional()    // temperature
            .groupEnd()
            .any()
            .compile();

    private static final Pattern PATTERN_OBD = new PatternBuilder()
            .text("imei:")
            .number("(d+),")                     // imei
            .expression("OBD,")                  // type
            .number("(dd)(dd)(dd)")              // date (yymmdd)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(d+)?,")                    // odometer
            .number("(d+.d+)?,")                 // fuel instant
            .number("(d+.d+)?,")                 // fuel average
            .number("(d+)?,")                    // hours
            .number("(d+),")                     // speed
            .number("(d+.?d*%),")                // power load
            .number("(?:([-+]?d+)|[-+]?),")      // temperature
            .number("(d+.?d*%),")                // throttle
            .number("(d+),")                     // rpm
            .number("(d+.d+),")                  // battery
            .number("([^;]*)")                   // dtcs
            .any()
            .compile();

    private static final Pattern PATTERN_ALT = new PatternBuilder()
            .text("imei:")
            .number("(d+),")                     // imei
            .expression("[^,]+,")
            .expression("(?:-+|(.+)),")          // event
            .expression("(?:-+|(.+)),")          // sensor id
            .expression("(?:-+|(.+)),")          // sensor voltage
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(dd)(dd)(dd),")             // date (ddmmyy)
            .number("(d+),")                     // rssi
            .number("(d),")                      // gps status
            .number("(-?d+.d+),")                // latitude
            .number("(-?d+.d+),")                // longitude
            .number("(d+),")                     // speed
            .number("(d+),")                     // course
            .number("(-?d+),")                   // altitude
            .number("(d+.d+),")                  // hdop
            .number("(d+),")                     // satellites
            .number("([01]),")                   // ignition
            .number("([01]),")                   // charge
            .expression("(?:-+|(.+))")           // error
            .any()
            .compile();

    private String decodeAlarm(String value) {
        if (value.startsWith("T:")) {
            return Position.ALARM_TEMPERATURE;
        } else if (value.startsWith("oil")) {
            return Position.ALARM_FUEL_LEAK;
        }
        switch (value) {
            case "tracker":
                return null;
            case "help me":
                return Position.ALARM_SOS;
            case "low battery":
                return Position.ALARM_LOW_BATTERY;
            case "stockade":
                return Position.ALARM_GEOFENCE;
            case "move":
                return Position.ALARM_MOVEMENT;
            case "speed":
                return Position.ALARM_OVERSPEED;
            case "acc on":
                return Position.ALARM_POWER_ON;
            case "acc off":
                return Position.ALARM_POWER_OFF;
            case "door alarm":
                return Position.ALARM_DOOR;
            case "ac alarm":
                return Position.ALARM_POWER_CUT;
            case "accident alarm":
                return Position.ALARM_ACCIDENT;
            case "sensor alarm":
                return Position.ALARM_SHOCK;
            case "bonnet alarm":
                return Position.ALARM_BONNET;
            case "footbrake alarm":
                return Position.ALARM_FOOT_BRAKE;
            case "DTC":
                return Position.ALARM_FAULT;
            default:
                return null;
        }
    }

    private Position decodeRegular(Channel channel, SocketAddress remoteAddress, String sentence) {

        Parser parser = new Parser(PATTERN, sentence);
        if (!parser.matches()) {
            return null;
        }

        String imei = parser.next();
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        String alarm = parser.next();
        position.set(Position.KEY_ALARM, decodeAlarm(alarm));
        if (alarm.equals("help me")) {
            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage("**,imei:" + imei + ",E;", remoteAddress));
            }
        } else if (alarm.startsWith("vt")) {
            photoPackets = Integer.parseInt(alarm.substring(2));
            photo = Unpooled.buffer();
        } else if (alarm.equals("acc on")) {
            position.set(Position.KEY_IGNITION, true);
        } else if (alarm.equals("acc off")) {
            position.set(Position.KEY_IGNITION, false);
        } else if (alarm.startsWith("T:")) {
            position.set(Position.PREFIX_TEMP + 1, Double.parseDouble(alarm.substring(2)));
        } else if (alarm.startsWith("oil ")) {
            position.set(Position.KEY_FUEL_LEVEL, Double.parseDouble(alarm.substring(4)));
        } else if (!position.getAttributes().containsKey(Position.KEY_ALARM) && !alarm.equals("tracker")) {
            position.set(Position.KEY_EVENT, alarm);
        }

        DateBuilder dateBuilder = new DateBuilder()
                .setDate(parser.nextInt(0), parser.nextInt(0), parser.nextInt(0));

        int localHours = parser.nextInt(0);
        int localMinutes = parser.nextInt(0);

        String rfid = parser.next();
        if (alarm.equals("rfid")) {
            position.set(Position.KEY_DRIVER_UNIQUE_ID, rfid);
        }

        if (parser.hasNext(2)) {

            getLastLocation(position, null);

            position.setNetwork(new Network(CellTower.fromLacCid(parser.nextHexInt(0), parser.nextHexInt(0))));

        } else {

            String utcHours = parser.next();
            String utcMinutes = parser.next();

            dateBuilder.setTime(localHours, localMinutes, parser.nextInt(0));

            // Timezone calculation
            if (utcHours != null && utcMinutes != null) {
                int deltaMinutes = (localHours - Integer.parseInt(utcHours)) * 60;
                deltaMinutes += localMinutes - Integer.parseInt(utcMinutes);
                if (deltaMinutes <= -12 * 60) {
                    deltaMinutes += 24 * 60;
                } else if (deltaMinutes > 12 * 60) {
                    deltaMinutes -= 24 * 60;
                }
                dateBuilder.addMinute(-deltaMinutes);
            }
            position.setTime(dateBuilder.getDate());

            position.setValid(parser.next().equals("A"));
            position.setFixTime(position.getDeviceTime());
            position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG_MIN_HEM));
            position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG_MIN_HEM));
            position.setSpeed(parser.nextDouble(0));
            position.setCourse(parser.nextDouble(0));
            position.setAltitude(parser.nextDouble(0));

            if (parser.hasNext()) {
                position.set(Position.KEY_IGNITION, parser.nextInt() == 1);
            }
            if (parser.hasNext()) {
                position.set(Position.KEY_DOOR, parser.nextInt() == 1);
            }
            position.set("fuel1", parser.nextDouble());
            position.set("fuel2", parser.nextDouble());
            position.set(Position.PREFIX_TEMP + 1, parser.nextInt());

        }

        return position;
    }

    private Position decodeObd(Channel channel, SocketAddress remoteAddress, String sentence) {

        Parser parser = new Parser(PATTERN_OBD, sentence);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        getLastLocation(position, parser.nextDateTime());

        position.set(Position.KEY_ODOMETER, parser.nextInt(0));
        parser.nextDouble(0); // instant fuel consumption
        position.set(Position.KEY_FUEL_CONSUMPTION, parser.nextDouble(0));
        if (parser.hasNext()) {
            position.set(Position.KEY_HOURS, UnitsConverter.msFromHours(parser.nextInt()));
        }
        position.set(Position.KEY_OBD_SPEED, parser.nextInt(0));
        position.set(Position.KEY_ENGINE_LOAD, parser.next());
        position.set(Position.KEY_COOLANT_TEMP, parser.nextInt());
        position.set(Position.KEY_THROTTLE, parser.next());
        position.set(Position.KEY_RPM, parser.nextInt(0));
        position.set(Position.KEY_BATTERY, parser.nextDouble(0));
        position.set(Position.KEY_DTCS, parser.next().replace(',', ' ').trim());

        return position;
    }


    private Position decodeAlternative(Channel channel, SocketAddress remoteAddress, String sentence) {

        Parser parser = new Parser(PATTERN_ALT, sentence);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.set(Position.KEY_EVENT, parser.next());
        position.set("sensorId", parser.next());
        position.set("sensorVoltage", parser.nextDouble());

        position.setTime(parser.nextDateTime(Parser.DateTimeFormat.HMS_DMY));

        position.set(Position.KEY_RSSI, parser.nextInt());

        position.setValid(parser.nextInt() > 0);
        position.setLatitude(parser.nextDouble());
        position.setLongitude(parser.nextDouble());
        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextInt()));
        position.setCourse(parser.nextInt());
        position.setAltitude(parser.nextInt());

        position.set(Position.KEY_HDOP, parser.nextDouble());
        position.set(Position.KEY_SATELLITES, parser.nextInt());
        position.set(Position.KEY_IGNITION, parser.nextInt() > 0);
        position.set(Position.KEY_CHARGE, parser.nextInt() > 0);
        position.set("error", parser.next());

        return position;
    }

    private Position decodePhoto(Channel channel, SocketAddress remoteAddress, String sentence) {

        String imei = sentence.substring(5, 5 + 15);
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        ByteBuf buf = Unpooled.wrappedBuffer(DataConverter.parseHex(
                sentence.substring(24, sentence.endsWith(";") ? sentence.length() - 1 : sentence.length())));
        int index = buf.readUnsignedShortLE();
        photo.writeBytes(buf, buf.readerIndex() + 2, buf.readableBytes() - 4);

        if (index + 1 >= photoPackets) {
            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            getLastLocation(position, null);

            try {
                position.set(Position.KEY_IMAGE, Context.getMediaManager().writeFile(imei, photo, "jpg"));
            } finally {
                photoPackets = 0;
                photo.release();
                photo = null;
            }

            return position;
        } else {
            return null;
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;

        if (sentence.contains("imei:") && sentence.length() <= 30) {
            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage("LOAD", remoteAddress));
                Matcher matcher = Pattern.compile("imei:(\\d+),").matcher(sentence);
                if (matcher.find()) {
                    getDeviceSession(channel, remoteAddress, matcher.group(1));
                }
            }
            return null;
        }

        if (!sentence.isEmpty() && Character.isDigit(sentence.charAt(0))) {
            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage("ON", remoteAddress));
            }
            int start = sentence.indexOf("imei:");
            if (start >= 0) {
                sentence = sentence.substring(start);
            } else {
                return null;
            }
        }

        if (sentence.startsWith("vr", 21)) {
            return decodePhoto(channel, remoteAddress, sentence);
        } else if (sentence.substring(21, 21 + 3).contains("OBD")) {
            return decodeObd(channel, remoteAddress, sentence);
        } else if (sentence.endsWith("*")) {
            return decodeAlternative(channel, remoteAddress, sentence);
        } else {
            return decodeRegular(channel, remoteAddress, sentence);
        }
    }

}
