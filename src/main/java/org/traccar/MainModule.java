/*
 * Copyright 2018 - 2019 Anton Tananaev (anton@traccar.org)
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.database.AttributesManager;
import org.traccar.database.CalendarManager;
import org.traccar.database.DataManager;
import org.traccar.database.DeviceManager;
import org.traccar.database.GeofenceManager;
import org.traccar.database.IdentityManager;
import org.traccar.database.MaintenancesManager;
import org.traccar.database.StatisticsManager;
import org.traccar.geocoder.AddressFormat;
import org.traccar.geocoder.BanGeocoder;
import org.traccar.geocoder.BingMapsGeocoder;
import org.traccar.geocoder.FactualGeocoder;
import org.traccar.geocoder.GeocodeFarmGeocoder;
import org.traccar.geocoder.GeocodeXyzGeocoder;
import org.traccar.geocoder.Geocoder;
import org.traccar.geocoder.GisgraphyGeocoder;
import org.traccar.geocoder.GoogleGeocoder;
import org.traccar.geocoder.HereGeocoder;
import org.traccar.geocoder.MapQuestGeocoder;
import org.traccar.geocoder.MapmyIndiaGeocoder;
import org.traccar.geocoder.NominatimGeocoder;
import org.traccar.geocoder.OpenCageGeocoder;
import org.traccar.geocoder.PositionStackGeocoder;
import org.traccar.geocoder.TomTomGeocoder;
import org.traccar.geocoder.MapboxGeocoder;
import org.traccar.geolocation.GeolocationProvider;
import org.traccar.geolocation.GoogleGeolocationProvider;
import org.traccar.geolocation.MozillaGeolocationProvider;
import org.traccar.geolocation.OpenCellIdGeolocationProvider;
import org.traccar.geolocation.UnwiredGeolocationProvider;
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
import org.traccar.handler.RemoteAddressHandler;
import org.traccar.handler.SpeedLimitHandler;
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
import org.traccar.reports.model.TripsConfig;

import javax.annotation.Nullable;
import javax.ws.rs.client.Client;
import io.netty.util.Timer;
import org.traccar.speedlimit.OverpassSpeedLimitProvider;
import org.traccar.speedlimit.SpeedLimitProvider;

public class MainModule extends AbstractModule {

    @Provides
    public static ObjectMapper provideObjectMapper() {
        return Context.getObjectMapper();
    }

    @Provides
    public static Config provideConfig() {
        return Context.getConfig();
    }

    @Provides
    public static DataManager provideDataManager() {
        return Context.getDataManager();
    }

    @Provides
    public static IdentityManager provideIdentityManager() {
        return Context.getIdentityManager();
    }

    @Provides
    public static Client provideClient() {
        return Context.getClient();
    }

    @Provides
    public static TripsConfig provideTripsConfig() {
        return Context.getTripsConfig();
    }

    @Provides
    public static DeviceManager provideDeviceManager() {
        return Context.getDeviceManager();
    }

    @Provides
    public static GeofenceManager provideGeofenceManager() {
        return Context.getGeofenceManager();
    }

    @Provides
    public static CalendarManager provideCalendarManager() {
        return Context.getCalendarManager();
    }

    @Provides
    public static AttributesManager provideAttributesManager() {
        return Context.getAttributesManager();
    }

    @Provides
    public static MaintenancesManager provideMaintenancesManager() {
        return Context.getMaintenancesManager();
    }

    @Singleton
    @Provides
    public static StatisticsManager provideStatisticsManager(
            Config config, DataManager dataManager, Client client, ObjectMapper objectMapper) {
        return new StatisticsManager(config, dataManager, client, objectMapper);
    }

    @Singleton
    @Provides
    public static Geocoder provideGeocoder(Config config) {
        if (config.getBoolean(Keys.GEOCODER_ENABLE)) {
            String type = config.getString(Keys.GEOCODER_TYPE, "google");
            String url = config.getString(Keys.GEOCODER_URL);
            String id = config.getString(Keys.GEOCODER_ID);
            String key = config.getString(Keys.GEOCODER_KEY);
            String language = config.getString(Keys.GEOCODER_LANGUAGE);
            String formatString = config.getString(Keys.GEOCODER_FORMAT);
            AddressFormat addressFormat = formatString != null ? new AddressFormat(formatString) : new AddressFormat();

            int cacheSize = config.getInteger(Keys.GEOCODER_CACHE_SIZE);
            switch (type) {
                case "nominatim":
                    return new NominatimGeocoder(url, key, language, cacheSize, addressFormat);
                case "gisgraphy":
                    return new GisgraphyGeocoder(url, cacheSize, addressFormat);
                case "mapquest":
                    return new MapQuestGeocoder(url, key, cacheSize, addressFormat);
                case "opencage":
                    return new OpenCageGeocoder(url, key, cacheSize, addressFormat);
                case "bingmaps":
                    return new BingMapsGeocoder(url, key, cacheSize, addressFormat);
                case "factual":
                    return new FactualGeocoder(url, key, cacheSize, addressFormat);
                case "geocodefarm":
                    return new GeocodeFarmGeocoder(key, language, cacheSize, addressFormat);
                case "geocodexyz":
                    return new GeocodeXyzGeocoder(key, cacheSize, addressFormat);
                case "ban":
                    return new BanGeocoder(cacheSize, addressFormat);
                case "here":
                    return new HereGeocoder(url, id, key, language, cacheSize, addressFormat);
                case "mapmyindia":
                    return new MapmyIndiaGeocoder(url, key, cacheSize, addressFormat);
                case "tomtom":
                    return new TomTomGeocoder(url, key, cacheSize, addressFormat);
                case "positionstack":
                    return new PositionStackGeocoder(key, cacheSize, addressFormat);
                case "mapbox":
                    return new MapboxGeocoder(key, cacheSize, addressFormat);
                default:
                    return new GoogleGeocoder(key, language, cacheSize, addressFormat);
            }
        }
        return null;
    }

    @Singleton
    @Provides
    public static GeolocationProvider provideGeolocationProvider(Config config) {
        if (config.getBoolean(Keys.GEOLOCATION_ENABLE)) {
            String type = config.getString(Keys.GEOLOCATION_TYPE, "mozilla");
            String url = config.getString(Keys.GEOLOCATION_URL);
            String key = config.getString(Keys.GEOLOCATION_KEY);
            switch (type) {
                case "google":
                    return new GoogleGeolocationProvider(key);
                case "opencellid":
                    return new OpenCellIdGeolocationProvider(url, key);
                case "unwired":
                    return new UnwiredGeolocationProvider(url, key);
                default:
                    return new MozillaGeolocationProvider(key);
            }
        }
        return null;
    }

    @Singleton
    @Provides
    public static SpeedLimitProvider provideSpeedLimitProvider(Config config) {
        if (config.getBoolean(Keys.SPEED_LIMIT_ENABLE)) {
            String type = config.getString(Keys.SPEED_LIMIT_TYPE, "overpass");
            String url = config.getString(Keys.SPEED_LIMIT_URL);
            switch (type) {
                case "overpass":
                default:
                    return new OverpassSpeedLimitProvider(url);
            }
        }
        return null;
    }

    @Singleton
    @Provides
    public static DistanceHandler provideDistanceHandler(Config config, IdentityManager identityManager) {
        return new DistanceHandler(config, identityManager);
    }

    @Singleton
    @Provides
    public static FilterHandler provideFilterHandler(Config config) {
        if (config.getBoolean(Keys.FILTER_ENABLE)) {
            return new FilterHandler(config);
        }
        return null;
    }

    @Singleton
    @Provides
    public static HemisphereHandler provideHemisphereHandler(Config config) {
        if (config.hasKey(Keys.LOCATION_LATITUDE_HEMISPHERE) || config.hasKey(Keys.LOCATION_LONGITUDE_HEMISPHERE)) {
            return new HemisphereHandler(config);
        }
        return null;
    }

    @Singleton
    @Provides
    public static RemoteAddressHandler provideRemoteAddressHandler(Config config) {
        if (config.getBoolean(Keys.PROCESSING_REMOTE_ADDRESS_ENABLE)) {
            return new RemoteAddressHandler();
        }
        return null;
    }

    @Singleton
    @Provides
    public static WebDataHandler provideWebDataHandler(
            Config config, IdentityManager identityManager, ObjectMapper objectMapper, Client client) {
        if (config.getBoolean(Keys.FORWARD_ENABLE)) {
            return new WebDataHandler(config, identityManager, objectMapper, client);
        }
        return null;
    }

    @Singleton
    @Provides
    public static GeolocationHandler provideGeolocationHandler(
            Config config, @Nullable GeolocationProvider geolocationProvider, StatisticsManager statisticsManager) {
        if (geolocationProvider != null) {
            return new GeolocationHandler(config, geolocationProvider, statisticsManager);
        }
        return null;
    }

    @Singleton
    @Provides
    public static GeocoderHandler provideGeocoderHandler(
            Config config, @Nullable Geocoder geocoder, IdentityManager identityManager) {
        if (geocoder != null) {
            return new GeocoderHandler(config, geocoder, identityManager);
        }
        return null;
    }

    @Singleton
    @Provides
    public static SpeedLimitHandler provideSpeedLimitHandler(@Nullable SpeedLimitProvider speedLimitProvider) {
        if (speedLimitProvider != null) {
            return new SpeedLimitHandler(speedLimitProvider);
        }
        return null;
    }

    @Singleton
    @Provides
    public static MotionHandler provideMotionHandler(TripsConfig tripsConfig) {
        return new MotionHandler(tripsConfig.getSpeedThreshold());
    }

    @Singleton
    @Provides
    public static EngineHoursHandler provideEngineHoursHandler(Config config, IdentityManager identityManager) {
        if (config.getBoolean(Keys.PROCESSING_ENGINE_HOURS_ENABLE)) {
            return new EngineHoursHandler(identityManager);
        }
        return null;
    }

    @Singleton
    @Provides
    public static CopyAttributesHandler provideCopyAttributesHandler(Config config, IdentityManager identityManager) {
        if (config.getBoolean(Keys.PROCESSING_COPY_ATTRIBUTES_ENABLE)) {
            return new CopyAttributesHandler(identityManager);
        }
        return null;
    }

    @Singleton
    @Provides
    public static ComputedAttributesHandler provideComputedAttributesHandler(
            Config config, IdentityManager identityManager, AttributesManager attributesManager) {
        if (config.getBoolean(Keys.PROCESSING_COMPUTED_ATTRIBUTES_ENABLE)) {
            return new ComputedAttributesHandler(config, identityManager, attributesManager);
        }
        return null;
    }

    @Singleton
    @Provides
    public static TimeHandler provideTimeHandler(Config config) {
        if (config.hasKey(Keys.TIME_OVERRIDE)) {
            return new TimeHandler(config);
        }
        return null;
    }

    @Singleton
    @Provides
    public static DefaultDataHandler provideDefaultDataHandler(@Nullable DataManager dataManager) {
        if (dataManager != null) {
            return new DefaultDataHandler(dataManager);
        }
        return null;
    }

    @Singleton
    @Provides
    public static CommandResultEventHandler provideCommandResultEventHandler() {
        return new CommandResultEventHandler();
    }

    @Singleton
    @Provides
    public static OverspeedEventHandler provideOverspeedEventHandler(
            Config config, DeviceManager deviceManager, GeofenceManager geofenceManager) {
        return new OverspeedEventHandler(config, deviceManager, geofenceManager);
    }

    @Singleton
    @Provides
    public static FuelDropEventHandler provideFuelDropEventHandler(IdentityManager identityManager) {
        return new FuelDropEventHandler(identityManager);
    }

    @Singleton
    @Provides
    public static MotionEventHandler provideMotionEventHandler(
            IdentityManager identityManager, DeviceManager deviceManager, TripsConfig tripsConfig) {
        return new MotionEventHandler(identityManager, deviceManager, tripsConfig);
    }

    @Singleton
    @Provides
    public static GeofenceEventHandler provideGeofenceEventHandler(
            IdentityManager identityManager, GeofenceManager geofenceManager, CalendarManager calendarManager) {
        return new GeofenceEventHandler(identityManager, geofenceManager, calendarManager);
    }

    @Singleton
    @Provides
    public static AlertEventHandler provideAlertEventHandler(Config config, IdentityManager identityManager) {
        return new AlertEventHandler(config, identityManager);
    }

    @Singleton
    @Provides
    public static IgnitionEventHandler provideIgnitionEventHandler(IdentityManager identityManager) {
        return new IgnitionEventHandler(identityManager);
    }

    @Singleton
    @Provides
    public static MaintenanceEventHandler provideMaintenanceEventHandler(
            IdentityManager identityManager, MaintenancesManager maintenancesManager) {
        return new MaintenanceEventHandler(identityManager, maintenancesManager);
    }

    @Singleton
    @Provides
    public static DriverEventHandler provideDriverEventHandler(IdentityManager identityManager) {
        return new DriverEventHandler(identityManager);
    }

    @Singleton
    @Provides
    public static Timer provideTimer() {
        return GlobalTimer.getTimer();
    }

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
    }

}
