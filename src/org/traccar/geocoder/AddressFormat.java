/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Available parameters:
 *
 * %p - postcode
 * %c - country
 * %s - state
 * %d - district
 * %t - settlement (town)
 * %u - suburb
 * %r - street (road)
 * %h - house
 * %f - formatted address
 *
 */
public class AddressFormat extends Format {

    private final String format;

    public AddressFormat() {
        this("%h %r, %t, %s, %c");
    }

    public AddressFormat(String format) {
        this.format = format;
    }

    private static String replace(String s, String key, String value) {
        if (value != null) {
            s = s.replace(key, value);
        } else {
            s = s.replaceAll("[, ]*" + key, "");
        }
        return s;
    }

    @Override
    public StringBuffer format(Object o, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        Address address = (Address) o;
        String result = format;

        result = replace(result, "%p", address.getPostcode());
        result = replace(result, "%c", address.getCountry());
        result = replace(result, "%s", address.getState());
        result = replace(result, "%d", address.getDistrict());
        result = replace(result, "%t", address.getSettlement());
        result = replace(result, "%u", address.getSuburb());
        result = replace(result, "%r", address.getStreet());
        result = replace(result, "%h", address.getHouse());
        result = replace(result, "%f", address.getFormattedAddress());
        result = replace(result, "%z", address.getSpeedLimit());

        result = result.replaceAll("^[, ]*", "");

        return stringBuffer.append(result);
    }

    @Override
    public Address parseObject(String s, ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }

}
