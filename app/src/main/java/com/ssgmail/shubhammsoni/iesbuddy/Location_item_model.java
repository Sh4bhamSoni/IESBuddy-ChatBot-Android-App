package com.ssgmail.shubhammsoni.iesbuddy;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by shubham soni on 21-Jun-18.
 */

public class Location_item_model {

    String type;
    String desc;
    LatLng latLng;

    public Location_item_model(String type, String desc, LatLng latLng) {
        this.type = type;
        this.desc = desc;
        this.latLng = latLng;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
