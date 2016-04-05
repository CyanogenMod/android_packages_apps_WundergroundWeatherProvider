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
