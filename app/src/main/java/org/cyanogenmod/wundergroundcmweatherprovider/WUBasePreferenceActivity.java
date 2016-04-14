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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.Editable;

public class WUBasePreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String CREATE_ACCOUNT_KEY = "create_account";
    private static final String API_KEY_KEY = "api_key";

    private static final String WU_CREATE_ACCOUNT_URL = "https://www.wunderground.com/weather/api/d/login.html";

    private Preference mCreateAccountPreference;
    private EditTextPreference mApiKeyPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WundergroundCMApplication.get(this).inject(this);
        addPreferencesFromResource(R.xml.preferences);

        mCreateAccountPreference = findPreference(CREATE_ACCOUNT_KEY);
        mApiKeyPreference = (EditTextPreference) findPreference(API_KEY_KEY);
        mApiKeyPreference.setOnPreferenceChangeListener(this);
        mCreateAccountPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()) {
            case API_KEY_KEY:
                Editable editText = mApiKeyPreference.getEditText().getText();
                if (editText != null) {
                    String text = editText.toString();
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            WundergroundModule.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(WundergroundModule.API_KEY, text).commit();
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
}
