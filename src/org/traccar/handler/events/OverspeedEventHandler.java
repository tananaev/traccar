/*
 * Copyright 2016 - 2019 Anton Tananaev (anton@traccar.org)
 * Copyright 2018 Andrey Kunitsyn (andrey@traccar.org)
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
package org.traccar.handler.events;

import java.util.Collections;
import java.util.Map;

import io.netty.channel.ChannelHandler;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.database.DeviceManager;
import org.traccar.database.GeofenceManager;
import org.traccar.model.Device;
import org.traccar.model.DeviceState;
import org.traccar.model.Event;
import org.traccar.model.Geofence;
import org.traccar.model.Position;
import org.traccar.Context;

@ChannelHandler.Sharable
public class OverspeedEventHandler extends BaseEventHandler {

    public static final String ATTRIBUTE_SPEED = "speed";
    public static final String ATTRIBUTE_SPEED_LIMIT = "speedLimit";

    private final DeviceManager deviceManager;
    private final GeofenceManager geofenceManager;

    private final boolean notRepeat;
    private final long minimalDuration;
    private final boolean preferLowest;

    public OverspeedEventHandler(Config config, DeviceManager deviceManager, GeofenceManager geofenceManager) {
        this.deviceManager = deviceManager;
        this.geofenceManager = geofenceManager;
        notRepeat = config.getBoolean(Keys.EVENT_OVERSPEED_NOT_REPEAT);
        minimalDuration = config.getLong(Keys.EVENT_OVERSPEED_MINIMAL_DURATION) * 1000;
        preferLowest = config.getBoolean(Keys.EVENT_OVERSPEED_PREFER_LOWEST);
    }

    private Map<Event, Position> newEvent(DeviceState deviceState, double speedLimit) {
        Position position = deviceState.getOverspeedPosition();
        Event event = new Event(Event.TYPE_DEVICE_OVERSPEED, position.getDeviceId(), position.getId());
        event.set(ATTRIBUTE_SPEED, deviceState.getOverspeedPosition().getSpeed());
        event.set(ATTRIBUTE_SPEED_LIMIT, speedLimit);
        event.setGeofenceId(deviceState.getOverspeedGeofenceId());
        deviceState.setOverspeedState(notRepeat);
        deviceState.setOverspeedPosition(null);
        deviceState.setOverspeedGeofenceId(0);
        return Collections.singletonMap(event, position);
    }

    public Map<Event, Position> updateOverspeedState(DeviceState deviceState, double speedLimit) {
        Map<Event, Position> result = null;
        if (deviceState.getOverspeedState() != null && !deviceState.getOverspeedState()
                && deviceState.getOverspeedPosition() != null && speedLimit != 0) {
            long currentTime = System.currentTimeMillis();
            Position overspeedPosition = deviceState.getOverspeedPosition();
            long overspeedTime = overspeedPosition.getFixTime().getTime();
            if (overspeedTime + minimalDuration <= currentTime) {
                result = newEvent(deviceState, speedLimit);
            }
        }
        return result;
    }

    public Map<Event, Position> updateOverspeedState(
            DeviceState deviceState, Position position, double speedLimit, long geofenceId) {
        Map<Event, Position> result = null;

        Boolean oldOverspeed = deviceState.getOverspeedState();

        long currentTime = position.getFixTime().getTime();
        boolean newOverspeed = position.getSpeed() > speedLimit;
        if (newOverspeed && !oldOverspeed) {
            if (deviceState.getOverspeedPosition() == null) {
                deviceState.setOverspeedPosition(position);
                deviceState.setOverspeedGeofenceId(geofenceId);
            }
        } else if (oldOverspeed && !newOverspeed) {
            deviceState.setOverspeedState(false);
            deviceState.setOverspeedPosition(null);
            deviceState.setOverspeedGeofenceId(0);
        } else {
            deviceState.setOverspeedPosition(null);
            deviceState.setOverspeedGeofenceId(0);
        }
        Position overspeedPosition = deviceState.getOverspeedPosition();
        if (overspeedPosition != null) {
            long overspeedTime = overspeedPosition.getFixTime().getTime();
            if (newOverspeed && overspeedTime + minimalDuration <= currentTime) {
                result = newEvent(deviceState, speedLimit);
            }
        }
        return result;
    }

    @Override
    protected Map<Event, Position> analyzePosition(Position position) {

        long deviceId = position.getDeviceId();
        Device device = deviceManager.getById(deviceId);
        if (device == null) {
            return null;
        }
        if (!deviceManager.isLatestPosition(position) || !position.getValid()) {
            return null;
        }

        double speedLimit = deviceManager.lookupAttributeDouble(deviceId, ATTRIBUTE_SPEED_LIMIT, 0, false);

        double geofenceSpeedLimit = 0;
        long overspeedGeofenceId = 0;

        if (geofenceManager != null && device.getGeofenceIds() != null) {
            for (long geofenceId : device.getGeofenceIds()) {
                Geofence geofence = geofenceManager.getById(geofenceId);
                if (geofence != null) {
                    double currentSpeedLimit = geofence.getDouble(ATTRIBUTE_SPEED_LIMIT);
                    if (currentSpeedLimit > 0 && geofenceSpeedLimit == 0
                            || preferLowest && currentSpeedLimit < geofenceSpeedLimit
                            || !preferLowest && currentSpeedLimit > geofenceSpeedLimit) {
                        geofenceSpeedLimit = currentSpeedLimit;
                        overspeedGeofenceId = geofenceId;
                    }
                }
            }
        }
        if (geofenceSpeedLimit > 0) {
            speedLimit = geofenceSpeedLimit;
        }

        if (Context.getGeocoder() != null) {
            double geoLimit = Context.getGeocoder().getSpeedLimit(
                    position.getLatitude(), position.getLongitude(), null);
            if (geoLimit > 0) {
                speedLimit = geoLimit;
            }
        }

        if (speedLimit == 0) {
            return null;
        }

        Map<Event, Position> result = null;
        DeviceState deviceState = deviceManager.getDeviceState(deviceId);

        if (deviceState.getOverspeedState() == null) {
            deviceState.setOverspeedState(position.getSpeed() > speedLimit);
            deviceState.setOverspeedGeofenceId(position.getSpeed() > speedLimit ? overspeedGeofenceId : 0);
        } else {
            result = updateOverspeedState(deviceState, position, speedLimit, overspeedGeofenceId);
        }

        deviceManager.setDeviceState(deviceId, deviceState);
        return result;
    }

}
