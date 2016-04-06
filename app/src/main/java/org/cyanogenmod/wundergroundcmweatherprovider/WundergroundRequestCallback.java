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

import android.util.Log;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;

import cyanogenmod.weatherservice.ServiceRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by adnan on 4/6/16.
 */
public class WundergroundRequestCallback implements Callback<WundergroundReponse> {
    private static final String TAG = WundergroundRequestCallback.class.getSimpleName();
    private ServiceRequest mServiceRequest;
    private WundergroundResponseListener mWundergroundResponseListener;

    public WundergroundRequestCallback(ServiceRequest serviceRequest,
            WundergroundResponseListener wundergroundResponseListener) {
        mServiceRequest = serviceRequest;
        mWundergroundResponseListener = wundergroundResponseListener;
    }

    @Override
    public void onResponse(Call<WundergroundReponse> call, Response<WundergroundReponse> response) {
        if (response.isSuccessful()) {
            Log.d(TAG, "Received response:\n" + response.body().toString());
            WundergroundReponse wundergroundReponse = response.body();
            if (wundergroundReponse == null) {
                Log.d(TAG, "Null wu reponse, return");
                mServiceRequest.fail();
                return;
            }
            mWundergroundResponseListener.processWundergroundResponse(
                    wundergroundReponse, mServiceRequest);
        } else {
            Log.d(TAG, "Response " + response.toString());
        }
    }

    @Override
    public void onFailure(Call<WundergroundReponse> call, Throwable t) {
        Log.d(TAG, "Failure " + t.toString());
        mServiceRequest.fail();
    }
}
