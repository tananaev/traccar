/*
 * Copyright 2015 - 2020 Anton Tananaev (anton@traccar.org)
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

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.Context;
import org.traccar.DeviceSession;
import org.traccar.NetworkMessage;
import org.traccar.Protocol;
import org.traccar.config.Keys;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XirgoProtocolDecoder extends BaseProtocolDecoder {

    private Boolean newFormat;
    private String form;

    public XirgoProtocolDecoder(Protocol protocol) {
        super(protocol);
        form = Context.getConfig().getString(Keys.PROTOCOL_FORM.withPrefix(getProtocolName()));
    }

    public void setForm(String form) {
        this.form = form;
    }

    private static final Pattern PATTERN_OLD = new PatternBuilder()
            .text("$$")
            .number("(d+),")                     // imei
            .number("(d+),")                     // event
            .number("(dddd)/(dd)/(dd),")         // date (yyyy/mm/dd)
            .number("(dd):(dd):(dd),")           // time (hh:mm:ss)
            .number("(-?d+.?d*),")               // latitude
            .number("(-?d+.?d*),")               // longitude
            .number("(-?d+.?d*),")               // altitude
            .number("(d+.?d*),")                 // speed
            .number("(d+.?d*),")                 // course
            .number("(d+),")                     // satellites
            .number("(d+.?d*),")                 // hdop
            .number("(d+.d+),")                  // battery
            .number("(d+),")                     // gsm
            .number("(d+.?d*),")                 // odometer
            .number("(d+),")                     // gps
            .any()
            .compile();

    private static final Pattern PATTERN_NEW = new PatternBuilder()
            .text("$$")
            .number("(d+),")                     // imei
            .number("(d+),")                     // event
            .number("(dddd)/(dd)/(dd),")         // date (yyyy/mm/dd)
            .number("(dd):(dd):(dd),")           // time (hh:mm:ss)
            .number("(-?d+.?d*),")               // latitude
            .number("(-?d+.?d*),")               // longitude
            .number("(-?d+.?d*),")               // altitude
            .number("(d+.?d*),")                 // speed
            .number("d+.?d*,")                   // acceleration
            .number("d+.?d*,")                   // deceleration
            .number("d+,")
            .number("(d+.?d*),")                 // course
            .number("(d+),")                     // satellites
            .number("(d+.?d*),")                 // hdop
            .number("(d+.?d*),")                 // odometer
            .number("(d+.?d*),")                 // fuel consumption
            .number("(d+.d+),")                  // battery
            .number("(d+),")                     // gsm
            .number("(d+),")                     // gps
            .groupBegin()
            .number("d,")                        // reset mode
            .expression("([01])")                // input 1
            .expression("([01])")                // input 1
            .expression("([01])")                // input 1
            .expression("([01]),")               // output 1
            .number("(d+.?d*),")                 // adc 1
            .number("(d+.?d*),")                 // fuel level
            .number("d+,")                       // engine load
            .number("(d+),")                     // engine hours
            .number("(d+),")                     // oil pressure
            .number("(d+),")                     // oil level
            .number("(-?d+),")                   // oil temperature
            .number("(d+),")                     // coolant pressure
            .number("(d+),")                     // coolant level
            .number("(-?d+)")                    // coolant temperature
            .groupEnd("?")
            .any()
            .compile();

    private void decodeEvent(Position position, int event) {

        position.set(Position.KEY_EVENT, event);

        switch (event) {
            case 4001:
            case 4003:
            case 6011:
            case 6013:
                position.set(Position.KEY_IGNITION, true);
                break;
            case 4002:
            case 4004:
            case 6012:
            case 6014:
                position.set(Position.KEY_IGNITION, false);
                break;
            case 4005:
                position.set(Position.KEY_CHARGE, false);
                break;
            case 6002:
                position.set(Position.KEY_ALARM, Position.ALARM_OVERSPEED);
                break;
            case 6006:
                position.set(Position.KEY_ALARM, Position.ALARM_ACCELERATION);
                break;
            case 6007:
                position.set(Position.KEY_ALARM, Position.ALARM_BRAKING);
                break;
            case 6008:
                position.set(Position.KEY_ALARM, Position.ALARM_LOW_POWER);
                break;
            case 6009:
                position.set(Position.KEY_ALARM, Position.ALARM_POWER_CUT);
                break;
            case 6010:
                position.set(Position.KEY_ALARM, Position.ALARM_POWER_RESTORED);
                break;
            case 6016:
                position.set(Position.KEY_ALARM, Position.ALARM_IDLE);
                break;
            case 6017:
                position.set(Position.KEY_ALARM, Position.ALARM_TOW);
                break;
            case 6030:
            case 6071:
                position.set(Position.KEY_MOTION, true);
                break;
            case 6031:
                position.set(Position.KEY_MOTION, false);
                break;
            case 6032:
                position.set(Position.KEY_ALARM, Position.ALARM_PARKING);
                break;
            case 6090:
                position.set(Position.KEY_ALARM, Position.ALARM_REMOVING);
                break;
            case 6091:
                position.set(Position.KEY_ALARM, Position.ALARM_LOW_BATTERY);
                break;
            default:
                break;
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;

        if (channel instanceof NioDatagramChannel) {
            Matcher matcher = Pattern.compile("\\$\\$\\d+,(\\d+),.*,(\\d+)##").matcher(sentence);
            if (matcher.matches()) {
                String response = "!UDP_ACK," + matcher.group(1) + "," + matcher.group(2);
                channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
            }
        }

        if (form != null) {
            return decodeCustom(channel, remoteAddress, sentence);
        } else {
            return decodeFixed(channel, remoteAddress, sentence);
        }
    }


    private Object decodeCustom(
            Channel channel, SocketAddress remoteAddress, String sentence) {

        String[] keys = form.split("[,;]");
        String[] values = sentence.replace("$$", "").replace("##", "").split("[,;]");

        if (values.length < keys.length) {
            return null;
        }

        Position position = new Position(getProtocolName());
        DateBuilder dateBuilder = new DateBuilder();

        for (int i = 0; i < keys.length; i++) {
            switch (keys[i]) {
                case "UID":
                case "IM":
                    DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, values[i]);
                    if (deviceSession != null) {
                        position.setDeviceId(deviceSession.getDeviceId());
                    }
                    break;
                case "EV":
                    decodeEvent(position, Integer.parseInt(values[i]));
                    break;
                case "D":
                    String[] date = values[i].split("/");
                    dateBuilder.setMonth(Integer.parseInt(date[0]));
                    dateBuilder.setDay(Integer.parseInt(date[1]));
                    dateBuilder.setYear(Integer.parseInt(date[2]));
                    break;
                case "T":
                    String[] time = values[i].split(":");
                    dateBuilder.setHour(Integer.parseInt(time[0]));
                    dateBuilder.setMinute(Integer.parseInt(time[1]));
                    dateBuilder.setSecond(Integer.parseInt(time[2]));
                    break;
                case "LT":
                    if (values[i].contains(".")) {
                        position.setLatitude(Double.parseDouble(values[i]));
                    } else {
                        position.setLatitude(Double.parseDouble(values[i]) / 1000000);
                    }
                    break;
                case "LN":
                    if (values[i].contains(".")) {
                        position.setLongitude(Double.parseDouble(values[i]));
                    } else {
                        position.setLongitude(Double.parseDouble(values[i]) / 1000000);
                    }
                    break;
                case "AL":
                    position.setAltitude(Integer.parseInt(values[i]));
                    break;
                case "GSPT":
                    position.setSpeed(UnitsConverter.knotsFromKph(Double.parseDouble(values[i])));
                    break;
                case "HD":
                    if (values[i].contains(".")) {
                        position.setCourse(Double.parseDouble(values[i]));
                    } else {
                        position.setCourse(Integer.parseInt(values[i]) * 0.1);
                    }
                    break;
                case "SV":
                    position.set(Position.KEY_SATELLITES, Integer.parseInt(values[i]));
                    break;
                case "BV":
                    if (values[i].contains(".")) {
                        position.set(Position.KEY_BATTERY, Double.parseDouble(values[i]));
                    } else {
                        position.set(Position.KEY_BATTERY, Double.parseDouble((values[i])) / 100);
                    }
                    break;
                case "CQ":
                    position.set(Position.KEY_RSSI, Integer.parseInt(values[i]));
                    break;
                case "MI":
                    position.set(Position.KEY_ODOMETER, Integer.parseInt(values[i]));
                    break;
                case "GS":
                    position.setValid(Integer.parseInt(values[i]) == 3);
                    break;
                case "SI":
                    position.set(Position.KEY_ICCID, values[i]);
                    break;
                case "IG":
                    int ignition = Integer.parseInt(values[i]);
                    if (ignition > 0) {
                        position.set(Position.KEY_IGNITION, ignition == 1);
                    }
                    break;
                case "OT":
                    position.set(Position.KEY_OUTPUT, Integer.parseInt(values[i]));
                    break;
                default:
                    break;
            }
        }

        position.setTime(dateBuilder.getDate());

        return position.getDeviceId() > 0 ? position : null;
    }

    private Object decodeFixed(
            Channel channel, SocketAddress remoteAddress, String sentence) {

        Parser parser;
        if (newFormat == null) {
            parser = new Parser(PATTERN_NEW, sentence);
            if (parser.matches()) {
                newFormat = true;
            } else {
                parser = new Parser(PATTERN_OLD, sentence);
                if (parser.matches()) {
                    newFormat = false;
                } else {
                    return null;
                }
            }
        } else {
            if (newFormat) {
                parser = new Parser(PATTERN_NEW, sentence);
            } else {
                parser = new Parser(PATTERN_OLD, sentence);
            }
            if (!parser.matches()) {
                return null;
            }
        }

        Position position = new Position(getProtocolName());

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }
        position.setDeviceId(deviceSession.getDeviceId());

        decodeEvent(position, parser.nextInt());

        position.setTime(parser.nextDateTime());

        position.setLatitude(parser.nextDouble(0));
        position.setLongitude(parser.nextDouble(0));
        position.setAltitude(parser.nextDouble(0));
        position.setSpeed(UnitsConverter.knotsFromMph(parser.nextDouble(0)));
        position.setCourse(parser.nextDouble(0));

        position.set(Position.KEY_SATELLITES, parser.nextInt());
        position.set(Position.KEY_HDOP, parser.nextDouble());

        if (newFormat) {
            position.set(Position.KEY_ODOMETER, UnitsConverter.metersFromMiles(parser.nextDouble(0)));
            position.set(Position.KEY_FUEL_CONSUMPTION, parser.next());
        }

        position.set(Position.KEY_BATTERY, parser.nextDouble(0));
        position.set(Position.KEY_RSSI, parser.nextDouble());

        if (!newFormat) {
            position.set(Position.KEY_ODOMETER, UnitsConverter.metersFromMiles(parser.nextDouble(0)));
        }

        position.setValid(parser.nextInt(0) == 1);

        if (newFormat && parser.hasNext(13)) {
            position.set(Position.PREFIX_IN + 1, parser.nextInt());
            position.set(Position.PREFIX_IN + 2, parser.nextInt());
            position.set(Position.PREFIX_IN + 3, parser.nextInt());
            position.set(Position.PREFIX_OUT + 1, parser.nextInt());
            position.set(Position.PREFIX_ADC + 1, parser.nextDouble());
            position.set(Position.KEY_FUEL_LEVEL, parser.nextDouble());
            position.set(Position.KEY_HOURS, UnitsConverter.msFromHours(parser.nextInt()));
            position.set("oilPressure", parser.nextInt());
            position.set("oilLevel", parser.nextInt());
            position.set("oilTemp", parser.nextInt());
            position.set("coolantPressure", parser.nextInt());
            position.set("coolantLevel", parser.nextInt());
            position.set("coolantTemp", parser.nextInt());
        }

        return position;
    }

}


