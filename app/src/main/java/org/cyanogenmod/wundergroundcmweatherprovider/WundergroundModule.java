package org.cyanogenmod.wundergroundcmweatherprovider;

import org.cyanogenmod.wundergroundcmweatherprovider.wunderground.WundergroundServiceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by adnan on 4/4/16.
 */
@Module(
        injects = {
                DebugActivity.class,
                WundergroundWeatherProviderService.class
        }
)
public class WundergroundModule {

    private static final String API_KEY = "API_KEY";
    private WundergroundCMApplication mWeatherviewApplication;

    public WundergroundModule(WundergroundCMApplication weatherviewApplication) {
        mWeatherviewApplication = weatherviewApplication;
    }

    @Provides
    @Singleton
    public WundergroundServiceManager providesWundergroundServiceManager() {
        return new WundergroundServiceManager(API_KEY);
    }
}
