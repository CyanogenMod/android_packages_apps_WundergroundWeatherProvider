package org.cyanogenmod.wundergroundcmweatherprovider.wunderground.responses;

import java.io.Serializable;

/**
 * Created by adnan on 4/5/16.
 */
public class DisplayLocationResponse implements Serializable {
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
