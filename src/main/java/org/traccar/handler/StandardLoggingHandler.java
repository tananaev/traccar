/*
 * Copyright 2019 Anton Tananaev (anton@traccar.org)
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
package org.traccar.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.NetworkMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandardLoggingHandler extends ChannelDuplexHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardLoggingHandler.class);

    public enum NetworkLogFormat {
        HexOnly, RawAndHex, RawOrHex
    }

    private static final Pattern NON_ASCII_PATTERN = Pattern.compile("[^\\p{ASCII}].*", Pattern.DOTALL);

    private NetworkLogFormat networkLogFormat;

    public StandardLoggingHandler(NetworkLogFormat networkLogFormat) {
        this.networkLogFormat = networkLogFormat;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log(ctx, false, msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log(ctx, true, msg);
        super.write(ctx, msg, promise);
    }

    public void log(ChannelHandlerContext ctx, boolean downstream, Object o) {
        if (o instanceof NetworkMessage) {
            NetworkMessage networkMessage = (NetworkMessage) o;
            if (networkMessage.getMessage() instanceof ByteBuf) {
                log(ctx, downstream, networkMessage.getRemoteAddress(), (ByteBuf) networkMessage.getMessage());
            }
        } else if (o instanceof ByteBuf) {
            log(ctx, downstream, ctx.channel().remoteAddress(), (ByteBuf) o);
        }
    }

    public void log(ChannelHandlerContext ctx, boolean downstream, SocketAddress remoteAddress, ByteBuf buf) {
        StringBuilder message = new StringBuilder();

        message.append('[').append(ctx.channel().id().asShortText()).append(": ");
        message.append(((InetSocketAddress) ctx.channel().localAddress()).getPort());
        if (downstream) {
            message.append(" > ");
        } else {
            message.append(" < ");
        }

        if (remoteAddress instanceof InetSocketAddress) {
            message.append(((InetSocketAddress) remoteAddress).getHostString());
        } else {
            message.append("unknown");
        }
        message.append(']');

        boolean logRaw = networkLogFormat != NetworkLogFormat.HexOnly;
        boolean logHex = true;
        String rawMessage = null;
        if (logRaw) {
            rawMessage = buf.toString(StandardCharsets.UTF_8);
            Matcher matcher = NON_ASCII_PATTERN.matcher(rawMessage);
            if (matcher.find()) {
                rawMessage = matcher.replaceFirst("<binary...>");
            } else {
                logHex = networkLogFormat != NetworkLogFormat.RawOrHex;
            }
        }
        if (logRaw) {
            message.append(' ');
            message.append(rawMessage);
        }
        if (logHex) {
            String hexMessage = ByteBufUtil.hexDump(buf);
            message.append(" HEX: ");
            message.append(hexMessage);
        }

        LOGGER.info(message.toString());
    }

}
