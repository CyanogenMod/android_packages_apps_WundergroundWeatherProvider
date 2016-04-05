package org.cyanogenmod.wundergroundcmweatherprovider.wunderground;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.ForecastDayResponse;

import java.util.ArrayList;
import java.util.List;

import cyanogenmod.providers.WeatherContract;
import cyanogenmod.weather.WeatherInfo;

public class ConverterUtils {

    private ConverterUtils() {
    }

    public static ArrayList<WeatherInfo.DayForecast> convertSimpleFCToDayForcast(
            List<ForecastDayResponse> forecastDayResponses) {
        ArrayList<WeatherInfo.DayForecast> dayForecasts = new ArrayList<>();
        for (ForecastDayResponse forecastDayResponse : forecastDayResponses) {
            WeatherInfo.DayForecast dayForecast = new WeatherInfo.DayForecast.Builder()
                    .setHigh(forecastDayResponse.getHigh().getFahrenheit())
                    .setLow(forecastDayResponse.getLow().getFahrenheit())
                    .setWeatherCondition(WeatherContract.WeatherColumns.WeatherCode.SUNNY)
                    .build();
            dayForecasts.add(dayForecast);
        }
        return dayForecasts;
    }
}
