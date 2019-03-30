/*
 * Copyright 2015 - 2018 Anton Tananaev (anton@traccar.org)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.Context;
import org.traccar.helper.UnitsConverter;

import javax.json.JsonObject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class JsonGeocoder implements Geocoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonGeocoder.class);

    private final String url;
    private final AddressFormat addressFormat;

    private Map<Map.Entry<Double, Double>, String> cache;
    private Map<Map.Entry<Double, Double>, Double> speedcache;

    public JsonGeocoder(String url, final int cacheSize, AddressFormat addressFormat) {
        this.url = url;
        this.addressFormat = addressFormat;
        if (cacheSize > 0) {
            this.cache = Collections.synchronizedMap(new LinkedHashMap<Map.Entry<Double, Double>, String>() {
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > cacheSize;
                }
            });
            this.speedcache = Collections.synchronizedMap(new LinkedHashMap<Map.Entry<Double, Double>, Double>() {
                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > cacheSize;
                }
            });
        }
    }

    private String handleResponse(
            double latitude, double longitude, JsonObject json, ReverseGeocoderCallback callback) {

        Address address = parseAddress(json);
        if (address != null) {
            String formattedAddress = addressFormat.format(address);
            if (cache != null) {
                cache.put(new AbstractMap.SimpleImmutableEntry<>(latitude, longitude), formattedAddress);
            }
            if (callback != null) {
                callback.onSuccess(formattedAddress);
            }
            return formattedAddress;
        } else {
            String msg = "Empty address. Error: " + parseError(json);
            if (callback != null) {
                callback.onFailure(new GeocoderException(msg));
            } else {
                LOGGER.warn(msg);
            }
        }
        return null;
    }

    private double handleSpeedResponse(
            double latitude, double longitude, JsonObject json, ReverseGeocoderCallback callback) {

        Address address = parseAddress(json);
        if (address.getSpeedLimit() != null) {
            String speedStr = address.getSpeedLimit();
            double speed;
            if (speedStr.endsWith("mph")) {
                // units in mph
                speed = UnitsConverter.knotsFromMph(Double.parseDouble(speedStr.replaceAll("[^\\d.]", "")));
            } else {
                // units in km/h
                speed = UnitsConverter.knotsFromKph(Double.parseDouble(speedStr.replaceAll("[^\\d.]", "")));
            }
            if (speedcache != null) {
                speedcache.put(new AbstractMap.SimpleImmutableEntry<>(latitude, longitude), speed);
            }
            if (callback != null) {
                callback.onSuccess(speed);
            }
            return speed;
        }
        return 0;
    }

    @Override
    public String getAddress(
            final double latitude, final double longitude, final ReverseGeocoderCallback callback) {

        if (cache != null) {
            String cachedAddress = cache.get(new AbstractMap.SimpleImmutableEntry<>(latitude, longitude));
            if (cachedAddress != null) {
                if (callback != null) {
                    callback.onSuccess(cachedAddress);
                }
                return cachedAddress;
            }
        }

        Invocation.Builder request = Context.getClient().target(String.format(url, latitude, longitude)).request();

        if (callback != null) {
            request.async().get(new InvocationCallback<JsonObject>() {
                @Override
                public void completed(JsonObject json) {
                    handleResponse(latitude, longitude, json, callback);
                }

                @Override
                public void failed(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        } else {
            try {
                return handleResponse(latitude, longitude, request.get(JsonObject.class), null);
            } catch (ClientErrorException e) {
                LOGGER.warn("Geocoder network error", e);
            }
        }
        return null;
    }

    @Override
    public double getSpeedLimit(
            final double latitude, final double longitude, final ReverseGeocoderCallback callback) {

        if (speedcache != null) {
            double cachedSpeedLimit = speedcache.get(new AbstractMap.SimpleImmutableEntry<>(latitude, longitude));
            if (cachedSpeedLimit != 0) {
                if (callback != null) {
                    callback.onSuccess(cachedSpeedLimit);
                }
                return cachedSpeedLimit;
            }
        }

        Invocation.Builder request = Context.getClient().target(String.format(url, latitude, longitude)).request();

        if (callback != null) {
            request.async().get(new InvocationCallback<JsonObject>() {
                @Override
                public void completed(JsonObject json) {
                    handleSpeedResponse(latitude, longitude, json, callback);
                }

                @Override
                public void failed(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        } else {
            try {
                return handleSpeedResponse(latitude, longitude, request.get(JsonObject.class), null);
            } catch (ClientErrorException e) {
                LOGGER.warn("Geocoder network error", e);
            }
        }
        return 0;
    }

    public abstract Address parseAddress(JsonObject json);

    protected String parseError(JsonObject json) {
        return null;
    }

}
