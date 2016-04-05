package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WundergroundReponse implements Serializable {
    @SerializedName("current_observation")
    private CurrentObservationResponse currentObservationResponse;

    @SerializedName("forecast")
    private ForecastResponse forecastResponse;

    public CurrentObservationResponse getCurrentObservation() {
        return currentObservationResponse;
    }

    public void setCurrentObservationResponse(CurrentObservationResponse
            currentObservationResponse) {
        this.currentObservationResponse = currentObservationResponse;
    }

    public ForecastResponse getForecast() {
        return forecastResponse;
    }

    public void setForecastResponse(ForecastResponse forecastResponse) {
        this.forecastResponse = forecastResponse;
    }

    @Override
    public String toString() {
        return "WundergroundResponse:\n"
                + "Forecast: " + forecastResponse.toString() + "\n"
                + "Current Observation: " + currentObservationResponse.toString();
    }
}
