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

import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.Protocol;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class SiwiProtocolDecoder extends BaseProtocolDecoder {

    public SiwiProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("$").expression("[A-Z]+,")     // header
            .number("(d+),")                     // device id
            .number("d+,")                       // unit no
            .expression("([A-Z]),")              // reason
            .number("d*,")                       // command code
            .number("[^,]*,")                    // command value
            .expression("([01]),")               // ignition
            .expression("[01],")                 // power cut
            .number("d+,")                       // flags
            .number("[^,]+,")
            .number("(d+),")                     // odometer
            .number("(d+),")                     // speed
            .number("(d+),")                     // satellites
            .expression("([AV]),")               // valid
            .number("(-?d+.d+),")                // latitude
            .number("(-?d+.d+),")                // longitude
            .number("(-?d+),")                   // altitude
            .number("(d+),")                     // course
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(dd)(dd)(dd),")             // date (ddmmyy)
            .number("d+,")                       // signal strength
            .number("d+,")                       // gsm status
            .number("d+,")                       // error code
            .number("d+,")                       // internal status
            .number("(d+),")                     // battery
            .number("(d+),")                     // adc
            .number("(d+),")                     // digital inputs
            .number("(d+),")                     // sensor 1
            .number("(d+),")                     // sensor 2
            .number("(d+),")                     // sensor 3
            .number("(d+),")                     // sensor 4
            .expression("([^,]+),")              // hw version
            .expression("([^,]+),")              // sw version
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        Parser parser = new Parser(PATTERN, (String) msg);
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
        position.set(Position.KEY_IGNITION, parser.next().equals("1"));
        position.set(Position.KEY_ODOMETER, parser.nextInt(0));

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextInt(0)));

        position.set(Position.KEY_SATELLITES, parser.nextInt(0));

        position.setValid(parser.next().equals("A"));
        position.setLatitude(parser.nextDouble(0));
        position.setLongitude(parser.nextDouble(0));
        position.setAltitude(parser.nextDouble(0));
        position.setCourse(parser.nextInt(0));

        position.setTime(parser.nextDateTime(Parser.DateTimeFormat.HMS_DMY, "IST"));

        position.set(Position.KEY_BATTERY, parser.nextInt() * 0.001);
        position.set(Position.PREFIX_ADC + 1, parser.nextInt() * 0.01);
        position.set(Position.KEY_INPUT, parser.nextInt());

        for (int i = 1; i <= 4; i++) {
            int value = parser.nextInt();
            if (value != 0) {
                position.set(Position.PREFIX_IO + i, value);
            }
        }

        position.set(Position.KEY_VERSION_HW, parser.next());
        position.set(Position.KEY_VERSION_FW, parser.next());

        return position;
    }

}
