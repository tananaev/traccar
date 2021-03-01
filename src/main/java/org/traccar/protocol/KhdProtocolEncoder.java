/*
 * Copyright 2015 - 2021 Anton Tananaev (anton@traccar.org)
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
import org.traccar.BaseProtocolEncoder;
import org.traccar.helper.Checksum;
import org.traccar.model.Command;
import org.traccar.Protocol;

public class KhdProtocolEncoder extends BaseProtocolEncoder {

    public static final int MSG_ON_DEMAND_TRACK = 0x30;
    public static final int MSG_CUT_OIL = 0x39;
    public static final int MSG_RESUME_OIL = 0x38;
    public static final int MSG_CHECK_VERSION = 0x3D;
    public static final int MSG_FACTORY_RESET = 0xC3;
    public static final int MSG_SET_OVERSPEED = 0x3F;
    public static final int MSG_DELETE_MILEAGE = 0x66;

    public KhdProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeCommand(int command, String uniqueId, ByteBuf content) {

        ByteBuf buf = Unpooled.buffer();

        buf.writeByte(0x29);
        buf.writeByte(0x29);

        buf.writeByte(command);

        int length = 6;
        if (content != null) {
            length += content.readableBytes();
        }
        buf.writeShort(length);

        uniqueId = "00000000".concat(uniqueId);
        uniqueId = uniqueId.substring(uniqueId.length() - 8);
        buf.writeByte(Integer.parseInt(uniqueId.substring(0, 2)));
        buf.writeByte(Integer.parseInt(uniqueId.substring(2, 4)) + 0x80);
        buf.writeByte(Integer.parseInt(uniqueId.substring(4, 6)) + 0x80);
        buf.writeByte(Integer.parseInt(uniqueId.substring(6, 8)));

        if (content != null) {
            buf.writeBytes(content);
        }

        buf.writeByte(Checksum.xor(buf.nioBuffer()));
        buf.writeByte(0x0D); // ending

        return buf;
    }

    @Override
    protected Object encodeCommand(Command command) {

        String uniqueId = getUniqueId(command.getDeviceId());

        switch (command.getType()) {
            case Command.TYPE_ENGINE_STOP:
                return encodeCommand(MSG_CUT_OIL, uniqueId, null);
            case Command.TYPE_ENGINE_RESUME:
                return encodeCommand(MSG_RESUME_OIL, uniqueId, null);
            case Command.TYPE_GET_VERSION:
                return encodeCommand(MSG_CHECK_VERSION, uniqueId, null);
            case Command.TYPE_FACTORY_RESET:
                return encodeCommand(MSG_FACTORY_RESET, uniqueId, null);
            case Command.TYPE_SET_SPEED_LIMIT:
                ByteBuf content = Unpooled.buffer();
                content.writeByte(Integer.parseInt(command.getString(Command.KEY_DATA)));
                return encodeCommand(MSG_RESUME_OIL, uniqueId, content);
            case Command.TYPE_SET_ODOMETER:
                return encodeCommand(MSG_DELETE_MILEAGE, uniqueId, null);
            case Command.TYPE_POSITION_SINGLE:
                return encodeCommand(MSG_ON_DEMAND_TRACK, uniqueId, null);
            default:
                return null;
        }
    }

}
