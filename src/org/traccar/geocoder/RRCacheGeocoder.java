/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.traccar.geocoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.traccar.Config;
import org.traccar.Context;
import org.traccar.helper.Log;

/**
 *
 * @author eriansv@gmail.com
 */

public class RRCacheGeocoder implements Geocoder {

    private final List<Provider> providers;
    private Map<Map.Entry<Double, Double>, String> cache;
    private int pointer = 0;
    private int cacheCounter = 0;
    private int cacheDay = 0;

    private class Provider implements Geocoder {

        private final String name;
        private final Geocoder geocoder;
        private final int limit;
        private int counter = 0;
        private int day = 0;

        Provider(String name, Geocoder geocoder, int limit) {

            this.name = name;
            this.geocoder = geocoder;

            if (limit > 0) {
                this.limit = limit;
            } else {
                this.limit = 2000;
            }

            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        }

        private synchronized void updateCounter() {

            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            if (day != currentDay) {
                Log.warning("RRCache.Provider " + name + ": Resetting Counter (" + counter + ")");
                counter = 0;
                day = currentDay;
            }

            if (cacheDay != currentDay) {
                Log.warning("RRCache Resetting Cache Counter (" + cacheCounter + ")");
                cacheCounter = 0;
                cacheDay = currentDay;

                Log.warning("RRCache Loading Cache (" + loadCache() + ")");

            }

            counter++;
        }

        @Override
        public void getAddress(AddressFormat format, double latitude, double longitude,
                ReverseGeocoderCallback callback) {

            if (counter < limit) {
                updateCounter();
                geocoder.getAddress(format, latitude, longitude, callback);
            } else {
                callback.onFailure(new GeocoderException(name + " - Request limit reached (" + limit + ")"));
            }
        }

        public boolean isLimitReached() {
            return counter >= limit;
        }

        public String getName() {
            return name;
        }

    }

    public RRCacheGeocoder() {
        this.providers = loadProviders();
        this.cacheDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Log.warning("RRCache Loading Cache (" + loadCache() + ")");

    }

    private String getQuery(Config config, String key) {
        String query = config.getString(key);
        if (query == null) {
            Log.warning("Query not provided: " + key);
        }
        return query;
    }

    private List<Provider> loadProviders() {

        Config config = Context.getConfig();

        int numProviders = config.getInteger("geocoder.rrcache", 0);
        int cacheSize = config.getInteger("geocoder.cacheSize", 0) / (numProviders + 1);

        String seq = "";

        List<Provider> result = new ArrayList<>();

        for (int i = 0; i <= numProviders; i++) {

            if (i > 0) {
                seq = "." + i;
            }

            String type = config.getString("geocoder.type" + seq);
            String url = config.getString("geocoder.url" + seq);
            String key = config.getString("geocoder.key" + seq);
            String language = config.getString("geocoder.language" + seq);
            Integer limit = config.getInteger("geocoder.limit" + seq, 2000);

            Geocoder geocoder = null;

            switch (type) {
                case "nominatim":
                    geocoder = new NominatimGeocoder(url, key, language, cacheSize);
                    break;
                case "gisgraphy":
                    geocoder = new GisgraphyGeocoder(url, cacheSize);
                    break;
                case "mapquest":
                    geocoder = new MapQuestGeocoder(url, key, cacheSize);
                    break;
                case "opencage":
                    geocoder = new OpenCageGeocoder(url, key, cacheSize);
                    break;
                case "bingmaps":
                    geocoder = new BingMapsGeocoder(url, key, cacheSize);
                    break;
                case "factual":
                    geocoder = new FactualGeocoder(url, key, cacheSize);
                    break;
                case "geocodefarm":
                    geocoder = new GeocodeFarmGeocoder(key, language, cacheSize);
                    break;
                case "google":
                    geocoder = new GoogleGeocoder(key, language, cacheSize);
                    break;
                default:
                    break;
            }

            if (geocoder != null) {
                Provider p = new Provider(type, geocoder, limit);
                result.add(p);
                Log.warning("RRCache.loadProviders: " + i + " : " + type + " : "
                        + limit + " : " + language + " : " + url);
            }
        }

        return result;
    }

    private int loadCache() {

        int numRows = 0;

        Config config = Context.getConfig();
        DataSource ds = Context.getDataManager().getDataSource();

        if (this.cache != null) {
            this.cache.clear();
        } else {
            this.cache = new HashMap<>();
        }

        try {
            ResultSet rs = ds.getConnection().prepareStatement(getQuery(config, "database.rrcache")).executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    numRows++;
                    cache.put(new AbstractMap.SimpleImmutableEntry<>(rs.getDouble("longitude"),
                            rs.getDouble("latitude")),
                            rs.getString("address"));
                }
            }
        } catch (Exception ex) {
            Log.warning("RRCache.loadCache Exception: " + ex.getMessage());
        }

        return numRows;
    }

    private double fixedValue(double value) {
        return BigDecimal.valueOf(value).setScale(5, RoundingMode.HALF_UP).doubleValue();
    }

    private synchronized int nextPointer() {

        pointer++;
        if (pointer >= providers.size()) {
            pointer = 0;
        }

        int ls = 0; // Loop Sequence
        while (providers.get(pointer).isLimitReached()) {
            ls++;
            pointer++;
            if (pointer >= providers.size()) {
                pointer = 0;
            }
            if (ls >= providers.size()) {
                break;
            }
        }

        return pointer;

    }

    @Override
    public void getAddress(
            final AddressFormat format, final double latitude,
            final double longitude, final ReverseGeocoderCallback callback) {

        final double fixedLatitude = fixedValue(latitude);
        final double fixedLongitude = fixedValue(longitude);

        if (cache != null) {
            String cachedAddress = cache.get(new AbstractMap.SimpleImmutableEntry<>(fixedLatitude, fixedLongitude));
            if (cachedAddress != null) {
                callback.onSuccess(cachedAddress);
                cacheCounter++;
                return;
            }
        }

        final int ptr = nextPointer();
        final Provider provider = providers.get(ptr);
        provider.getAddress(format, fixedLatitude, fixedLongitude,
                new Geocoder.ReverseGeocoderCallback() {
            @Override
            public void onSuccess(String address) {
                if (address != null && !address.isEmpty()) {
                    callback.onSuccess(address);
                } else {
                    int next = nextPointer();
                    Log.warning("RRCache.getAddress onSuccess - Address Empty : Traying Next Provider : " + ptr + "."
                            + provider.getName() + " -> " + next + "." + providers.get(next).getName());
                    providers.get(next).getAddress(format, fixedLatitude, fixedLongitude, callback);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                int next = nextPointer();
                Log.warning("RRCache.getAddress onFailure - Traying Next Provider : " + ptr + "."
                        + provider.getName() + " -> " + next + "." + providers.get(next).getName()
                        + " - E: " + e.getMessage());
                providers.get(next).getAddress(format, fixedLatitude, fixedLongitude, callback);
            }
        });

    }

}
