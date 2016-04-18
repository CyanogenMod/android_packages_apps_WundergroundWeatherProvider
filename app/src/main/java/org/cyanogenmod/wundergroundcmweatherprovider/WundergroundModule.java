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
import android.content.SharedPreferences;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.WundergroundServiceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                DebugActivity.class,
                WundergroundWeatherProviderService.class,
                WUBasePreferenceActivity.class
        }
)
public class WundergroundModule {

    public static final String SHARED_PREFS_KEY = "WU_SHARED_PREFS";
    public static final String API_KEY = "API_KEY";
    public static final String API_KEY_VERIFIED = "API_KEY_VERIFIED";
    private WundergroundCMApplication mWeatherviewApplication;

    public WundergroundModule(WundergroundCMApplication weatherviewApplication) {
        mWeatherviewApplication = weatherviewApplication;
    }

    @Provides
    @Singleton
    public WundergroundServiceManager providesWundergroundServiceManager() {
        SharedPreferences sharedPreferences = mWeatherviewApplication.getSharedPreferences(
                SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        String apikey = sharedPreferences.getString(API_KEY, null);
        return new WundergroundServiceManager(apikey);
    }
}
