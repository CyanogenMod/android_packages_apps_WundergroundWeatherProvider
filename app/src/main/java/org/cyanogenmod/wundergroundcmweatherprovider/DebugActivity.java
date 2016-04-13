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

package org.cyanogenmod.wundergroundcmweatherprovider;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.ConverterUtils;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.Feature;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.WundergroundServiceManager;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.CurrentObservationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.DisplayLocationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.ForecastResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.citylookup.CityDisambiguationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.SimpleForecastResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cyanogenmod.providers.WeatherContract;
import cyanogenmod.weather.CMWeatherManager;
import cyanogenmod.weather.RequestInfo;
import cyanogenmod.weather.WeatherInfo;
import cyanogenmod.weather.WeatherLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebugActivity extends WUBaseActivity implements
        CMWeatherManager.WeatherServiceProviderChangeListener,
        CMWeatherManager.WeatherUpdateRequestListener,
        CMWeatherManager.LookupCityRequestListener,
        LocationListener {

    private static final String TAG = DebugActivity.class.getSimpleName();

    private static final int TYPE_CITY_STATE = 0;
    private static final int TYPE_POSTAL_CODE = 1;

    @Inject
    WundergroundServiceManager mWundergroundServiceManager;

    private CMWeatherManager mWeatherManager;
    private LocationManager mLocationManager;

    private boolean mDirectRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeatherManager = CMWeatherManager.getInstance(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mWeatherManager.registerWeatherServiceProviderChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestWeatherInfoByGeoLocation(View v) {
        mDirectRequest = false;
        requestWeatherInfoByGeoLocation();
    }

    public void requestWeatherInfoByGeoLocationDirectly(View v) {
        mDirectRequest = true;
        requestWeatherInfoByGeoLocation();
    }

    private void requestWeatherInfoByGeoLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        mLocationManager.requestSingleUpdate(criteria, this, Looper.getMainLooper());
    }

    public void requestWeatherInfoByWeatherLocationCityState(View v) {
        mDirectRequest = false;
        requestWeatherInfoByWeatherLocation(TYPE_CITY_STATE);
    }

    public void requestWeatherInfoByWeatherLocationCityStateDirectly(View v) {
        mDirectRequest = true;
        requestWeatherInfoByWeatherLocation(TYPE_CITY_STATE);
    }

    public void requestWeatherInfoByWeatherLocationPostalcode(View v) {
        mDirectRequest = false;
        requestWeatherInfoByWeatherLocation(TYPE_POSTAL_CODE);
    }

    public void requestWeatherInfoByWeatherLocationPostalcodeDirectly(View v) {
        mDirectRequest = true;
        requestWeatherInfoByWeatherLocation(TYPE_POSTAL_CODE);
    }

    public void requestCityDisambiguation(View v) {
        mDirectRequest = false;
        requestCityDisambiguation();
    }

    public void requestCityDisambiguationDirectly(View v) {
        mDirectRequest = true;
        requestCityDisambiguation();
    }

    private static final String HARDCODED_CITY = "DALLAS";
    private void requestCityDisambiguation() {
        if (!mDirectRequest) {
            mWeatherManager.lookupCity(HARDCODED_CITY, this);
        } else {
            Call<WundergroundReponse> wundergroundCall =
                    mWundergroundServiceManager.lookupCity(HARDCODED_CITY);
            wundergroundCall.enqueue(new Callback<WundergroundReponse>() {
                @Override
                public void onResponse(Call<WundergroundReponse> call, Response<WundergroundReponse> response) {
                    List<CityDisambiguationResponse> cityDisambiguationResponses =
                            response.body().getCityDisambiguation();

                    ArrayList<WeatherLocation> weatherLocations =
                            ConverterUtils.convertDisambiguationsToWeatherLocations(
                                    cityDisambiguationResponses);

                    Log.d(TAG, "Received disambiguation:");
                    for (WeatherLocation weatherLocation : weatherLocations) {
                        Log.d(TAG, "Weather location: " + weatherLocation);
                    }
                }

                @Override
                public void onFailure(Call<WundergroundReponse> call, Throwable t) {

                }
            });
        }
    }

    private void requestWeatherInfoByWeatherLocation(int type) {
        WeatherLocation weatherLocation = new WeatherLocation.Builder("Seattle", "Seattle")
                .setPostalCode("98121")
                .setCountry("US")
                .setState("WA")
                .build();

        Log.d(TAG, "Requesting weather by weather location " + weatherLocation);
        Call<WundergroundReponse> wundergroundCall = null;
        if (!mDirectRequest) {
            mWeatherManager.requestWeatherUpdate(weatherLocation, this);
            return;
        } else  {
            if (type == TYPE_CITY_STATE) {
                wundergroundCall =
                        mWundergroundServiceManager.query(weatherLocation.getState(),
                                weatherLocation.getCity(), Feature.conditions, Feature.forecast);
            } else if (type == TYPE_POSTAL_CODE) {
                wundergroundCall =
                        mWundergroundServiceManager.query(weatherLocation.getPostalCode(),
                                Feature.conditions, Feature.forecast);
            }
        }

        wundergroundCall.enqueue(new Callback<WundergroundReponse>() {
            @Override
            public void onResponse(Call<WundergroundReponse> call, Response<WundergroundReponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Received response:\n" + response.body().toString());
                    WundergroundReponse wundergroundReponse = response.body();

                    if (wundergroundReponse == null) {
                        Log.d(TAG, "Null wu reponse, return");
                        return;
                    }

                    CurrentObservationResponse currentObservationResponse =
                            wundergroundReponse.getCurrentObservation();

                    if (currentObservationResponse == null) {
                        Log.d(TAG, "Null co reponse, return");
                        return;
                    }

                    WeatherInfo.Builder weatherInfoBuilder =
                            new WeatherInfo.Builder(
                                    currentObservationResponse.getDisplayLocation().getCity(),
                                    currentObservationResponse.getTempF(),
                                    WeatherContract.WeatherColumns.TempUnit.FAHRENHEIT);

                    weatherInfoBuilder.setWeatherCondition(
                            WeatherContract.WeatherColumns.WeatherCode.CLOUDY);

                    DisplayLocationResponse displayLocationResponse =
                            currentObservationResponse.getDisplayLocation();

                    if (displayLocationResponse == null) {
                        Log.d(TAG, "Null dl reponse, return");
                        return;
                    }

                    // Set humidity
                    weatherInfoBuilder.setHumidity(currentObservationResponse.getHumidity()
                            .floatValue());

                    // Set wind arguments
                    weatherInfoBuilder.setWind(
                            currentObservationResponse.getWindMph().floatValue(),
                            currentObservationResponse.getWindDegrees().floatValue(),
                            WeatherContract.WeatherColumns.WindSpeedUnit.MPH);

                    ForecastResponse forecastResponse =
                            wundergroundReponse.getForecast();

                    if (forecastResponse == null) {
                        Log.d(TAG, "Null fc reponse, return");
                        return;
                    }

                    SimpleForecastResponse simpleForecastResponse =
                            forecastResponse.getSimpleForecast();

                    if (simpleForecastResponse == null) {
                        Log.d(TAG, "Null sf reponse, return");
                        return;
                    }

                    ArrayList<WeatherInfo.DayForecast> dayForecasts =
                            ConverterUtils.convertSimpleFCToDayForcast(
                                    simpleForecastResponse.getForecastDay());
                    weatherInfoBuilder.setForecast(dayForecasts);

                    Log.d(TAG, "Weather info " + weatherInfoBuilder.build().toString());
                }
            }

            @Override
            public void onFailure(Call<WundergroundReponse> call, Throwable t) {
                Log.d(TAG, "Failure " + t.toString());
            }
        });
    }

    @Override
    public void onWeatherServiceProviderChanged(String s) {

    }

    @Override
    public void onWeatherRequestCompleted(int i, WeatherInfo weatherInfo) {
        switch (i) {
            case CMWeatherManager.RequestStatus.COMPLETED:
                Log.d(TAG, "Weather request completed: " + weatherInfo.toString());
                break;
            case CMWeatherManager.RequestStatus.FAILED:
                Log.d(TAG, "Weather request failed!");
                break;
            case CMWeatherManager.RequestStatus.ALREADY_IN_PROGRESS:
                Log.d(TAG, "Weather request already in progress");
                break;
            case CMWeatherManager.RequestStatus.SUBMITTED_TOO_SOON:
                Log.d(TAG, "Weather request submitted too soon");
                break;
            case CMWeatherManager.RequestStatus.NO_MATCH_FOUND:
                Log.d(TAG, "Weather request match not found");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Requesting weather by location " + location);
        if (mDirectRequest) {
            Call<WundergroundReponse> wundergroundCall =
                    mWundergroundServiceManager.query(location.getLatitude(),
                    location.getLongitude(), Feature.conditions, Feature.forecast);

            wundergroundCall.enqueue(new Callback<WundergroundReponse>() {
                @Override
                public void onResponse(Call<WundergroundReponse> call, Response<WundergroundReponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Received response:\n" + response.body().toString());
                        WundergroundReponse wundergroundReponse = response.body();

                        if (wundergroundReponse == null) {
                            Log.d(TAG, "Null wu reponse, return");
                            return;
                        }

                        CurrentObservationResponse currentObservationResponse =
                                wundergroundReponse.getCurrentObservation();

                        if (currentObservationResponse == null) {
                            Log.d(TAG, "Null co reponse, return");
                            return;
                        }

                        WeatherInfo.Builder weatherInfoBuilder =
                                new WeatherInfo.Builder(
                                        currentObservationResponse.getDisplayLocation().getCity(),
                                        currentObservationResponse.getTempF(),
                                        WeatherContract.WeatherColumns.TempUnit.FAHRENHEIT);

                        weatherInfoBuilder.setWeatherCondition(
                                WeatherContract.WeatherColumns.WeatherCode.CLOUDY);

                        DisplayLocationResponse displayLocationResponse =
                                currentObservationResponse.getDisplayLocation();

                        if (displayLocationResponse == null) {
                            Log.d(TAG, "Null dl reponse, return");
                            return;
                        }

                        // Set humidity
                        weatherInfoBuilder.setHumidity(currentObservationResponse.getHumidity()
                                .floatValue());

                        // Set wind arguments
                        weatherInfoBuilder.setWind(
                                currentObservationResponse.getWindMph().floatValue(),
                                currentObservationResponse.getWindDegrees().floatValue(),
                                WeatherContract.WeatherColumns.WindSpeedUnit.MPH);

                        ForecastResponse forecastResponse =
                                wundergroundReponse.getForecast();

                        if (forecastResponse == null) {
                            Log.d(TAG, "Null fc reponse, return");
                            return;
                        }

                        SimpleForecastResponse simpleForecastResponse =
                                forecastResponse.getSimpleForecast();

                        if (simpleForecastResponse == null) {
                            Log.d(TAG, "Null sf reponse, return");
                            return;
                        }

                        ArrayList<WeatherInfo.DayForecast> dayForecasts =
                                ConverterUtils.convertSimpleFCToDayForcast(
                                simpleForecastResponse.getForecastDay());
                        weatherInfoBuilder.setForecast(dayForecasts);

                        Log.d(TAG, "Weather info " + weatherInfoBuilder.build().toString());
                    } else {
                        Log.d(TAG, "Response " + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<WundergroundReponse> call, Throwable t) {
                    Log.d(TAG, "Failure " + t.toString());
                }
            });
        } else {
            mWeatherManager.requestWeatherUpdate(location, this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onLookupCityRequestCompleted(int state, List<WeatherLocation> arrayList) {
        Log.d(TAG, "Received disambiguation:");
        if (state == CMWeatherManager.RequestStatus.COMPLETED) {
            for (WeatherLocation weatherLocation : arrayList) {
                Log.d(TAG, "Weather location: " + weatherLocation);
            }
        } else {
            Log.d(TAG, "Received state " + state);
        }
    }
}
