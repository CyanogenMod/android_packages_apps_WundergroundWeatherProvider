/**
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast;

import java.io.Serializable;

public class ForecastDayResponse implements Serializable {
    private TemperatureForecastResponse high;
    private TemperatureForecastResponse low;

    private String conditions;

    public TemperatureForecastResponse getHigh() {
        return high;
    }

    public void setHigh(TemperatureForecastResponse high) {
        this.high = high;
    }

    public TemperatureForecastResponse getLow() {
        return low;
    }

    public void setLow(TemperatureForecastResponse low) {
        this.low = low;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
