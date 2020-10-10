/*
 * Copyright 2016 - 2019 Anton Tananaev (anton@traccar.org)
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

import org.traccar.model.Event;
import org.traccar.model.Position;

import java.util.Set;

public abstract class EventForwarder {
    public void forwardEventAsync(final Event event, final Position position, final Set<Long> users) {
        new Thread(new Runnable() {
            public void run() {
                forwardEvent(event, position, users);
            }
        }).start();
    }

    public abstract void forwardEvent(Event event, Position position, Set<Long> users);
}
