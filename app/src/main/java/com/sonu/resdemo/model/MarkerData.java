package com.sonu.resdemo.model;

/**
 * Created by SONU SAINI on 5/26/2017.
 */

public class MarkerData {

    public double latitude;

    public double longitude;

    public String titile;

    public MarkerData() {
    }

//    public MarkerData(double latitude, double longitude, String titile) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.titile = titile;
//    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }
}
