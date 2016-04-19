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

import android.util.Log;

import com.google.common.base.Joiner;

import org.cyanogenmod.wundergroundcmweatherprovider.ApiKeyInterceptor;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WundergroundServiceManager {
    private final static String TAG = "WundergroundManager";
    private final static boolean DEBUG = true;//Log.isLoggable(TAG, Log.VERBOSE);

    private final WundergroundServiceInterface mWundergroundServiceInterface;
    private final ApiKeyInterceptor mApiKeyInterceptor;

    public WundergroundServiceManager(String apiKey) {
        mApiKeyInterceptor = new ApiKeyInterceptor();
        Retrofit baseAdapter = buildRestAdapter(apiKey);
        mWundergroundServiceInterface = baseAdapter.create(WundergroundServiceInterface.class);
    }

    public Call<WundergroundReponse> query(String state, String city,
            FeatureParam... features) {
        return mWundergroundServiceInterface.query(state, city,
                coerceVarArgFeaturesToDelimitedString(features));
    }

    public Call<WundergroundReponse> query(double latitude, double longitude,
            FeatureParam... features) {
        return mWundergroundServiceInterface.query(latitude, longitude,
                coerceVarArgFeaturesToDelimitedString(features));
    }

    public Call<WundergroundReponse> query(String postalCode, FeatureParam... features) {
        return mWundergroundServiceInterface.query(postalCode,
                coerceVarArgFeaturesToDelimitedString(features));
    }

    public Call<WundergroundReponse> lookupCity(String city) {
        return mWundergroundServiceInterface.lookupCity(city);
    }

    public void updateApiKey(String apiKey) {
        mApiKeyInterceptor.setApiKey(apiKey);
    }

    private String coerceVarArgFeaturesToDelimitedString(FeatureParam... featureParams) {
        return Joiner.on('/').join(featureParams);
    }

    private Retrofit buildRestAdapter(String apiKey) {
        final OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (DEBUG) {
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        mApiKeyInterceptor.setApiKey(apiKey);
        builder.addInterceptor(mApiKeyInterceptor);
        final OkHttpClient client = builder.build();

        final String baseUrl = "http://api.wunderground.com/api/" + apiKey + "/";
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

