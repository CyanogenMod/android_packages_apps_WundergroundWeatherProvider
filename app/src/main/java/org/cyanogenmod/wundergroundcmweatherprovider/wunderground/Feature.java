package org.cyanogenmod.wundergroundcmweatherprovider.wunderground;

/**
 * Created by adnan on 4/5/16.
 */
public enum Feature implements FeatureParam {
    conditions,
    forecast;

    @Override
    public String toString() {
        return name();
    }
}
