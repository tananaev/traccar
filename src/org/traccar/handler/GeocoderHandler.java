/*
 * Copyright 2012 - 2019 Anton Tananaev (anton@traccar.org)
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

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.Context;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.database.IdentityManager;
import org.traccar.database.StatisticsManager;
import org.traccar.geocoder.Geocoder;
import org.traccar.model.Position;

@ChannelHandler.Sharable
public class GeocoderHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeocoderHandler.class);

    private final Geocoder geocoder;
    private final IdentityManager identityManager;
    private final StatisticsManager statisticsManager;
    private final boolean ignorePositions;
    private final boolean processInvalidPositions;
    private final int geocoderReuseDistance;

    public GeocoderHandler(
            Config config, Geocoder geocoder, IdentityManager identityManager, StatisticsManager statisticsManager) {
        this.geocoder = geocoder;
        this.identityManager = identityManager;
        this.statisticsManager = statisticsManager;
        ignorePositions = Context.getConfig().getBoolean(Keys.GEOCODER_IGNORE_POSITIONS);
        processInvalidPositions = config.getBoolean(Keys.GEOCODER_PROCESS_INVALID_POSITIONS);
        geocoderReuseDistance = config.getInteger(Keys.GEOCODER_REUSE_DISTANCE, 0);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object message) {
        if (message instanceof Position && !ignorePositions) {
            final Position position = (Position) message;
            if (processInvalidPositions || position.getValid()) {
                if (geocoderReuseDistance != 0) {
                    Position lastPosition = identityManager.getLastPosition(position.getDeviceId());
                    if (lastPosition != null && lastPosition.getAddress() != null
                            && position.getDouble(Position.KEY_DISTANCE) <= geocoderReuseDistance) {
                        position.setAddress(lastPosition.getAddress());
                        ctx.fireChannelRead(position);
                        return;
                    }
                }

                if (statisticsManager != null) {
                    statisticsManager.registerGeocoderRequest();
                }

                geocoder.getAddress(position.getLatitude(), position.getLongitude(),
                        new Geocoder.ReverseGeocoderCallback() {
                    @Override
                    public void onSuccess(String address) {
                        position.setAddress(address);
                        ctx.fireChannelRead(position);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        LOGGER.warn("Geocoding failed", e);
                        ctx.fireChannelRead(position);
                    }

                    @Override
                    public void onSuccess(double speed) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
            } else {
                ctx.fireChannelRead(position);
            }
        } else {
            ctx.fireChannelRead(message);
        }
    }

}
