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

package org.cyanogenmod.wundergroundcmweatherprovider.wunderground;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.citylookup.CityDisambiguationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.ForecastDayResponse;

import java.util.ArrayList;
import java.util.List;

import cyanogenmod.providers.WeatherContract;
import cyanogenmod.weather.WeatherInfo;
import cyanogenmod.weather.WeatherLocation;

public class ConverterUtils {

    private ConverterUtils() {
    }

    public static ArrayList<WeatherInfo.DayForecast> convertSimpleFCToDayForcast(
            List<ForecastDayResponse> forecastDayResponses) {
        ArrayList<WeatherInfo.DayForecast> dayForecasts = new ArrayList<>();
        for (ForecastDayResponse forecastDayResponse : forecastDayResponses) {
            WeatherInfo.DayForecast dayForecast = new WeatherInfo.DayForecast
                    .Builder(WeatherContract.WeatherColumns.WeatherCode.SUNNY)
                    .setHigh(forecastDayResponse.getHigh().getFahrenheit())
                    .setLow(forecastDayResponse.getLow().getFahrenheit())
                    .build();
            dayForecasts.add(dayForecast);
        }
        return dayForecasts;
    }

    public static ArrayList<WeatherLocation> convertDisambiguationsToWeatherLocations(
            List<CityDisambiguationResponse> cityDisambiguationResponses) {
        ArrayList<WeatherLocation> weatherLocations = new ArrayList<>();
        for (CityDisambiguationResponse cityDisambiguationResponse : cityDisambiguationResponses) {
            WeatherLocation weatherLocation = new WeatherLocation.Builder(
                    cityDisambiguationResponse.getCity())
                    .setCountry(cityDisambiguationResponse.getCountry())
                    .build();
            weatherLocations.add(weatherLocation);
        }
        return weatherLocations;
    }
}
