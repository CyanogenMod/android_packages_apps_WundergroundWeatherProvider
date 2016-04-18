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

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.Feature;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.WundergroundServiceManager;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.CurrentObservationResponse;
import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses.WundergroundReponse;

import javax.inject.Inject;

import cyanogenmod.weather.WeatherLocation;
import retrofit2.Call;
import retrofit2.Response;

public class WUBasePreferenceActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String CREATE_ACCOUNT_KEY = "create_account";
    private static final String API_KEY_KEY = "api_key";

    private static final String WU_CREATE_ACCOUNT_URL =
            "https://www.wunderground.com/weather/api/d/login.html";

    private static final int VERIFY_API_KEY = 0;
    private static final int UPDATE_SUMMARIES = 1;

    private Preference mCreateAccountPreference;
    private EditTextPreference mApiKeyPreference;

    @Inject
    WundergroundServiceManager mWundergroundServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WundergroundCMApplication.get(this).inject(this);
        addPreferencesFromResource(R.xml.preferences);

        mCreateAccountPreference = findPreference(CREATE_ACCOUNT_KEY);
        mApiKeyPreference = (EditTextPreference) findPreference(API_KEY_KEY);
        mApiKeyPreference.setOnPreferenceChangeListener(this);
        mCreateAccountPreference.setOnPreferenceClickListener(this);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getSharedPreferences().contains(WundergroundModule.API_KEY_VERIFIED)) {
            updateSummaries();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case API_KEY_KEY:
                Editable editText = mApiKeyPreference.getEditText().getText();
                if (editText != null) {
                    String text = editText.toString();
                    SharedPreferences.Editor editor = getSharedPreferences().edit();
                    editor.putString(WundergroundModule.API_KEY, text);
                    editor.commit();

                    Message verifyApiKeyMessage = mHandler.obtainMessage();
                    verifyApiKeyMessage.sendToTarget();
                }
                return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case CREATE_ACCOUNT_KEY:
                Intent createAccountIntent = new Intent();
                createAccountIntent.setAction(Intent.ACTION_VIEW);
                createAccountIntent.setData(Uri.parse(WU_CREATE_ACCOUNT_URL));
                try {
                    startActivity(createAccountIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
        }
        return false;
    }

    private final NonLeakyMessageHandler mHandler = new NonLeakyMessageHandler(this);

    private static class NonLeakyMessageHandler extends
            WeakReferenceHandler<WUBasePreferenceActivity> {

        public NonLeakyMessageHandler(WUBasePreferenceActivity reference) {
            super(reference);
        }

        @Override
        protected void handleMessage(WUBasePreferenceActivity reference, Message msg) {
            switch (msg.what) {
                case VERIFY_API_KEY:
                    reference.verifyReceivedWeatherInfoByPostalCode();
                    break;
                case UPDATE_SUMMARIES:
                    reference.updateSummaries();
                    break;
            }
        }
    }

    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences(WundergroundModule.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }

    private void updateSummaries() {
        final SharedPreferences sharedPreferences = getSharedPreferences();
        mApiKeyPreference.setSummary(
                sharedPreferences.getBoolean(WundergroundModule.API_KEY_VERIFIED, false) ?
                        getString(R.string.authentication_preference_api_key_verified) :
                        getString(R.string.authentication_preference_api_key_not_verified));
    }

    private void verifyReceivedWeatherInfoByPostalCode() {
        WeatherLocation weatherLocation = new WeatherLocation.Builder("Seattle")
                .setPostalCode("98121")
                .setCountry("US")
                .setState("WA")
                .build();

        Call<WundergroundReponse> wundergroundCall =
                mWundergroundServiceManager.query(weatherLocation.getPostalCode(),
                        Feature.conditions, Feature.forecast);

        wundergroundCall.enqueue(new retrofit2.Callback<WundergroundReponse>() {
            @Override
            public void onResponse(Call<WundergroundReponse> call,
                                   Response<WundergroundReponse> response) {
                if (response.isSuccessful()) {
                    WundergroundReponse wundergroundReponse = response.body();

                    if (wundergroundReponse == null) {
                        failedVerification();
                        return;
                    }

                    CurrentObservationResponse currentObservationResponse =
                            wundergroundReponse.getCurrentObservation();

                    if (currentObservationResponse == null) {
                        failedVerification();
                    } else {
                        passedVerification();
                    }
                }
            }

            @Override
            public void onFailure(Call<WundergroundReponse> call, Throwable t) {
                failedVerification();
            }
        });
    }

    private void failedVerification() {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(WundergroundModule.API_KEY_VERIFIED, false);
        editor.apply();
        update();
    }

    private void passedVerification() {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(WundergroundModule.API_KEY_VERIFIED, true);
        editor.apply();
        update();
    }

    private void update() {
        Message message = mHandler.obtainMessage(UPDATE_SUMMARIES);
        message.sendToTarget();
    }
}
