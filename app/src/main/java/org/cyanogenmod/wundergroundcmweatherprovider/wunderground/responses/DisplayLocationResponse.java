package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses;

import java.io.Serializable;

public class DisplayLocationResponse implements Serializable {
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
