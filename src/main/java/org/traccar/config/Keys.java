/*
 * Copyright 2019 - 2020 Anton Tananaev (anton@traccar.org)
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
package org.traccar.config;

public final class Keys {

    /**
     * Connection timeout value in seconds. Because sometimes there is no way to detect lost TCP connection old
     * connections stay in open state. On most systems there is a limit on number of open connection, so this leads to
     * problems with establishing new connections when number of devices is high or devices data connections are
     * unstable.
     */
    public static final ConfigSuffix PROTOCOL_TIMEOUT = new ConfigSuffix(
            ".timeout", Integer.class);

    /**
     * Server wide connection timeout value in seconds. See protocol timeout for more information.
     */
    public static final ConfigKey SERVER_TIMEOUT = new ConfigKey(
            "server.timeout", Integer.class);

    /**
     * Address for uploading aggregated anonymous usage statistics. Uploaded information is the same you can see on the
     * statistics screen in the web app. It does not include any sensitive (e.g. locations).
     */
    public static final ConfigKey SERVER_STATISTICS = new ConfigKey(
            "server.statistics", Boolean.class);

    /**
     * Enable events subsystem. Flag to enable all events handlers.
     */
    public static final ConfigKey EVENT_ENABLE = new ConfigKey(
            "event.enable", Boolean.class);

    /**
     * If true, the event is generated once at the beginning of overspeeding period.
     */
    public static final ConfigKey EVENT_OVERSPEED_NOT_REPEAT = new ConfigKey(
            "event.overspeed.notRepeat", Boolean.class);

    /**
     * Minimal over speed duration to trigger the event. Value in seconds.
     */
    public static final ConfigKey EVENT_OVERSPEED_MINIMAL_DURATION = new ConfigKey(
            "event.overspeed.minimalDuration", Long.class);

    /**
     * Relevant only for geofence speed limits. Use lowest speed limits from all geofences.
     */
    public static final ConfigKey EVENT_OVERSPEED_PREFER_LOWEST = new ConfigKey(
            "event.overspeed.preferLowest", Boolean.class);

    /**
     * Do not generate alert event if same alert was present in last known location.
     */
    public static final ConfigKey EVENT_IGNORE_DUPLICATE_ALERTS = new ConfigKey(
            "event.ignoreDuplicateAlerts", Boolean.class);

    /**
     * List of external handler classes to use in Netty pipeline.
     */
    public static final ConfigKey EXTRA_HANDLERS = new ConfigKey(
            "extra.handlers", String.class);

    /**
     * List of external event forwarder classes.
     */
    public static final ConfigKey EXTRA_EVENT_FORWARDERS = new ConfigKey(
            "extra.event_forwarders", String.class);

    /**
     * Enable positions forwarding to other web server.
     */
    public static final ConfigKey FORWARD_ENABLE = new ConfigKey(
            "forward.enable", Boolean.class);

    /**
     * URL to forward positions. Data is passed through URL parameters. For example, {uniqueId} for device identifier,
     * {latitude} and {longitude} for coordinates.
     */
    public static final ConfigKey FORWARD_URL = new ConfigKey(
            "forward.url", String.class);

    /**
     * Additional HTTP header, can be used for authorization.
     */
    public static final ConfigKey FORWARD_HEADER = new ConfigKey(
            "forward.header", String.class);

    /**
     * Boolean value to enable forwarding in JSON format.
     */
    public static final ConfigKey FORWARD_JSON = new ConfigKey(
            "forward.json", Boolean.class);

    /**
     * Boolean value to enable URL parameters in json mode. For example, {uniqueId} for device identifier,
     * {latitude} and {longitude} for coordinates.
     */
    public static final ConfigKey FORWARD_URL_VARIABLES = new ConfigKey(
        "forward.urlVariables", Boolean.class);

    /**
     * Position forwarding retrying enable. When enabled, additional attempts are made to deliver positions. If initial
     * delivery fails, because of an unreachable server or an HTTP response different from '2xx', the software waits
     * for 'forward.retry.delay' milliseconds to retry delivery. On subsequent failures, this delay is duplicated.
     * If forwarding is retried for 'forward.retry.count', retrying is canceled and the position is dropped. Positions
     * pending to be delivered are limited to 'forward.retry.limit'. If this limit is reached, positions get discarded.
     */
    public static final ConfigKey FORWARD_RETRY_ENABLE = new ConfigKey(
            "forward.retry.enable", Boolean.class);

    /**
     * Position forwarding retry first delay in milliseconds.
     * Can be set to anything greater than 0. Defaults to 100 milliseconds.
     */
    public static final ConfigKey FORWARD_RETRY_DELAY = new ConfigKey(
            "forward.retry.delay", Integer.class);

    /**
     * Position forwarding retry maximum retries.
     * Can be set to anything greater than 0. Defaults to 10 retries.
     */
    public static final ConfigKey FORWARD_RETRY_COUNT = new ConfigKey(
            "forward.retry.count", Integer.class);

    /**
     * Position forwarding retry pending positions limit.
     * Can be set to anything greater than 0. Defaults to 100 positions.
     */
    public static final ConfigKey FORWARD_RETRY_LIMIT = new ConfigKey(
            "forward.retry.limit", Integer.class);

    /**
     * Boolean flag to enable or disable position filtering.
     */
    public static final ConfigKey FILTER_ENABLE = new ConfigKey(
            "filter.enable", Boolean.class);

    /**
     * Filter invalid (valid field is set to false) positions.
     */
    public static final ConfigKey FILTER_INVALID = new ConfigKey(
            "filter.invalid", Boolean.class);

    /**
     * Filter zero coordinates. Zero latitude and longitude are theoretically valid values, but it practice it usually
     * indicates invalid GPS data.
     */
    public static final ConfigKey FILTER_ZERO = new ConfigKey(
            "filter.zero", Boolean.class);

    /**
     * Filter duplicate records (duplicates are detected by time value).
     */
    public static final ConfigKey FILTER_DUPLICATE = new ConfigKey(
            "filter.duplicate", Boolean.class);

    /**
     * Filter records with fix time in future. The values is specified in seconds. Records that have fix time more than
     * specified number of seconds later than current server time would be filtered out.
     */
    public static final ConfigKey FILTER_FUTURE = new ConfigKey(
            "filter.future", Long.class);

    /**
     * Filter positions with accuracy less than specified value in meters.
     */
    public static final ConfigKey FILTER_ACCURACY = new ConfigKey(
            "filter.accuracy", Integer.class);

    /**
     * Filter cell and wifi locations that are coming from geolocation provider.
     */
    public static final ConfigKey FILTER_APPROXIMATE = new ConfigKey(
            "filter.approximate", Boolean.class);

    /**
     * Filter positions with exactly zero speed values.
     */
    public static final ConfigKey FILTER_STATIC = new ConfigKey(
            "filter.static", Boolean.class);

    /**
     * Filter records by distance. The values is specified in meters. If the new position is less far than this value
     * from the last one it gets filtered out.
     */
    public static final ConfigKey FILTER_DISTANCE = new ConfigKey(
            "filter.distance", Integer.class);

    /**
     * Filter records by Maximum Speed value in knots. Can be used to filter jumps to far locations even if they're
     * marked as valid. Shouldn't be too low. Start testing with values at about 25000.
     */
    public static final ConfigKey FILTER_MAX_SPEED = new ConfigKey(
            "filter.maxSpeed", Integer.class);

    /**
     * Filter position if time from previous position is less than specified value in seconds.
     */
    public static final ConfigKey FILTER_MIN_PERIOD = new ConfigKey(
            "filter.minPeriod", Integer.class);

    /**
     * Time limit for the filtering in seconds. If the time difference between last position and a new one is more than
     * this limit, the new position will not be filtered out.
     */
    public static final ConfigKey FILTER_SKIP_LIMIT = new ConfigKey(
            "filter.skipLimit", Long.class);

    /**
     * Enable attributes skipping. Attribute skipping can be enabled in the config or device attributes.
     */
    public static final ConfigKey FILTER_SKIP_ATTRIBUTES_ENABLE = new ConfigKey(
            "filter.skipAttributes.enable", Boolean.class);

    /**
     * Override device time. Possible values are 'deviceTime' and 'serverTime'
     */
    public static final ConfigKey TIME_OVERRIDE = new ConfigKey(
            "time.override", String.class);

    /**
     * List of protocols for overriding time. If not specified override is applied globally. List consist of protocol
     * names that can be separated by comma or single space character.
     */
    public static final ConfigKey TIME_PROTOCOLS = new ConfigKey(
            "time.protocols", String.class);

    /**
     * Replaces coordinates with last known if change is less than a 'coordinates.minError' meters
     * or more than a 'coordinates.maxError' meters. Helps to avoid coordinates jumps during parking period
     * or jumps to zero coordinates.
     */
    public static final ConfigKey COORDINATES_FILTER = new ConfigKey(
            "coordinates.filter", Boolean.class);

    /**
     * Distance in meters. Distances below this value gets handled like explained in 'coordinates.filter'.
     */
    public static final ConfigKey COORDINATES_MIN_ERROR = new ConfigKey(
            "coordinates.minError", Integer.class);

    /**
     * Distance in meters. Distances above this value gets handled like explained in 'coordinates.filter', but only if
     * Position is also marked as 'invalid'.
     */
    public static final ConfigKey COORDINATES_MAX_ERROR = new ConfigKey(
            "coordinates.maxError", Integer.class);

    /**
     * Enable to save device IP addresses information. Disabled by default.
     */
    public static final ConfigKey PROCESSING_REMOTE_ADDRESS_ENABLE = new ConfigKey(
            "processing.remoteAddress.enable", Boolean.class);

    /**
     * Enable engine hours calculation on the server side. It uses ignition value to determine engine state.
     */
    public static final ConfigKey PROCESSING_ENGINE_HOURS_ENABLE = new ConfigKey(
            "processing.engineHours.enable", Boolean.class);

    /**
     * Enable copying of missing attributes from last position to the current one. Might be useful if device doesn't
     * send some values in every message.
     */
    public static final ConfigKey PROCESSING_COPY_ATTRIBUTES_ENABLE = new ConfigKey(
            "processing.copyAttributes.enable", Boolean.class);

    /**
     * Enable computed attributes processing.
     */
    public static final ConfigKey PROCESSING_COMPUTED_ATTRIBUTES_ENABLE = new ConfigKey(
            "processing.computedAttributes.enable", Boolean.class);

    /**
     * Enable computed attributes processing.
     */
    public static final ConfigKey PROCESSING_COMPUTED_ATTRIBUTES_DEVICE_ATTRIBUTES = new ConfigKey(
            "processing.computedAttributes.deviceAttributes", Boolean.class);

    /**
     * Boolean flag to enable or disable reverse geocoder.
     */
    public static final ConfigKey GEOCODER_ENABLE = new ConfigKey(
            "geocoder.enable", Boolean.class);

    /**
     * Reverse geocoder type. Check reverse geocoding documentation for more info. By default (if the value is not
     * specified) server uses Google API.
     */
    public static final ConfigKey GEOCODER_TYPE = new ConfigKey(
            "geocoder.type", String.class);

    /**
     * Geocoder server URL. Applicable only to Nominatim and Gisgraphy providers.
     */
    public static final ConfigKey GEOCODER_URL = new ConfigKey(
            "geocoder.url", String.class);

    /**
     * App id for use with Here provider.
     */
    public static final ConfigKey GEOCODER_ID = new ConfigKey(
            "geocoder.id", String.class);

    /**
     * Provider API key. Most providers require API keys.
     */
    public static final ConfigKey GEOCODER_KEY = new ConfigKey(
            "geocoder.key", String.class);

    /**
     * Language parameter for providers that support localization (e.g. Google and Nominatim).
     */
    public static final ConfigKey GEOCODER_LANGUAGE = new ConfigKey(
            "geocoder.language", String.class);

    /**
     * Address format string. Default value is %h %r, %t, %s, %c. See AddressFormat for more info.
     */
    public static final ConfigKey GEOCODER_FORMAT = new ConfigKey(
            "geocoder.format", String.class);

    /**
     * Cache size for geocoding results.
     */
    public static final ConfigKey GEOCODER_CACHE_SIZE = new ConfigKey(
            "geocoder.cacheSize", Integer.class);

    /**
     * Disable automatic reverse geocoding requests for all positions.
     */
    public static final ConfigKey GEOCODER_IGNORE_POSITIONS = new ConfigKey(
            "geocoder.ignorePositions", Boolean.class);

    /**
     * Boolean flag to apply reverse geocoding to invalid positions.
     */
    public static final ConfigKey GEOCODER_PROCESS_INVALID_POSITIONS = new ConfigKey(
            "geocoder.processInvalidPositions", Boolean.class);

    /**
     * Optional parameter to specify minimum distance for new reverse geocoding request. If distance is less than
     * specified value (in meters), then Traccar will reuse last known address.
     */
    public static final ConfigKey GEOCODER_REUSE_DISTANCE = new ConfigKey(
            "geocoder.reuseDistance", Integer.class);

    /**
     * Boolean flag to enable LBS location resolution. Some devices send cell towers information and WiFi point when GPS
     * location is not available. Traccar can determine coordinates based on that information using third party
     * services. Default value is false.
     */
    public static final ConfigKey GEOLOCATION_ENABLE = new ConfigKey(
            "geolocation.enable", Boolean.class);

    /**
     * Provider to use for LBS location. Available options: google, mozilla and opencellid. By default opencellid is
     * used. You have to supply a key that you get from corresponding provider. For more information see LBS geolocation
     * documentation.
     */
    public static final ConfigKey GEOLOCATION_TYPE = new ConfigKey(
            "geolocation.type", String.class);

    /**
     * Geolocation provider API URL address. Not required for most providers.
     */
    public static final ConfigKey GEOLOCATION_URL = new ConfigKey(
            "geolocation.url", String.class);

    /**
     * Provider API key. OpenCellID service requires API key.
     */
    public static final ConfigKey GEOLOCATION_KEY = new ConfigKey(
            "geolocation.key", String.class);

    /**
     * Boolean flag to apply geolocation to invalid positions.
     */
    public static final ConfigKey GEOLOCATION_PROCESS_INVALID_POSITIONS = new ConfigKey(
            "geolocation.processInvalidPositions", Boolean.class);

    /**
     * Boolean flag to enable speed limit API to get speed limit values depending on location. Default value is false.
     */
    public static final ConfigKey SPEED_LIMIT_ENABLE = new ConfigKey(
            "speedLimit.enable", Boolean.class);

    /**
     * Provider to use for speed limit. Available options: overpass. By default overpass is used.
     */
    public static final ConfigKey SPEED_LIMIT_TYPE = new ConfigKey(
            "speedLimit.type", String.class);

    /**
     * Speed limit provider API URL address.
     */
    public static final ConfigKey SPEED_LIMIT_URL = new ConfigKey(
            "speedLimit.url", String.class);

    /**
     * Override latitude sign / hemisphere. Useful in cases where value is incorrect because of device bug. Value can be
     * N for North or S for South.
     */
    public static final ConfigKey LOCATION_LATITUDE_HEMISPHERE = new ConfigKey(
            "location.latitudeHemisphere", Boolean.class);

    /**
     * Override longitude sign / hemisphere. Useful in cases where value is incorrect because of device bug. Value can
     * be E for East or W for West.
     */
    public static final ConfigKey LOCATION_LONGITUDE_HEMISPHERE = new ConfigKey(
            "location.longitudeHemisphere", Boolean.class);

    /**
     * Enable Jetty Request Log.
     */
    public static final ConfigKey WEB_REQUEST_LOG_ENABLE = new ConfigKey(
            "web.requestLog.enable", Boolean.class);

    /**
     * Jetty Request Log Path.
     * The path must include the string "yyyy_mm_dd", which is replaced with the actual date when creating and rolling
     * over the file.
     * Example: ./logs/jetty-yyyy_mm_dd.request.log
     */
    public static final ConfigKey WEB_REQUEST_LOG_PATH = new ConfigKey(
            "web.requestLog.path", String.class);

    /**
     * Set the number of days before rotated request log files are deleted.
     */
    public static final ConfigKey WEB_REQUEST_LOG_RETAIN_DAYS = new ConfigKey(
            "web.requestLog.retainDays", Integer.class);

    /**
     * Disable systemd health checks.
     */
    public static final ConfigKey WEB_DISABLE_HEALTH_CHECK = new ConfigKey(
            "web.disableHealthCheck", Boolean.class);

    /**
     * Sets SameSite cookie attribute value.
     * Supported options: Lax, Strict, None.
     */
    public static final ConfigKey WEB_SAME_SITE_COOKIE = new ConfigKey(
            "web.sameSiteCookie", String.class);

    private Keys() {
    }

}
