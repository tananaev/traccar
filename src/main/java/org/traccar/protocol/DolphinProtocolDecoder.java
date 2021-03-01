/*
 * Copyright 2020 - 2021 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;
import org.traccar.protobuf.dolphin.Messages.DolphinMessages;

import java.net.SocketAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DolphinProtocolDecoder extends BaseProtocolDecoder {

    public DolphinProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        buf.readUnsignedShort(); // header
        buf.readUnsignedIntLE(); // index
        buf.readUnsignedShort(); // version
        buf.readUnsignedShort(); // flags
        int type = buf.readUnsignedShortLE();

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, String.valueOf(buf.readLongLE()));
        if (deviceSession == null) {
            return null;
        }

        int length = (int) buf.readUnsignedIntLE();
        buf.readUnsignedInt(); // reserved

        if (type == DolphinMessages.MessageType.DataPack_Request.getNumber()) {

            DolphinMessages.DataPackRequest message = DolphinMessages.DataPackRequest.parseFrom(
                    ByteBufUtil.getBytes(buf, buf.readerIndex(), length, false));

            if (channel != null) {
                DolphinMessages.DataPackResponse response = DolphinMessages.DataPackResponse.newBuilder()
                        .setResponse(DolphinMessages.DataPackResponseCode.DataPack_OK)
                        .build();
                channel.writeAndFlush(new NetworkMessage(
                        Unpooled.wrappedBuffer(response.toByteArray()), remoteAddress));
            }

            List<Position> positions = new LinkedList<>();

            for (int i = 0; i < message.getPointsCount(); i++) {

                Position position = new Position(getProtocolName());
                position.setDeviceId(deviceSession.getDeviceId());

                DolphinMessages.DataPoint point = message.getPoints(i);

                position.setValid(true);
                position.setTime(new Date(point.getTimestamp() * 1000L));
                position.setLatitude(point.getLatitude());
                position.setLongitude(point.getLongitude());
                position.setAltitude(point.getAltitude());
                position.setSpeed(UnitsConverter.knotsFromKph(point.getSpeed()));
                position.setCourse(point.getBearing());

                position.set(Position.KEY_SATELLITES, point.getSatellites());
                position.set(Position.KEY_HDOP, point.getHDOP());

                positions.add(position);

            }

            return positions;

        }

        return null;
    }

}
