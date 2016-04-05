package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by adnan on 4/5/16.
 */
public class CurrentObservationResponse implements Serializable {

    @SerializedName("display_location")
    private DisplayLocationResponse displayLocationResponse;

    private String weather;

    @SerializedName("temperature_string")
    private String temperatureString;

    @SerializedName("temp_f")
    private Double tempF;

    @SerializedName("temp_c")
    private Double tempC;

    @SerializedName("wind_dir")
    private String windDir;

    @SerializedName("wind_degrees")
    private Double windDegrees;

    @SerializedName("wind_mph")
    private Double windMph;

    public DisplayLocationResponse getDisplayLocation() {
        return displayLocationResponse;
    }

    public void setDisplayLocation(DisplayLocationResponse city) {
        this.displayLocationResponse = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperatureString() {
        return temperatureString;
    }

    public void setTemperatureString(String temperatureString) {
        this.temperatureString = temperatureString;
    }

    public Double getTempF() {
        return tempF;
    }

    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    public Double getTempC() {
        return tempC;
    }

    public void setTempC(Double tempC) {
        this.tempC = tempC;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public Double getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(Double windDegrees) {
        this.windDegrees = windDegrees;
    }

    public Double getWindMph() {
        return windMph;
    }

    public void setWindMph(Double windMph) {
        this.windMph = windMph;
    }
}
