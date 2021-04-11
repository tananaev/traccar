/*
 * Copyright 2012 - 2020 Anton Tananaev (anton@traccar.org)
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
package org.traccar;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.traccar.config.Keys;
import org.traccar.handler.ComputedAttributesHandler;
import org.traccar.handler.CopyAttributesHandler;
import org.traccar.handler.DefaultDataHandler;
import org.traccar.handler.DistanceHandler;
import org.traccar.handler.EngineHoursHandler;
import org.traccar.handler.FilterHandler;
import org.traccar.handler.GeocoderHandler;
import org.traccar.handler.GeolocationHandler;
import org.traccar.handler.HemisphereHandler;
import org.traccar.handler.MotionHandler;
import org.traccar.handler.NetworkMessageHandler;
import org.traccar.handler.OpenChannelHandler;
import org.traccar.handler.RemoteAddressHandler;
import org.traccar.handler.SpeedLimitHandler;
import org.traccar.handler.StandardLoggingHandler;
import org.traccar.handler.TimeHandler;
import org.traccar.handler.events.AlertEventHandler;
import org.traccar.handler.events.CommandResultEventHandler;
import org.traccar.handler.events.DriverEventHandler;
import org.traccar.handler.events.FuelDropEventHandler;
import org.traccar.handler.events.GeofenceEventHandler;
import org.traccar.handler.events.IgnitionEventHandler;
import org.traccar.handler.events.MaintenanceEventHandler;
import org.traccar.handler.events.MotionEventHandler;
import org.traccar.handler.events.OverspeedEventHandler;

import java.util.Map;

public abstract class BasePipelineFactory extends ChannelInitializer<Channel> {

    private final TrackerServer server;
    private final String protocol;
    private int timeout;

    public BasePipelineFactory(TrackerServer server, String protocol) {
        this.server = server;
        this.protocol = protocol;
        timeout = Context.getConfig().getInteger(Keys.PROTOCOL_TIMEOUT.withPrefix(protocol));
        if (timeout == 0) {
            timeout = Context.getConfig().getInteger(Keys.SERVER_TIMEOUT);
        }
    }

    protected abstract void addProtocolHandlers(PipelineBuilder pipeline);

    @SafeVarargs
    private final void addHandlers(ChannelPipeline pipeline, Class<? extends ChannelHandler>... handlerClasses) {
        for (Class<? extends ChannelHandler> handlerClass : handlerClasses) {
            if (handlerClass != null) {
                pipeline.addLast(Main.getInjector().getInstance(handlerClass));
            }
        }
    }

    public static <T extends ChannelHandler> T getHandler(ChannelPipeline pipeline, Class<T> clazz) {
        for (Map.Entry<String, ChannelHandler> handlerEntry : pipeline) {
            ChannelHandler handler = handlerEntry.getValue();
            if (handler instanceof WrapperInboundHandler) {
                handler = ((WrapperInboundHandler) handler).getWrappedHandler();
            } else if (handler instanceof WrapperOutboundHandler) {
                handler = ((WrapperOutboundHandler) handler).getWrappedHandler();
            }
            if (clazz.isAssignableFrom(handler.getClass())) {
                return (T) handler;
            }
        }
        return null;
    }

    @Override
    protected void initChannel(Channel channel) {
        final ChannelPipeline pipeline = channel.pipeline();

        if (timeout > 0 && !server.isDatagram()) {
            pipeline.addLast(new IdleStateHandler(timeout, 0, 0));
        }
        pipeline.addLast(new OpenChannelHandler(server));
        pipeline.addLast(new NetworkMessageHandler());
        pipeline.addLast(new StandardLoggingHandler(StandardLoggingHandler.NetworkLogFormat.valueOf(
                Context.getConfig().getString("logger.networkLogFormat",
                        StandardLoggingHandler.NetworkLogFormat.RawOrHex.name()))));

        addProtocolHandlers(handler -> {
            if (!(handler instanceof BaseProtocolDecoder || handler instanceof BaseProtocolEncoder)) {
                if (handler instanceof ChannelInboundHandler) {
                    handler = new WrapperInboundHandler((ChannelInboundHandler) handler);
                } else {
                    handler = new WrapperOutboundHandler((ChannelOutboundHandler) handler);
                }
            }
            pipeline.addLast(handler);
        });

        addHandlers(
                pipeline,
                TimeHandler.class,
                GeolocationHandler.class,
                HemisphereHandler.class,
                DistanceHandler.class,
                RemoteAddressHandler.class,
                FilterHandler.class,
                GeocoderHandler.class,
                SpeedLimitHandler.class,
                MotionHandler.class,
                CopyAttributesHandler.class,
                EngineHoursHandler.class,
                ComputedAttributesHandler.class,
                WebDataHandler.class,
                DefaultDataHandler.class,
                CommandResultEventHandler.class,
                OverspeedEventHandler.class,
                FuelDropEventHandler.class,
                MotionEventHandler.class,
                GeofenceEventHandler.class,
                AlertEventHandler.class,
                IgnitionEventHandler.class,
                MaintenanceEventHandler.class,
                DriverEventHandler.class);

        pipeline.addLast(new MainEventHandler());
    }

}
