package org.cyanogenmod.wundergroundcmweatherprovider.wunderground;

public enum Feature implements FeatureParam {
    conditions,
    forecast;

    @Override
    public String toString() {
        return name();
    }
}
