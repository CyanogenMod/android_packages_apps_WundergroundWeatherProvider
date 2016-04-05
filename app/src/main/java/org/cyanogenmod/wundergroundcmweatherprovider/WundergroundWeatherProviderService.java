package org.cyanogenmod.wundergroundcmweatherprovider;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.ConverterUtils;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.Feature;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.WundergroundServiceManager;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.CurrentObservationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.DisplayLocationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.ForecastResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.ForecastDayResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.forecast.SimpleForecastResponse;

import cyanogenmod.providers.WeatherContract;
import cyanogenmod.weather.RequestInfo;
import cyanogenmod.weather.WeatherInfo;
import cyanogenmod.weatherservice.ServiceRequest;
import cyanogenmod.weatherservice.ServiceRequestResult;
import cyanogenmod.weatherservice.WeatherProviderService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by adnan on 4/4/16.
 */
public class WundergroundWeatherProviderService extends WeatherProviderService {
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

    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                case SERVICE_REQUEST_SUBMITTED:
                    processServiceRequestSubmitted((ServiceRequest)inputMessage.obj);
                    break;
                case SERVICE_REQUEST_CANCELLED:
                    break;
            }
        }
    };

    private void processServiceRequestSubmitted(final ServiceRequest serviceRequest) {
        final RequestInfo requestInfo = serviceRequest.getRequestInfo();
        Log.d(TAG, "Received request info: " + requestInfo.toString());

        Location location = requestInfo.getLocation();
        if (location == null) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_HIGH);
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria
                    , false));
        }

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
                        serviceRequest.fail();
                        return;
                    }

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

                    weatherInfoBuilder.setCity(displayLocationResponse.getCity(),
                            displayLocationResponse.getCity());

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
                } else {
                    Log.d(TAG, "Response " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<WundergroundReponse> call, Throwable t) {
                Log.d(TAG, "Failure " + t.toString());
                serviceRequest.fail();
            }
        });
    }
}
