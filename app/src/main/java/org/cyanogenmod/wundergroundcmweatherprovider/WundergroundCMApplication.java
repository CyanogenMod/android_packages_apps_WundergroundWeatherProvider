package org.cyanogenmod.wundergroundcmweatherprovider;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;

/**
 * Created by adnan on 4/4/16.
 */
public class WundergroundCMApplication extends Application {
    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(new WundergroundModule(this));
    }

    public static WundergroundCMApplication get(Context context) {
        return (WundergroundCMApplication) context.getApplicationContext();
    }

    public final void inject(Object object) {
        mObjectGraph.inject(object);
    }
}
