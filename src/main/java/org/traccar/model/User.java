/*
 * Copyright 2013 - 2018 Anton Tananaev (anton@traccar.org)
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
package org.traccar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.traccar.database.QueryExtended;
import org.traccar.database.QueryIgnore;
import org.traccar.helper.Hashing;
import org.traccar.Context;

import java.util.Date;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorException;

public class User extends ExtendedModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private boolean readonly;

    public boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    private boolean administrator;

    public boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    private String map;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private int zoom;

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    private boolean twelveHourFormat;

    public boolean getTwelveHourFormat() {
        return twelveHourFormat;
    }

    public void setTwelveHourFormat(boolean twelveHourFormat) {
        this.twelveHourFormat = twelveHourFormat;
    }

    private String coordinateFormat;

    public String getCoordinateFormat() {
        return coordinateFormat;
    }

    public void setCoordinateFormat(String coordinateFormat) {
        this.coordinateFormat = coordinateFormat;
    }

    private boolean disabled;

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    private Date expirationTime;

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    private int deviceLimit;

    public int getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(int deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    private int userLimit;

    public int getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    private boolean deviceReadonly;

    public boolean getDeviceReadonly() {
        return deviceReadonly;
    }

    public void setDeviceReadonly(boolean deviceReadonly) {
        this.deviceReadonly = deviceReadonly;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (token != null && !token.isEmpty()) {
            if (!token.matches("^[a-zA-Z0-9-]{16,}$")) {
                throw new IllegalArgumentException("Illegal token");
            }
            this.token = token;
        } else {
            this.token = null;
        }
    }

    private boolean limitCommands;

    public boolean getLimitCommands() {
        return limitCommands;
    }

    public void setLimitCommands(boolean limitCommands) {
        this.limitCommands = limitCommands;
    }

    private String poiLayer;

    public String getPoiLayer() {
        return poiLayer;
    }

    public void setPoiLayer(String poiLayer) {
        this.poiLayer = poiLayer;
    }

    @QueryIgnore
    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            Hashing.HashingResult hashingResult = Hashing.createHash(password);
            hashedPassword = hashingResult.getHash();
            salt = hashingResult.getSalt();
        }
    }

    private String hashedPassword;

    @JsonIgnore
    @QueryExtended
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    private String salt;

    @JsonIgnore
    @QueryExtended
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isPasswordValid(String password) {
        return Hashing.validatePassword(password, hashedPassword, salt);
    }

    private String totpKey;
    private boolean useTotp;

    public void setTotpKey(String key) {
        if (key.isEmpty()) {
            this.totpKey = null;
            this.totpKey = this.getTotpKey();
            return;
        }
        this.totpKey = key;
    }

    public String getTotpKey() {
        if (this.totpKey == null) {
            GoogleAuthenticator authenticator = new GoogleAuthenticator();
            this.setTotpKey(authenticator.createCredentials().getKey());
        }
        return this.totpKey;
    }

    public void setUseTotp(boolean doUse) {
        this.useTotp = doUse;
    }

    public boolean getUseTotp() {
        if (!Context.getConfig().getBoolean("totp.enabled")) {
            return false;
        }
        return this.useTotp;
    }

    @JsonIgnore
    @QueryIgnore
    public boolean isTotpAuthCodeValid(String code) {
        if (!Context.getConfig().getBoolean("totp.enabled")) {
            return true;
        }

        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        if (!Context.getConfig().getBoolean("totp.enforce")) {
            if (!this.useTotp) {
                return true;
            }
        }

        int numericCode;
        try {
            numericCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return false;
        }

        Boolean result = false;
        try {
            result = authenticator.authorize(getTotpKey(), numericCode);
        } catch (GoogleAuthenticatorException e) {
            result = false;
        }
        return result;
    }
}
