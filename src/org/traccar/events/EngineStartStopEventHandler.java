/*
 * Copyright 2017 Anton Tananaev (anton@traccar.org)
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
package org.traccar.events;

import org.traccar.BaseEventHandler;
import org.traccar.Context;
import org.traccar.model.Device;
import org.traccar.model.Event;
import org.traccar.model.Position;

import java.util.Collection;
import java.util.Collections;

public class EngineStartStopEventHandler extends BaseEventHandler {

    // Define new attribute
    public static final String ATTRIBUTE_ENGINE_START_STOP_POWER_THRESHOLD = "engineStartStopPowerThreshold";

    @Override
    protected Collection<Event> analyzePosition(Position position) {

        Device device = Context.getIdentityManager().getDeviceById(position.getDeviceId());
        if (device == null) {
            return null;
        }
        if (!Context.getIdentityManager().isLatestPosition(position)) {
            return null;
        }

        // Getting ATTRIBUTE_ENGINE_START_STOP_POWER_THRESHOLD value
        double engineStartStopPowerThreshold = Context.getDeviceManager()
                .lookupAttributeDouble(device.getId(), ATTRIBUTE_ENGINE_START_STOP_POWER_THRESHOLD, 0, false);

        // If no power threshold specified for current device using attributes
        if (engineStartStopPowerThreshold == 0) {
            return null;
        }

        // Getting current power
        double power = position.getAttributes().containsKey(Position.KEY_POWER);

        // Getting old power
        double oldPower = 0;
        Position lastPosition = Context.getIdentityManager().getLastPosition(position.getDeviceId());
        if (lastPosition != null) {
            oldPower = lastPosition.getAttributes().containsKey(Position.KEY_POWER);
        }

        // Here comes the logic to determine if engine has started or stopped
        if (power > engineStartStopPowerThreshold && oldPower < engineStartStopPowerThreshold) {
            return Collections.singleton(
                    new Event(Event.TYPE_ENGINE_STARTED, position.getDeviceId(), position.getId()));
        } else if (power < engineStartStopPowerThreshold && oldPower > engineStartStopPowerThreshold) {
            return Collections.singleton(
                    new Event(Event.TYPE_ENGINE_STOPPED, position.getDeviceId(), position.getId()));
        }
        return null;

    }

}
