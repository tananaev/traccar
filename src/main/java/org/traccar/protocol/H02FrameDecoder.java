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

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.traccar.BaseFrameDecoder;

public class H02FrameDecoder extends BaseFrameDecoder {

    private static final int MESSAGE_SHORT = 32;
    private static final int MESSAGE_LONG = 45;
    private static final int LBS_DATASET_LENGTH = 8;
    private static final int WIFI_DATASET_LENGTH = 7;

    private int messageLength;

    public H02FrameDecoder(int messageLength) {
        this.messageLength = messageLength;
    }

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        char marker = (char) buf.getByte(buf.readerIndex());

        while (marker != '*' && marker != '$' && marker != 'X' && buf.readableBytes() > 0) {
            buf.skipBytes(1);
            if (buf.readableBytes() > 0) {
                marker = (char) buf.getByte(buf.readerIndex());
            }
        }

        switch (marker) {
            case '*':

                // Return text message
                int index = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) '#');
                if (index != -1) {
                    ByteBuf result = buf.readRetainedSlice(index + 1 - buf.readerIndex());
                    while (buf.isReadable()
                            && (buf.getByte(buf.readerIndex()) == '\r' || buf.getByte(buf.readerIndex()) == '\n')) {
                        buf.readByte(); // skip new line
                    }
                    return result;
                }

                break;

            case '$':

                if (messageLength == 0) {
                    if (buf.readableBytes() == MESSAGE_LONG) {
                        messageLength = MESSAGE_LONG;
                    } else {
                        messageLength = MESSAGE_SHORT;
                    }
                } else {
                    // messagelength = 37 in config (for GPS data)
                    if (buf.capacity() > messageLength) {
                		byte dataFormat = buf.getByte(messageLength - 7);
                        
                		if (dataFormat > 0x00) {
                            byte count = buf.getByte(messageLength - 6);
                			// LBS Data
                			if (dataFormat == 0x01) {
                				messageLength = MESSAGE_SHORT + count * LBS_DATASET_LENGTH + 1;
                			}
                			// Wifi Data
                			else if (dataFormat == 0x02) {
                				messageLength = MESSAGE_SHORT + count * WIFI_DATASET_LENGTH + 1;
                			}
                		}
                	}
                }

                if (buf.readableBytes() >= messageLength) {
                    return buf.readRetainedSlice(messageLength);
                }

                break;

            case 'X':

                if (buf.readableBytes() >= MESSAGE_SHORT) {
                    return buf.readRetainedSlice(MESSAGE_SHORT);
                }

                break;

            default:

                return null;
        }

        return null;
    }

}
