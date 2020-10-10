/*
 * Copyright 2018 Anton Tananaev (anton@traccar.org)
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
package org.traccar.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.Context;
import org.traccar.config.Keys;

import java.util.HashSet;
import java.util.Set;

public final class EventForwarderManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventForwarderManager.class);

    private final Set<EventForwarder> eventForwarders = new HashSet<>();

    public EventForwarderManager(boolean defaultForwardEnabled) {
        if (defaultForwardEnabled) {
            eventForwarders.add(new JsonTypeEventForwarder());
        }

        addDynamicEventForwards();
    }

    private void addDynamicEventForwards() {
        String handlers = Context.getConfig().getString(Keys.EXTRA_EVENT_FORWARDERS);
        if (handlers != null) {
            for (String handler : handlers.split(",")) {
                try {
                    eventForwarders.add((EventForwarder) Class.forName(handler).getDeclaredConstructor().newInstance());
                } catch (ReflectiveOperationException error) {
                    LOGGER.warn("Dynamic event forwarder error", error);
                }
            }
        }
    }

    public Set<EventForwarder> getAllEventForwarders() {
        return eventForwarders;
    }
}
