package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by adnan on 4/5/16.
 */
public class SimpleForecastResponse implements Serializable {
    @SerializedName("forecastday")
    private List<ForecastDayResponse> forecastDay;

    public List<ForecastDayResponse> getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(List<ForecastDayResponse> forecastDay) {
        this.forecastDay = forecastDay;
    }
}
