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

import java.util.Collections;

public final class Keys {

    private Keys() {
    }

    /**
     * Network interface for a the protocol. If not specified, server will bind all interfaces.
     */
    public static final ConfigSuffix<String> PROTOCOL_ADDRESS = new ConfigSuffix<>(
            ".address",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Port number for the protocol. Most protocols use TCP on the transport layer. Some protocols use UDP. Some
     * support both TCP and UDP.
     */
    public static final ConfigSuffix<Integer> PROTOCOL_PORT = new ConfigSuffix<>(
            ".port",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Connection timeout value in seconds. Because sometimes there is no way to detect lost TCP connection old
     * connections stay in open state. On most systems there is a limit on number of open connection, so this leads to
     * problems with establishing new connections when number of devices is high or devices data connections are
     * unstable.
     */
    public static final ConfigSuffix<Integer> PROTOCOL_TIMEOUT = new ConfigSuffix<>(
            ".timeout",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Device password. Commonly used in some protocol for sending commands.
     */
    public static final ConfigSuffix<String> PROTOCOL_DEVICE_PASSWORD = new ConfigSuffix<>(
            ".devicePassword",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Default protocol mask to use. Currently used only by Skypatrol protocol.
     */
    public static final ConfigSuffix<Integer> PROTOCOL_MASK = new ConfigSuffix<>(
            ".mask",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Custom message length. Currently used only by H2 protocol for specifying binary message length.
     */
    public static final ConfigSuffix<Integer> PROTOCOL_MESSAGE_LENGTH = new ConfigSuffix<>(
            ".messageLength",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable extended functionality for the protocol. The reason it's disabled by default is that not all devices
     * support it.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_EXTENDED = new ConfigSuffix<>(
            ".extended",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Decode string as UTF8 instead of ASCII. Only applicable for some protocols.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_UTF8 = new ConfigSuffix<>(
            ".utf8",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable CAN decoding for the protocol. Similar to 'extended' configuration, it's not supported for some devices.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_CAN = new ConfigSuffix<>(
            ".can",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Indicates whether server acknowledgement is required. Only applicable for some protocols.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_ACK = new ConfigSuffix<>(
            ".ack",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Ignore device reported fix time. Useful in case some devices report invalid time. Currently only available for
     * GL200 protocol.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_IGNORE_FIX_TIME = new ConfigSuffix<>(
            ".ignoreFixTime",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Decode additional TK103 attributes. Not supported for some devices.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_DECODE_LOW = new ConfigSuffix<>(
            ".decodeLow",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Use long date format for Atrack protocol.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_LONG_DATE = new ConfigSuffix<>(
            ".longDate",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Use decimal fuel value format for Atrack protocol.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_DECIMAL_FUEL = new ConfigSuffix<>(
            ".decimalFuel",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Indicates additional custom attributes for Atrack protocol.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_CUSTOM = new ConfigSuffix<>(
            ".custom",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Custom format string for Atrack protocol.
     */
    public static final ConfigSuffix<String> PROTOCOL_FORM = new ConfigSuffix<>(
            ".form",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Protocol configuration. Required for some devices for decoding incoming data.
     */
    public static final ConfigSuffix<String> PROTOCOL_CONFIG = new ConfigSuffix<>(
            ".config",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Alarm mapping for Atrack protocol.
     */
    public static final ConfigSuffix<String> PROTOCOL_ALARM_MAP = new ConfigSuffix<>(
            ".alarmMap",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Some devices require server address confirmation. Use this parameter to configure correct public address.
     */
    public static final ConfigSuffix<String> PROTOCOL_SERVER = new ConfigSuffix<>(
            ".server",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Skip device connection session cache. Per protocol configuration.
     */
    public static final ConfigSuffix<Boolean> PROTOCOL_IGNORE_SESSIONS_CACHE = new ConfigSuffix<>(
            ".ignoreSessionCache",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Skip device connection session cache. Global configuration.
     */
    public static final ConfigKey<Boolean> DECODER_IGNORE_SESSIONS_CACHE = new ConfigKey<>(
            "decoder.ignoreSessionCache",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Server wide connection timeout value in seconds. See protocol timeout for more information.
     */
    public static final ConfigKey<Integer> SERVER_TIMEOUT = new ConfigKey<>(
            "server.timeout",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Address for uploading aggregated anonymous usage statistics. Uploaded information is the same you can see on the
     * statistics screen in the web app. It does not include any sensitive (e.g. locations).
     */
    public static final ConfigKey<String> SERVER_STATISTICS = new ConfigKey<>(
            "server.statistics",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * If true, the event is generated once at the beginning of overspeeding period.
     */
    public static final ConfigKey<Boolean> EVENT_OVERSPEED_NOT_REPEAT = new ConfigKey<>(
            "event.overspeed.notRepeat",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Minimal over speed duration to trigger the event. Value in seconds.
     */
    public static final ConfigKey<Long> EVENT_OVERSPEED_MINIMAL_DURATION = new ConfigKey<>(
            "event.overspeed.minimalDuration",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Relevant only for geofence speed limits. Use lowest speed limits from all geofences.
     */
    public static final ConfigKey<Boolean> EVENT_OVERSPEED_PREFER_LOWEST = new ConfigKey<>(
            "event.overspeed.preferLowest",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Do not generate alert event if same alert was present in last known location.
     */
    public static final ConfigKey<Boolean> EVENT_IGNORE_DUPLICATE_ALERTS = new ConfigKey<>(
            "event.ignoreDuplicateAlerts",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * If set to true, invalid positions will be considered for motion logic.
     */
    public static final ConfigKey<Boolean> EVENT_MOTION_PROCESS_INVALID_POSITIONS = new ConfigKey<>(
            "event.motion.processInvalidPositions",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * If the speed is above specified value, the object is considered to be in motion. Default value is 0.01 knots.
     */
    public static final ConfigKey<Double> EVENT_MOTION_SPEED_THRESHOLD = new ConfigKey<>(
            "event.motion.speedThreshold",
            Collections.singletonList(KeyType.GLOBAL),
            0.01);

    /**
     * Global polyline geofence distance. Within that distance from the polyline, point is considered within the
     * geofence. Each individual geofence can also has 'polylineDistance' attribute which will take precedence.
     */
    public static final ConfigKey<Double> GEOFENCE_POLYLINE_DISTANCE = new ConfigKey<>(
            "geofence.polylineDistance",
            Collections.singletonList(KeyType.GLOBAL),
            25.0);

    /**
     * Path to the database driver JAR file. Traccar includes drivers for MySQL, PostgreSQL and H2 databases. If you use
     * one of those, you don't need to specify this parameter.
     */
    public static final ConfigKey<String> DATABASE_DRIVER_FILE = new ConfigKey<>(
            "database.driverFile",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Database driver Java class. For H2 use 'org.h2.Driver'. MySQL driver class name is 'com.mysql.jdbc.Driver'.
     */
    public static final ConfigKey<String> DATABASE_DRIVER = new ConfigKey<>(
            "database.driver",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Database connection URL. By default Traccar uses H2 database.
     */
    public static final ConfigKey<String> DATABASE_URL = new ConfigKey<>(
            "database.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Database user name. Default administrator user for H2 database is 'sa'.
     */
    public static final ConfigKey<String> DATABASE_USER = new ConfigKey<>(
            "database.user",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Database user password. Default password for H2 admin (sa) user is empty.
     */
    public static final ConfigKey<String> DATABASE_PASSWORD = new ConfigKey<>(
            "database.password",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Path to Liquibase master changelog file.
     */
    public static final ConfigKey<String> DATABASE_CHANGELOG = new ConfigKey<>(
            "database.changelog",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Automatically generate SQL database queries when possible.
     */
    public static final ConfigKey<Boolean> DATABASE_GENERATE_QUERIES = new ConfigKey<>(
            "database.generateQueries",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Database connection pool size. Default value is defined by the HikariCP library.
     */
    public static final ConfigKey<Integer> DATABASE_MAX_POOL_SIZE = new ConfigKey<>(
            "database.maxPoolSize",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * SQL query to check connection status. Default value is 'SELECT 1'. For Oracle database you can use
     * 'SELECT 1 FROM DUAL'.
     */
    public static final ConfigKey<String> DATABASE_CHECK_CONNECTION = new ConfigKey<>(
            "database.checkConnection",
            Collections.singletonList(KeyType.GLOBAL),
            "SELECT 1");

    /**
     * Store original HEX or string data as "raw" attribute in the corresponding position.
     */
    public static final ConfigKey<Boolean> DATABASE_SAVE_ORIGINAL = new ConfigKey<>(
            "database.saveOriginal",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * By default server syncs with the database if it encounters and unknown device. This flag allows to disable that
     * behavior to improve performance in some cases.
     */
    public static final ConfigKey<Boolean> DATABASE_IGNORE_UNKNOWN = new ConfigKey<>(
            "database.ignoreUnknown",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Automatically register unknown devices in the database.
     */
    public static final ConfigKey<Boolean> DATABASE_REGISTER_UNKNOWN = new ConfigKey<>(
            "database.registerUnknown",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Default category for auto-registered devices.
     */
    public static final ConfigKey<String> DATABASE_REGISTER_UNKNOWN_DEFAULT_CATEGORY = new ConfigKey<>(
            "database.registerUnknown.defaultCategory",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * The group id assigned to auto-registered devices.
     */
    public static final ConfigKey<Long> DATABASE_REGISTER_UNKNOWN_DEFAULT_GROUP_ID = new ConfigKey<>(
            "database.registerUnknown.defaultGroupId",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Minimum device refresh timeout in seconds. Default timeout is 5 minutes.
     */
    public static final ConfigKey<Long> DATABASE_REFRESH_DELAY = new ConfigKey<>(
            "database.refreshDelay",
            Collections.singletonList(KeyType.GLOBAL),
            300L);

    /**
     * Store empty messages as positions. For example, heartbeats.
     */
    public static final ConfigKey<Boolean> DATABASE_SAVE_EMPTY = new ConfigKey<>(
            "database.saveEmpty",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Device limit for self registered users. Default value is -1, which indicates no limit.
     */
    public static final ConfigKey<Integer> USERS_DEFAULT_DEVICE_LIMIT = new ConfigKey<>(
            "users.defaultDeviceLimit",
            Collections.singletonList(KeyType.GLOBAL),
            -1);

    /**
     * Default user expiration for self registered users. Value is in days. By default no expiration is set.
     */
    public static final ConfigKey<Integer> USERS_DEFAULT_EXPIRATION_DAYS = new ConfigKey<>(
            "users.defaultExpirationDays",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP server URL.
     */
    public static final ConfigKey<String> LDAP_URL = new ConfigKey<>(
            "ldap.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP server login.
     */
    public static final ConfigKey<String> LDAP_USER = new ConfigKey<>(
            "ldap.user",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP server password.
     */
    public static final ConfigKey<String> LDAP_PASSWORD = new ConfigKey<>(
            "ldap.password",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Force LDAP authentication.
     */
    public static final ConfigKey<Boolean> LDAP_FORCE = new ConfigKey<>(
            "ldap.force",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP user search base.
     */
    public static final ConfigKey<String> LDAP_BASE = new ConfigKey<>(
            "ldap.base",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP attribute used as user id. Default value is 'uid'.
     */
    public static final ConfigKey<String> LDAP_ID_ATTRIBUTE = new ConfigKey<>(
            "ldap.idAttribute",
            Collections.singletonList(KeyType.GLOBAL),
            "uid");

    /**
     * LDAP attribute used as user name. Default value is 'cn'.
     */
    public static final ConfigKey<String> LDAP_NAME_ATTRIBUTE = new ConfigKey<>(
            "ldap.nameAttribute",
            Collections.singletonList(KeyType.GLOBAL),
            "cn");

    /**
     * LDAP attribute used as user email. Default value is 'mail'.
     */
    public static final ConfigKey<String> LDAP_MAIN_ATTRIBUTE = new ConfigKey<>(
            "ldap.mailAttribute",
            Collections.singletonList(KeyType.GLOBAL),
            "mail");

    /**
     * LDAP custom search filter. If not specified, '({idAttribute}=:login)' will be used as a filter.
     */
    public static final ConfigKey<String> LDAP_SEARCH_FILTER = new ConfigKey<>(
            "ldap.searchFilter",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP custom admin search filter.
     */
    public static final ConfigKey<String> LDAP_ADMIN_FILTER = new ConfigKey<>(
            "ldap.adminFilter",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * LDAP admin user group. Used if custom admin filter is not specified.
     */
    public static final ConfigKey<String> LDAP_ADMIN_GROUP = new ConfigKey<>(
            "ldap.adminGroup",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * If no data is reported by a device for the given amount of time, status changes from online to unknown. Value is
     * in seconds. Default timeout is 10 minutes.
     */
    public static final ConfigKey<Long> STATUS_TIMEOUT = new ConfigKey<>(
            "status.timeout",
            Collections.singletonList(KeyType.GLOBAL),
            600L);

    /**
     * Force additional state check when device status changes to 'offline' or 'unknown'. Default false.
     */
    public static final ConfigKey<Boolean> STATUS_UPDATE_DEVICE_STATE = new ConfigKey<>(
            "status.updateDeviceState",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * List of protocol names to ignore offline status. Can be useful to not trigger status change when devices are
     * configured to disconnect after reporting a batch of data.
     */
    public static final ConfigKey<String> STATUS_IGNORE_OFFLINE = new ConfigKey<>(
            "status.ignoreOffline",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Path to the media folder. Server stores audio, video and photo files in that folder. Sub-folders will be
     * automatically created for each device by unique id.
     */
    public static final ConfigKey<String> MEDIA_PATH = new ConfigKey<>(
            "media.path",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Optional parameter to specify network interface for web interface to bind to. By default server will bind to all
     * available interfaces.
     */
    public static final ConfigKey<String> WEB_ADDRESS = new ConfigKey<>(
            "web.address",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Web interface TCP port number. By default Traccar uses port 8082. To avoid specifying port in the browser you
     * can set it to 80 (default HTTP port).
     */
    public static final ConfigKey<Integer> WEB_PORT = new ConfigKey<>(
            "web.port",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Path to the web app folder.
     */
    public static final ConfigKey<String> WEB_PATH = new ConfigKey<>(
            "web.path",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * WebSocket connection timeout in milliseconds. Default timeout is 10 minutes.
     */
    public static final ConfigKey<Long> WEB_TIMEOUT = new ConfigKey<>(
            "web.timeout",
            Collections.singletonList(KeyType.GLOBAL),
            60000L);

    /**
     * Authentication sessions timeout in seconds. By default no timeout.
     */
    public static final ConfigKey<Integer> WEB_SESSION_TIMEOUT = new ConfigKey<>(
            "web.sessionTimeout",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable database access console via '/console' URL. Use only for debugging. Never use in production.
     */
    public static final ConfigKey<Boolean> WEB_CONSOLE = new ConfigKey<>(
            "web.console",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Server debug version of the web app. Not recommended to use for performance reasons. It is intended to be used
     * for development and debugging purposes.
     */
    public static final ConfigKey<Boolean> WEB_DEBUG = new ConfigKey<>(
            "web.debug",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Cross-origin resource sharing origin header value.
     */
    public static final ConfigKey<String> WEB_ORIGIN = new ConfigKey<>(
            "web.origin",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Cache control header value. By default resources are cached for one hour.
     */
    public static final ConfigKey<String> WEB_CACHE_CONTROL = new ConfigKey<>(
            "web.cacheControl",
            Collections.singletonList(KeyType.GLOBAL),
            "max-age=3600,public");

    /**
     * URL to forward positions. Data is passed through URL parameters. For example, {uniqueId} for device identifier,
     * {latitude} and {longitude} for coordinates.
     */
    public static final ConfigKey<String> FORWARD_URL = new ConfigKey<>(
            "forward.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Additional HTTP header, can be used for authorization.
     */
    public static final ConfigKey<String> FORWARD_HEADER = new ConfigKey<>(
            "forward.header",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean value to enable forwarding in JSON format.
     */
    public static final ConfigKey<Boolean> FORWARD_JSON = new ConfigKey<>(
            "forward.json",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean value to enable URL parameters in json mode. For example, {uniqueId} for device identifier,
     * {latitude} and {longitude} for coordinates.
     */
    public static final ConfigKey<Boolean> FORWARD_URL_VARIABLES = new ConfigKey<>(
            "forward.urlVariables",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Position forwarding retrying enable. When enabled, additional attempts are made to deliver positions. If initial
     * delivery fails, because of an unreachable server or an HTTP response different from '2xx', the software waits
     * for 'forward.retry.delay' milliseconds to retry delivery. On subsequent failures, this delay is duplicated.
     * If forwarding is retried for 'forward.retry.count', retrying is canceled and the position is dropped. Positions
     * pending to be delivered are limited to 'forward.retry.limit'. If this limit is reached, positions get discarded.
     */
    public static final ConfigKey<Boolean> FORWARD_RETRY_ENABLE = new ConfigKey<>(
            "forward.retry.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Position forwarding retry first delay in milliseconds.
     * Can be set to anything greater than 0. Defaults to 100 milliseconds.
     */
    public static final ConfigKey<Integer> FORWARD_RETRY_DELAY = new ConfigKey<>(
            "forward.retry.delay",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Position forwarding retry maximum retries.
     * Can be set to anything greater than 0. Defaults to 10 retries.
     */
    public static final ConfigKey<Integer> FORWARD_RETRY_COUNT = new ConfigKey<>(
            "forward.retry.count",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Position forwarding retry pending positions limit.
     * Can be set to anything greater than 0. Defaults to 100 positions.
     */
    public static final ConfigKey<Integer> FORWARD_RETRY_LIMIT = new ConfigKey<>(
            "forward.retry.limit",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Events forwarding URL.
     */
    public static final ConfigKey<String> EVENT_FORWARD_URL = new ConfigKey<>(
            "event.forward.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Events forwarding headers. Example value:
     * FirstHeader: hello
     * SecondHeader: world
     */
    public static final ConfigKey<String> EVENT_FORWARD_HEADERS = new ConfigKey<>(
            "event.forward.header",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable commands queuing when devices are offline. Commands are buffered in memory only, so restarting service
     * will clear the buffer.
     */
    public static final ConfigKey<Boolean> COMMANDS_QUEUEING = new ConfigKey<>(
            "commands.queueing",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * SMS API service full URL. Enables SMS commands and notifications.
     */
    public static final ConfigKey<String> SMS_HTTP_URL = new ConfigKey<>(
            "sms.http.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * SMS API authorization header name. Default value is 'Authorization'.
     */
    public static final ConfigKey<String> SMS_HTTP_AUTHORIZATION_HEADER = new ConfigKey<>(
            "sms.http.authorizationHeader",
            Collections.singletonList(KeyType.GLOBAL),
            "Authorization");

    /**
     * SMS API authorization header value. This value takes precedence over user and password.
     */
    public static final ConfigKey<String> SMS_HTTP_AUTHORIZATION = new ConfigKey<>(
            "sms.http.authorization",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * SMS API basic authentication user.
     */
    public static final ConfigKey<String> SMS_HTTP_USER = new ConfigKey<>(
            "sms.http.user",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * SMS API basic authentication password.
     */
    public static final ConfigKey<String> SMS_HTTP_PASSWORD = new ConfigKey<>(
            "sms.http.password",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * SMS API body template. Placeholders {phone} and {message} can be used in the template.
     * If value starts with '{' or '[', server automatically assumes JSON format.
     */
    public static final ConfigKey<String> SMS_HTTP_TEMPLATE = new ConfigKey<>(
            "sms.http.template",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Traccar notification API key.
     */
    public static final ConfigKey<String> NOTIFICATOR_TRACCAR_KEY = new ConfigKey<>(
            "notificator.traccar.key",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Firebase server API key for push notifications.
     */
    public static final ConfigKey<String> NOTIFICATOR_FIREBASE_KEY = new ConfigKey<>(
            "notificator.firebase.key",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Pushover notification user name.
     */
    public static final ConfigKey<String> NOTIFICATOR_PUSHOVER_USER = new ConfigKey<>(
            "notificator.pushover.user",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Pushover notification user token.
     */
    public static final ConfigKey<String> NOTIFICATOR_PUSHOVER_TOKEN = new ConfigKey<>(
            "notificator.pushover.token",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Telegram notification API key.
     */
    public static final ConfigKey<String> NOTIFICATOR_TELEGRAM_KEY = new ConfigKey<>(
            "notificator.telegram.key",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Telegram notification chat id to post messages to.
     */
    public static final ConfigKey<String> NOTIFICATOR_TELEGRAM_CHAT_ID = new ConfigKey<>(
            "notificator.telegram.chatId",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Maximum time period for reports in seconds. Can be useful to prevent users to request unreasonably long reports.
     * By default there is no limit.
     */
    public static final ConfigKey<Long> REPORT_PERIOD_LIMIT = new ConfigKey<>(
            "report.periodLimit",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Trips less than minimal duration and minimal distance are ignored. 300 seconds and 500 meters are default.
     */
    public static final ConfigKey<Long> REPORT_TRIP_MINIMAL_TRIP_DISTANCE = new ConfigKey<>(
            "report.trip.minimalTripDistance",
            Collections.singletonList(KeyType.GLOBAL),
            500L);

    /**
     * Trips less than minimal duration and minimal distance are ignored. 300 seconds and 500 meters are default.
     */
    public static final ConfigKey<Long> REPORT_TRIP_MINIMAL_TRIP_DURATION = new ConfigKey<>(
            "report.trip.minimalTripDuration",
            Collections.singletonList(KeyType.GLOBAL),
            300L);

    /**
     * Parking less than minimal duration does not cut trip. Default 300 seconds.
     */
    public static final ConfigKey<Long> REPORT_TRIP_MINIMAL_PARKING_DURATION = new ConfigKey<>(
            "report.trip.minimalParkingDuration",
            Collections.singletonList(KeyType.GLOBAL),
            300L);

    /**
     * Gaps of more than specified time are counted as stops. Default value is one hour.
     */
    public static final ConfigKey<Long> REPORT_TRIP_MINIMAL_NO_DATA_DURATION = new ConfigKey<>(
            "report.trip.minimalNoDataDuration",
            Collections.singletonList(KeyType.GLOBAL),
            3600L);

    /**
     * Flag to enable ignition use for trips calculation.
     */
    public static final ConfigKey<Boolean> REPORT_TRIP_USE_IGNITION = new ConfigKey<>(
            "report.trip.useIgnition",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean flag to enable or disable position filtering.
     */
    public static final ConfigKey<Boolean> FILTER_ENABLE = new ConfigKey<>(
            "filter.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter invalid (valid field is set to false) positions.
     */
    public static final ConfigKey<Boolean> FILTER_INVALID = new ConfigKey<>(
            "filter.invalid",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter zero coordinates. Zero latitude and longitude are theoretically valid values, but it practice it usually
     * indicates invalid GPS data.
     */
    public static final ConfigKey<Boolean> FILTER_ZERO = new ConfigKey<>(
            "filter.zero",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter duplicate records (duplicates are detected by time value).
     */
    public static final ConfigKey<Boolean> FILTER_DUPLICATE = new ConfigKey<>(
            "filter.duplicate",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter records with fix time in future. The values is specified in seconds. Records that have fix time more than
     * specified number of seconds later than current server time would be filtered out.
     */
    public static final ConfigKey<Long> FILTER_FUTURE = new ConfigKey<>(
            "filter.future",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter positions with accuracy less than specified value in meters.
     */
    public static final ConfigKey<Integer> FILTER_ACCURACY = new ConfigKey<>(
            "filter.accuracy",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter cell and wifi locations that are coming from geolocation provider.
     */
    public static final ConfigKey<Boolean> FILTER_APPROXIMATE = new ConfigKey<>(
            "filter.approximate",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter positions with exactly zero speed values.
     */
    public static final ConfigKey<Boolean> FILTER_STATIC = new ConfigKey<>(
            "filter.static",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter records by distance. The values is specified in meters. If the new position is less far than this value
     * from the last one it gets filtered out.
     */
    public static final ConfigKey<Integer> FILTER_DISTANCE = new ConfigKey<>(
            "filter.distance",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter records by Maximum Speed value in knots. Can be used to filter jumps to far locations even if they're
     * marked as valid. Shouldn't be too low. Start testing with values at about 25000.
     */
    public static final ConfigKey<Integer> FILTER_MAX_SPEED = new ConfigKey<>(
            "filter.maxSpeed",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Filter position if time from previous position is less than specified value in seconds.
     */
    public static final ConfigKey<Integer> FILTER_MIN_PERIOD = new ConfigKey<>(
            "filter.minPeriod",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Time limit for the filtering in seconds. If the time difference between last position and a new one is more than
     * this limit, the new position will not be filtered out.
     */
    public static final ConfigKey<Long> FILTER_SKIP_LIMIT = new ConfigKey<>(
            "filter.skipLimit",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable attributes skipping. Attribute skipping can be enabled in the config or device attributes.
     */
    public static final ConfigKey<Boolean> FILTER_SKIP_ATTRIBUTES_ENABLE = new ConfigKey<>(
            "filter.skipAttributes.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Override device time. Possible values are 'deviceTime' and 'serverTime'
     */
    public static final ConfigKey<String> TIME_OVERRIDE = new ConfigKey<>(
            "time.override",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * List of protocols for overriding time. If not specified override is applied globally. List consist of protocol
     * names that can be separated by comma or single space character.
     */
    public static final ConfigKey<String> TIME_PROTOCOLS = new ConfigKey<>(
            "time.protocols",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Replaces coordinates with last known if change is less than a 'coordinates.minError' meters
     * or more than a 'coordinates.maxError' meters. Helps to avoid coordinates jumps during parking period
     * or jumps to zero coordinates.
     */
    public static final ConfigKey<Boolean> COORDINATES_FILTER = new ConfigKey<>(
            "coordinates.filter",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Distance in meters. Distances below this value gets handled like explained in 'coordinates.filter'.
     */
    public static final ConfigKey<Integer> COORDINATES_MIN_ERROR = new ConfigKey<>(
            "coordinates.minError",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Distance in meters. Distances above this value gets handled like explained in 'coordinates.filter', but only if
     * Position is also marked as 'invalid'.
     */
    public static final ConfigKey<Integer> COORDINATES_MAX_ERROR = new ConfigKey<>(
            "coordinates.maxError",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable to save device IP addresses information. Disabled by default.
     */
    public static final ConfigKey<Boolean> PROCESSING_REMOTE_ADDRESS_ENABLE = new ConfigKey<>(
            "processing.remoteAddress.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable engine hours calculation on the server side. It uses ignition value to determine engine state.
     */
    public static final ConfigKey<Boolean> PROCESSING_ENGINE_HOURS_ENABLE = new ConfigKey<>(
            "processing.engineHours.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable copying of missing attributes from last position to the current one. Might be useful if device doesn't
     * send some values in every message.
     */
    public static final ConfigKey<Boolean> PROCESSING_COPY_ATTRIBUTES_ENABLE = new ConfigKey<>(
            "processing.copyAttributes.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable computed attributes processing.
     */
    public static final ConfigKey<Boolean> PROCESSING_COMPUTED_ATTRIBUTES_ENABLE = new ConfigKey<>(
            "processing.computedAttributes.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable computed attributes processing.
     */
    public static final ConfigKey<Boolean> PROCESSING_COMPUTED_ATTRIBUTES_DEVICE_ATTRIBUTES = new ConfigKey<>(
            "processing.computedAttributes.deviceAttributes",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean flag to enable or disable reverse geocoder.
     */
    public static final ConfigKey<Boolean> GEOCODER_ENABLE = new ConfigKey<>(
            "geocoder.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Reverse geocoder type. Check reverse geocoding documentation for more info. By default (if the value is not
     * specified) server uses Google API.
     */
    public static final ConfigKey<String> GEOCODER_TYPE = new ConfigKey<>(
            "geocoder.type",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Geocoder server URL. Applicable only to Nominatim and Gisgraphy providers.
     */
    public static final ConfigKey<String> GEOCODER_URL = new ConfigKey<>(
            "geocoder.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * App id for use with Here provider.
     */
    public static final ConfigKey<String> GEOCODER_ID = new ConfigKey<>(
            "geocoder.id",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Provider API key. Most providers require API keys.
     */
    public static final ConfigKey<String> GEOCODER_KEY = new ConfigKey<>(
            "geocoder.key",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Language parameter for providers that support localization (e.g. Google and Nominatim).
     */
    public static final ConfigKey<String> GEOCODER_LANGUAGE = new ConfigKey<>(
            "geocoder.language",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Address format string. Default value is %h %r, %t, %s, %c. See AddressFormat for more info.
     */
    public static final ConfigKey<String> GEOCODER_FORMAT = new ConfigKey<>(
            "geocoder.format",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Cache size for geocoding results.
     */
    public static final ConfigKey<Integer> GEOCODER_CACHE_SIZE = new ConfigKey<>(
            "geocoder.cacheSize",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Disable automatic reverse geocoding requests for all positions.
     */
    public static final ConfigKey<Boolean> GEOCODER_IGNORE_POSITIONS = new ConfigKey<>(
            "geocoder.ignorePositions",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean flag to apply reverse geocoding to invalid positions.
     */
    public static final ConfigKey<Boolean> GEOCODER_PROCESS_INVALID_POSITIONS = new ConfigKey<>(
            "geocoder.processInvalidPositions",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Optional parameter to specify minimum distance for new reverse geocoding request. If distance is less than
     * specified value (in meters), then Traccar will reuse last known address.
     */
    public static final ConfigKey<Integer> GEOCODER_REUSE_DISTANCE = new ConfigKey<>(
            "geocoder.reuseDistance",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Perform geocoding when preparing reports and sending notifications.
     */
    public static final ConfigKey<Boolean> GEOCODER_ON_REQUEST = new ConfigKey<>(
            "geocoder.onRequest",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean flag to enable LBS location resolution. Some devices send cell towers information and WiFi point when GPS
     * location is not available. Traccar can determine coordinates based on that information using third party
     * services. Default value is false.
     */
    public static final ConfigKey<Boolean> GEOLOCATION_ENABLE = new ConfigKey<>(
            "geolocation.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Provider to use for LBS location. Available options: google, mozilla and opencellid. By default opencellid is
     * used. You have to supply a key that you get from corresponding provider. For more information see LBS geolocation
     * documentation.
     */
    public static final ConfigKey<String> GEOLOCATION_TYPE = new ConfigKey<>(
            "geolocation.type",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Geolocation provider API URL address. Not required for most providers.
     */
    public static final ConfigKey<String> GEOLOCATION_URL = new ConfigKey<>(
            "geolocation.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Provider API key. OpenCellID service requires API key.
     */
    public static final ConfigKey<String> GEOLOCATION_KEY = new ConfigKey<>(
            "geolocation.key",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean flag to apply geolocation to invalid positions.
     */
    public static final ConfigKey<Boolean> GEOLOCATION_PROCESS_INVALID_POSITIONS = new ConfigKey<>(
            "geolocation.processInvalidPositions",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Default MCC value to use if device doesn't report MCC.
     */
    public static final ConfigKey<Integer> GEOLOCATION_MCC = new ConfigKey<>(
            "geolocation.mcc",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Default MNC value to use if device doesn't report MNC.
     */
    public static final ConfigKey<Integer> GEOLOCATION_MNC = new ConfigKey<>(
            "geolocation.mnc",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Boolean flag to enable speed limit API to get speed limit values depending on location. Default value is false.
     */
    public static final ConfigKey<Boolean> SPEED_LIMIT_ENABLE = new ConfigKey<>(
            "speedLimit.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Provider to use for speed limit. Available options: overpass. By default overpass is used.
     */
    public static final ConfigKey<String> SPEED_LIMIT_TYPE = new ConfigKey<>(
            "speedLimit.type",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Speed limit provider API URL address.
     */
    public static final ConfigKey<String> SPEED_LIMIT_URL = new ConfigKey<>(
            "speedLimit.url",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Override latitude sign / hemisphere. Useful in cases where value is incorrect because of device bug. Value can be
     * N for North or S for South.
     */
    public static final ConfigKey<String> LOCATION_LATITUDE_HEMISPHERE = new ConfigKey<>(
            "location.latitudeHemisphere",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Override longitude sign / hemisphere. Useful in cases where value is incorrect because of device bug. Value can
     * be E for East or W for West.
     */
    public static final ConfigKey<String> LOCATION_LONGITUDE_HEMISPHERE = new ConfigKey<>(
            "location.longitudeHemisphere",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable Jetty Request Log.
     */
    public static final ConfigKey<Boolean> WEB_REQUEST_LOG_ENABLE = new ConfigKey<>(
            "web.requestLog.enable",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Jetty Request Log Path.
     * The path must include the string "yyyy_mm_dd", which is replaced with the actual date when creating and rolling
     * over the file.
     * Example: ./logs/jetty-yyyy_mm_dd.request.log
     */
    public static final ConfigKey<String> WEB_REQUEST_LOG_PATH = new ConfigKey<>(
            "web.requestLog.path",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Set the number of days before rotated request log files are deleted.
     */
    public static final ConfigKey<Integer> WEB_REQUEST_LOG_RETAIN_DAYS = new ConfigKey<>(
            "web.requestLog.retainDays",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Disable systemd health checks.
     */
    public static final ConfigKey<Boolean> WEB_DISABLE_HEALTH_CHECK = new ConfigKey<>(
            "web.disableHealthCheck",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Enable TOTP.
     */
    public static final ConfigKey TOTP_ENABLED = new ConfigKey(
            "totp.enabled", Boolean.class);

    /**
     * Enforce TOTP. When false local authentication still works without TOTP when true user always need to use TOTP.
     */
    public static final ConfigKey TOTP_ENFORCE = new ConfigKey(
            "totp.enforce", Boolean.class);

    /** Sets SameSite cookie attribute value.
     * Supported options: Lax, Strict, None.
     */
    public static final ConfigKey<String> WEB_SAME_SITE_COOKIE = new ConfigKey<>(
            "web.sameSiteCookie",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Output logging to the standard terminal output instead of a log file.
     */
    public static final ConfigKey<Boolean> LOGGER_CONSOLE = new ConfigKey<>(
            "logger.console",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Log file name. For rotating logs, a date is added at the end of the file name for non-current logs.
     */
    public static final ConfigKey<String> LOGGER_FILE = new ConfigKey<>(
            "logger.file",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Logging level. Default value is 'info'.
     * Available options: off, severe, warning, info, config, fine, finer, finest, all.
     */
    public static final ConfigKey<String> LOGGER_LEVEL = new ConfigKey<>(
            "logger.level",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Print full exception traces. Useful for debugging. By default shortened traces are logged.
     */
    public static final ConfigKey<Boolean> LOGGER_FULL_STACK_TRACES = new ConfigKey<>(
            "logger.fullStackTraces",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * Create a new log file daily. Helps with log management. For example, downloading and cleaning logs. Enabled by
     * default.
     */
    public static final ConfigKey<Boolean> LOGGER_ROTATE = new ConfigKey<>(
            "logger.rotate",
            Collections.singletonList(KeyType.GLOBAL));

    /**
     * A list of position attributes to log.
     */
    public static final ConfigKey<String> LOGGER_ATTRIBUTES = new ConfigKey<>(
            "logger.attributes",
            Collections.singletonList(KeyType.GLOBAL),
            "time,position,speed,course,accuracy,result");

}
