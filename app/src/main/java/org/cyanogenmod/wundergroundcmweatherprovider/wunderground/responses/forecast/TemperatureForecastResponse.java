package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast;

import java.io.Serializable;

public class TemperatureForecastResponse implements Serializable {
    private int fahrenheit;
    private int celsius;

    public int getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(int fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public int getCelsius() {
        return celsius;
    }

    public void setCelsius(int celsius) {
        this.celsius = celsius;
    }
}
