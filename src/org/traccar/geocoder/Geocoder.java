/*
 * Copyright 2012 - 2017 Anton Tananaev (anton@traccar.org)
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
package org.traccar.geocoder;

public interface Geocoder {

    interface ReverseGeocoderCallback {

        void onSuccess(String address);
        void onSuccess(double speed);

        void onFailure(Throwable e);

    }

    String getAddress(double latitude, double longitude, ReverseGeocoderCallback callback);
    double getSpeedLimit(double latitude, double longitude, ReverseGeocoderCallback callback);

}
