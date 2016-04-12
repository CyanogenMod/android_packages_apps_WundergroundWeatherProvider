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

package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses;

import com.google.gson.annotations.SerializedName;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.citylookup.CityDisambiguationResponse;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import cyanogenmod.weatherservice.ServiceRequest;

public class WundergroundReponse implements Serializable {
    @SerializedName("current_observation")
    private CurrentObservationResponse currentObservationResponse;

    @SerializedName("forecast")
    private ForecastResponse forecastResponse;

    @SerializedName("response")
    private CityDisambiguationResponseInner disambiguationResponse;

    private ServiceRequest serviceRequest;

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

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public List<CityDisambiguationResponse> getCityDisambiguation() {
        return disambiguationResponse != null
                ? disambiguationResponse.cityDisambiguationResponseList : null;
    }

    public void setCityDisambiguationResponseList(
            List<CityDisambiguationResponse> cityDisambiguationResponseList) {
        if (disambiguationResponse != null) {
            disambiguationResponse.cityDisambiguationResponseList = cityDisambiguationResponseList;
        }
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public class CityDisambiguationResponseInner {
        @SerializedName("results")
        private List<CityDisambiguationResponse> cityDisambiguationResponseList;

        @Override
        public String toString() {
                return "CityDisambiguationResponseInner [" + (cityDisambiguationResponseList != null
                    ? Arrays.toString(cityDisambiguationResponseList.toArray()) : "") + " ]";
        }
    }

    @Override
    public String toString() {
        return "WundergroundResponse:\n"
                + "CityDisambiguationResponse: " + disambiguationResponse + "\n"
                + "Forecast: " + forecastResponse + "\n"
                + "Current Observation: " + currentObservationResponse;
    }
}
