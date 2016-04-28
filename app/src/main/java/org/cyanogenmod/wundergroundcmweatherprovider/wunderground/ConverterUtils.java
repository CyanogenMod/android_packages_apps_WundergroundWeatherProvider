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

import android.text.TextUtils;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.citylookup.CityDisambiguationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.ForecastDayResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyanogenmod.providers.WeatherContract;
import cyanogenmod.weather.WeatherInfo;
import cyanogenmod.weather.WeatherLocation;

public class ConverterUtils {

    private ConverterUtils() {
    }

    private static Map<String, Integer> sWeatherConditionStringToCode = new HashMap<>();

    /**
     * From https://www.wunderground.com/weather/api/d/docs?d=resources/phrase-glossary&_ga=1.207239701.783906908.1458852719&MR=1
     */
    static {
        sWeatherConditionStringToCode.put("Chance of Rain",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        sWeatherConditionStringToCode.put("Chance Rain",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        sWeatherConditionStringToCode.put("Chance of Flurries",
                WeatherContract.WeatherColumns.WeatherCode.SNOW_FLURRIES);
        sWeatherConditionStringToCode.put("Chance of Freezing Rain",
                WeatherContract.WeatherColumns.WeatherCode.FREEZING_RAIN);
        sWeatherConditionStringToCode.put("Chance of Sleet",
                WeatherContract.WeatherColumns.WeatherCode.SLEET);
        sWeatherConditionStringToCode.put("Chance of Snow",
                WeatherContract.WeatherColumns.WeatherCode.SNOW);
        sWeatherConditionStringToCode.put("Chance of Thunderstorms",
                WeatherContract.WeatherColumns.WeatherCode.ISOLATED_THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Chance of a Thunderstorm",
                WeatherContract.WeatherColumns.WeatherCode.ISOLATED_THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Drizzle",
                WeatherContract.WeatherColumns.WeatherCode.DRIZZLE);
        sWeatherConditionStringToCode.put("Rain",
                WeatherContract.WeatherColumns.WeatherCode.SHOWERS);
        sWeatherConditionStringToCode.put("Snow",
                WeatherContract.WeatherColumns.WeatherCode.SNOW);
        sWeatherConditionStringToCode.put("Snow Grains",
                WeatherContract.WeatherColumns.WeatherCode.SNOW_FLURRIES);
        sWeatherConditionStringToCode.put("Ice Crystals",
                WeatherContract.WeatherColumns.WeatherCode.SLEET);
        sWeatherConditionStringToCode.put("Ice Pellets",
                WeatherContract.WeatherColumns.WeatherCode.SLEET);
        sWeatherConditionStringToCode.put("Hail",
                WeatherContract.WeatherColumns.WeatherCode.HAIL);
        sWeatherConditionStringToCode.put("Mist",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        sWeatherConditionStringToCode.put("Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        sWeatherConditionStringToCode.put("Fog Patches",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        sWeatherConditionStringToCode.put("Smoke",
                WeatherContract.WeatherColumns.WeatherCode.SMOKY);
        sWeatherConditionStringToCode.put("Volcanic Ash",
                WeatherContract.WeatherColumns.WeatherCode.SMOKY);
        sWeatherConditionStringToCode.put("Dust",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Sand",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Haze",
                WeatherContract.WeatherColumns.WeatherCode.HAZE);
        sWeatherConditionStringToCode.put("Spray",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        sWeatherConditionStringToCode.put("Dust Whirls",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Sandstorm",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Low Drifting Snow",
                WeatherContract.WeatherColumns.WeatherCode.BLOWING_SNOW);
        sWeatherConditionStringToCode.put("Low Drifting Widespread Dust",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Low Drifting Sand",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Blowing Snow",
                WeatherContract.WeatherColumns.WeatherCode.BLOWING_SNOW);
        sWeatherConditionStringToCode.put("Blowing Widespread Dust",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Blowing Sand",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        sWeatherConditionStringToCode.put("Rain Mist",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        sWeatherConditionStringToCode.put("Rain Showers",
                WeatherContract.WeatherColumns.WeatherCode.SHOWERS);
        sWeatherConditionStringToCode.put("Snow Showers",
                WeatherContract.WeatherColumns.WeatherCode.SNOW_SHOWERS);
        sWeatherConditionStringToCode.put("Snow Blowing",
                WeatherContract.WeatherColumns.WeatherCode.BLOWING_SNOW);
        sWeatherConditionStringToCode.put("Snow Blowing Snow Mist",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_SNOW);
        sWeatherConditionStringToCode.put("Ice Pellet Showers",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_HAIL);
        sWeatherConditionStringToCode.put("Hail Showers",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_HAIL);
        sWeatherConditionStringToCode.put("Small Hail Showers",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_HAIL);
        sWeatherConditionStringToCode.put("Thunderstorm",
                WeatherContract.WeatherColumns.WeatherCode.THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Thunderstorms and Rain",
                WeatherContract.WeatherColumns.WeatherCode.THUNDERSHOWER);
        sWeatherConditionStringToCode.put("Thunderstorms and Snow",
                WeatherContract.WeatherColumns.WeatherCode.THUNDERSHOWER);
        sWeatherConditionStringToCode.put("Thunderstorms and Ice Pellets",
                WeatherContract.WeatherColumns.WeatherCode.SEVERE_THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Thunderstorms with Hail",
                WeatherContract.WeatherColumns.WeatherCode.SEVERE_THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Thunderstorms with Small Hail",
                WeatherContract.WeatherColumns.WeatherCode.SEVERE_THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Freezing Drizzle",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_SLEET);
        sWeatherConditionStringToCode.put("Freezing Rain",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_SLEET);
        sWeatherConditionStringToCode.put("Freezing Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        sWeatherConditionStringToCode.put("Patches of Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        sWeatherConditionStringToCode.put("Shallow Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        sWeatherConditionStringToCode.put("Partial Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        sWeatherConditionStringToCode.put("Overcast",
                WeatherContract.WeatherColumns.WeatherCode.FAIR_DAY);
        sWeatherConditionStringToCode.put("Clear",
                WeatherContract.WeatherColumns.WeatherCode.SUNNY);
        sWeatherConditionStringToCode.put("Partly Cloudy",
                WeatherContract.WeatherColumns.WeatherCode.PARTLY_CLOUDY_DAY);
        sWeatherConditionStringToCode.put("Mostly Cloudy",
                WeatherContract.WeatherColumns.WeatherCode.CLOUDY);
        sWeatherConditionStringToCode.put("Scattered Clouds",
                WeatherContract.WeatherColumns.WeatherCode.PARTLY_CLOUDY_DAY);
        sWeatherConditionStringToCode.put("Small Hail",
                WeatherContract.WeatherColumns.WeatherCode.HAIL);
        sWeatherConditionStringToCode.put("Squalls",
                WeatherContract.WeatherColumns.WeatherCode.ISOLATED_THUNDERSTORMS);
        sWeatherConditionStringToCode.put("Funnel Cloud",
                WeatherContract.WeatherColumns.WeatherCode.TORNADO);
        sWeatherConditionStringToCode.put("Unknown Precipitation",
                WeatherContract.WeatherColumns.WeatherCode.DRIZZLE);
        sWeatherConditionStringToCode.put("Unknown",
                WeatherContract.WeatherColumns.WeatherCode.NOT_AVAILABLE);

    }

    public static ArrayList<WeatherInfo.DayForecast> convertSimpleFCToDayForcast(
            List<ForecastDayResponse> forecastDayResponses) {
        ArrayList<WeatherInfo.DayForecast> dayForecasts = new ArrayList<>();
        for (ForecastDayResponse forecastDayResponse : forecastDayResponses) {
            WeatherInfo.DayForecast dayForecast = new WeatherInfo.DayForecast
                    .Builder(
                    convertWeatherConditionStringToWeatherConditionCode(
                            forecastDayResponse.getConditions()))
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

    public static int convertWeatherConditionStringToWeatherConditionCode(String weatherCondition) {
        return sWeatherConditionStringToCode.containsKey(weatherCondition)
                ? sWeatherConditionStringToCode.get(weatherCondition)
                : WeatherContract.WeatherColumns.WeatherCode.NOT_AVAILABLE;
    }
}
