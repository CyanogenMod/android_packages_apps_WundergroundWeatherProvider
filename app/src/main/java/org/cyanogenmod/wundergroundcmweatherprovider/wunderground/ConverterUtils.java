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

    private static Map<String, Integer> mWeatherConditionStringToCode = new HashMap<>();

    /**
     * From https://www.wunderground.com/weather/api/d/docs?d=resources/phrase-glossary&_ga=1.207239701.783906908.1458852719&MR=1
     */
    static {
        mWeatherConditionStringToCode.put("Chance of Rain",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        mWeatherConditionStringToCode.put("Chance Rain",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        mWeatherConditionStringToCode.put("Chance of Flurries",
                WeatherContract.WeatherColumns.WeatherCode.SNOW_FLURRIES);
        mWeatherConditionStringToCode.put("Chance of Freezing Rain",
                WeatherContract.WeatherColumns.WeatherCode.FREEZING_RAIN);
        mWeatherConditionStringToCode.put("Chance of Sleet",
                WeatherContract.WeatherColumns.WeatherCode.SLEET);
        mWeatherConditionStringToCode.put("Chance of Snow",
                WeatherContract.WeatherColumns.WeatherCode.SNOW);
        mWeatherConditionStringToCode.put("Chance of Thunderstorms",
                WeatherContract.WeatherColumns.WeatherCode.ISOLATED_THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Chance of a Thunderstorm",
                WeatherContract.WeatherColumns.WeatherCode.ISOLATED_THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Drizzle",
                WeatherContract.WeatherColumns.WeatherCode.DRIZZLE);
        mWeatherConditionStringToCode.put("Rain",
                WeatherContract.WeatherColumns.WeatherCode.SHOWERS);
        mWeatherConditionStringToCode.put("Snow",
                WeatherContract.WeatherColumns.WeatherCode.SNOW);
        mWeatherConditionStringToCode.put("Snow Grains",
                WeatherContract.WeatherColumns.WeatherCode.SNOW_FLURRIES);
        mWeatherConditionStringToCode.put("Ice Crystals",
                WeatherContract.WeatherColumns.WeatherCode.SLEET);
        mWeatherConditionStringToCode.put("Ice Pellets",
                WeatherContract.WeatherColumns.WeatherCode.SLEET);
        mWeatherConditionStringToCode.put("Hail",
                WeatherContract.WeatherColumns.WeatherCode.HAIL);
        mWeatherConditionStringToCode.put("Mist",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        mWeatherConditionStringToCode.put("Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        mWeatherConditionStringToCode.put("Fog Patches",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        mWeatherConditionStringToCode.put("Smoke",
                WeatherContract.WeatherColumns.WeatherCode.SMOKY);
        mWeatherConditionStringToCode.put("Volcanic Ash",
                WeatherContract.WeatherColumns.WeatherCode.SMOKY);
        mWeatherConditionStringToCode.put("Dust",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Sand",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Haze",
                WeatherContract.WeatherColumns.WeatherCode.HAZE);
        mWeatherConditionStringToCode.put("Spray",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        mWeatherConditionStringToCode.put("Dust Whirls",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Sandstorm",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Low Drifting Snow",
                WeatherContract.WeatherColumns.WeatherCode.BLOWING_SNOW);
        mWeatherConditionStringToCode.put("Low Drifting Widespread Dust",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Low Drifting Sand",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Blowing Snow",
                WeatherContract.WeatherColumns.WeatherCode.BLOWING_SNOW);
        mWeatherConditionStringToCode.put("Blowing Widespread Dust",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Blowing Sand",
                WeatherContract.WeatherColumns.WeatherCode.DUST);
        mWeatherConditionStringToCode.put("Rain Mist",
                WeatherContract.WeatherColumns.WeatherCode.SCATTERED_SHOWERS);
        mWeatherConditionStringToCode.put("Rain Showers",
                WeatherContract.WeatherColumns.WeatherCode.SHOWERS);
        mWeatherConditionStringToCode.put("Snow Showers",
                WeatherContract.WeatherColumns.WeatherCode.SNOW_SHOWERS);
        mWeatherConditionStringToCode.put("Snow Blowing",
                WeatherContract.WeatherColumns.WeatherCode.BLOWING_SNOW);
        mWeatherConditionStringToCode.put("Snow Blowing Snow Mist",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_SNOW);
        mWeatherConditionStringToCode.put("Ice Pellet Showers",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_HAIL);
        mWeatherConditionStringToCode.put("Hail Showers",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_HAIL);
        mWeatherConditionStringToCode.put("Small Hail Showers",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_HAIL);
        mWeatherConditionStringToCode.put("Thunderstorm",
                WeatherContract.WeatherColumns.WeatherCode.THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Thunderstorms and Rain",
                WeatherContract.WeatherColumns.WeatherCode.THUNDERSHOWER);
        mWeatherConditionStringToCode.put("Thunderstorms and Snow",
                WeatherContract.WeatherColumns.WeatherCode.THUNDERSHOWER);
        mWeatherConditionStringToCode.put("Thunderstorms and Ice Pellets",
                WeatherContract.WeatherColumns.WeatherCode.SEVERE_THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Thunderstorms with Hail",
                WeatherContract.WeatherColumns.WeatherCode.SEVERE_THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Thunderstorms with Small Hail",
                WeatherContract.WeatherColumns.WeatherCode.SEVERE_THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Freezing Drizzle",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_SLEET);
        mWeatherConditionStringToCode.put("Freezing Rain",
                WeatherContract.WeatherColumns.WeatherCode.MIXED_RAIN_AND_SLEET);
        mWeatherConditionStringToCode.put("Freezing Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        mWeatherConditionStringToCode.put("Patches of Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        mWeatherConditionStringToCode.put("Shallow Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        mWeatherConditionStringToCode.put("Partial Fog",
                WeatherContract.WeatherColumns.WeatherCode.FOGGY);
        mWeatherConditionStringToCode.put("Overcast",
                WeatherContract.WeatherColumns.WeatherCode.FAIR_DAY);
        mWeatherConditionStringToCode.put("Clear",
                WeatherContract.WeatherColumns.WeatherCode.SUNNY);
        mWeatherConditionStringToCode.put("Partly Cloudy",
                WeatherContract.WeatherColumns.WeatherCode.PARTLY_CLOUDY);
        mWeatherConditionStringToCode.put("Mostly Cloudy",
                WeatherContract.WeatherColumns.WeatherCode.CLOUDY);
        mWeatherConditionStringToCode.put("Scattered Clouds",
                WeatherContract.WeatherColumns.WeatherCode.PARTLY_CLOUDY);
        mWeatherConditionStringToCode.put("Small Hail",
                WeatherContract.WeatherColumns.WeatherCode.HAIL);
        mWeatherConditionStringToCode.put("Squalls",
                WeatherContract.WeatherColumns.WeatherCode.ISOLATED_THUNDERSTORMS);
        mWeatherConditionStringToCode.put("Funnel Cloud",
                WeatherContract.WeatherColumns.WeatherCode.TORNADO);
        mWeatherConditionStringToCode.put("Unknown Precipitation",
                WeatherContract.WeatherColumns.WeatherCode.DRIZZLE);
        mWeatherConditionStringToCode.put("Unknown",
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
        if (!TextUtils.isEmpty(weatherCondition) &&
                mWeatherConditionStringToCode.get(weatherCondition) != null) {
            return mWeatherConditionStringToCode.get(weatherCondition);
        }
        return WeatherContract.WeatherColumns.WeatherCode.NOT_AVAILABLE;
    }
}
