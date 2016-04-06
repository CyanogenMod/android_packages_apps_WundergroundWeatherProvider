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
import android.location.LocationManager;
import android.os.Message;
import android.util.Log;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.ConverterUtils;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.Feature;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.WundergroundServiceManager;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.CurrentObservationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.DisplayLocationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.ForecastResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.SimpleForecastResponse;

import cyanogenmod.providers.WeatherContract;
import cyanogenmod.weather.RequestInfo;
import cyanogenmod.weather.WeatherInfo;
import cyanogenmod.weather.WeatherLocation;
import cyanogenmod.weatherservice.ServiceRequest;
import cyanogenmod.weatherservice.ServiceRequestResult;
import cyanogenmod.weatherservice.WeatherProviderService;

import retrofit2.Call;

import java.util.ArrayList;

import javax.inject.Inject;

public class WundergroundWeatherProviderService extends WeatherProviderService
        implements WundergroundResponseListener {

    private static final String TAG = WundergroundWeatherProviderService.class.getSimpleName();
    private static final int SERVICE_REQUEST_CANCELLED = -1;
    private static final int SERVICE_REQUEST_SUBMITTED = 0;

    @Inject
    public WundergroundServiceManager mWundergroundServiceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        WundergroundCMApplication.get(this).inject(this);
    }

    @Override
    protected void onRequestSubmitted(final ServiceRequest serviceRequest) {
        Log.d(TAG, "Received service request: " + serviceRequest.toString());
        Message request = mHandler.obtainMessage(SERVICE_REQUEST_SUBMITTED, serviceRequest);
        request.sendToTarget();
    }

    @Override
    protected void onRequestCancelled(ServiceRequest serviceRequest) {
        Log.d(TAG, "Received service request cancelled: " + serviceRequest.toString());
        Message request = mHandler.obtainMessage(SERVICE_REQUEST_CANCELLED, serviceRequest);
        request.sendToTarget();
    }

    private final NonLeakyMessageHandler mHandler = new NonLeakyMessageHandler(this);

    private static class NonLeakyMessageHandler
            extends WeakReferenceHandler<WundergroundWeatherProviderService> {
        public NonLeakyMessageHandler(WundergroundWeatherProviderService reference) {
            super(reference);
        }

        @Override
        protected void handleMessage(WundergroundWeatherProviderService reference,
                Message inputMessage) {
            ServiceRequest serviceRequest = (ServiceRequest)inputMessage.obj;
            switch (inputMessage.what) {
                case SERVICE_REQUEST_SUBMITTED:
                    RequestInfo requestInfo = serviceRequest.getRequestInfo();
                    switch (requestInfo.getRequestType()) {
                        case RequestInfo.TYPE_WEATHER_LOCATION_REQ:
                        case RequestInfo.TYPE_GEO_LOCATION_REQ:
                            reference.handleWeatherRequest(serviceRequest);
                            break;
                        default:
                            //Don't support anything else, fail.
                            serviceRequest.fail();
                            break;
                    }
                    break;
                case SERVICE_REQUEST_CANCELLED:
                    //TODO; Implement
                    break;
                default:
                    //Don't support anything else, fail.
                    if (serviceRequest != null) {
                        serviceRequest.fail();
                    }
            }
        }
    }

    private void handleWeatherRequest(final ServiceRequest serviceRequest) {
        final RequestInfo requestInfo = serviceRequest.getRequestInfo();
        Log.d(TAG, "Received weather request info: " + requestInfo.toString());

        if (requestInfo.getRequestType() == RequestInfo.TYPE_GEO_LOCATION_REQ) {
            Location location = requestInfo.getLocation();
            if (location == null) {
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_HIGH);
                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(
                        criteria, false));
            }
            handleRequestByGeoLocation(location, serviceRequest);
        } else {
            WeatherLocation weatherLocation = requestInfo.getWeatherLocation();
            handleRequestByWeatherLocation(weatherLocation, serviceRequest);
        }
    }

    /**
     * Enqueue request by geolocation (lat/long)
     */
    private void handleRequestByGeoLocation(Location location, final ServiceRequest serviceRequest) {
        Call<WundergroundReponse> wundergroundCall =
                mWundergroundServiceManager.query(location.getLatitude(),
                        location.getLongitude(), Feature.conditions, Feature.forecast);
        wundergroundCall.enqueue(new WundergroundRequestCallback(serviceRequest, this));
    }

    /**
     * Enqueue request by weatherlocation
     */
    private void handleRequestByWeatherLocation(WeatherLocation weatherLocation,
            final ServiceRequest serviceRequest) {
        Call<WundergroundReponse> wundergroundCall =
                mWundergroundServiceManager.query(
                        /** todo: ADD STATE TO WEATHER LOCATION API */ null,
                        weatherLocation.getCity(), Feature.conditions, Feature.forecast);
        wundergroundCall.enqueue(new WundergroundRequestCallback(serviceRequest, this));
    }

    @Override
    public void processWundergroundResponse(WundergroundReponse wundergroundReponse,
            final ServiceRequest serviceRequest) {

        CurrentObservationResponse currentObservationResponse =
                wundergroundReponse.getCurrentObservation();

        if (currentObservationResponse == null) {
            Log.d(TAG, "Null co reponse, return");
            serviceRequest.fail();
            return;
        }

        WeatherInfo.Builder weatherInfoBuilder =
                new WeatherInfo.Builder(System.currentTimeMillis());

        weatherInfoBuilder.setTemperature(currentObservationResponse.getTempF()
                        .floatValue(),
                WeatherContract.WeatherColumns.TempUnit.FAHRENHEIT);

        weatherInfoBuilder.setWeatherCondition(
                WeatherContract.WeatherColumns.WeatherCode.CLOUDY);

        DisplayLocationResponse displayLocationResponse =
                currentObservationResponse.getDisplayLocation();

        if (displayLocationResponse == null) {
            Log.d(TAG, "Null dl reponse, return");
            return;
        }

        // Set city
        weatherInfoBuilder.setCity(displayLocationResponse.getCity(),
                displayLocationResponse.getCity());

        // Set humidity
        weatherInfoBuilder.setHumidity(currentObservationResponse.getHumidity()
                .floatValue());

        // Set wind arguments
        weatherInfoBuilder.setWind(currentObservationResponse.getWindMph().floatValue(),
                currentObservationResponse.getWindDegrees().floatValue(),
                WeatherContract.WeatherColumns.WindSpeedUnit.MPH);

        ForecastResponse forecastResponse =
                wundergroundReponse.getForecast();

        if (forecastResponse == null) {
            Log.d(TAG, "Null fc reponse, return");
            serviceRequest.fail();
            return;
        }

        SimpleForecastResponse simpleForecastResponse =
                forecastResponse.getSimpleForecast();

        if (simpleForecastResponse == null) {
            Log.d(TAG, "Null sf reponse, return");
            serviceRequest.fail();
            return;
        }

        ArrayList<WeatherInfo.DayForecast> dayForecasts =
                ConverterUtils.convertSimpleFCToDayForcast(
                        simpleForecastResponse.getForecastDay());
        weatherInfoBuilder.setForecast(dayForecasts);

        ServiceRequestResult serviceRequestResult =
                new ServiceRequestResult.Builder()
                        .setWeatherInfo(weatherInfoBuilder.build()).build();
        serviceRequest.complete(serviceRequestResult);
    }
}
