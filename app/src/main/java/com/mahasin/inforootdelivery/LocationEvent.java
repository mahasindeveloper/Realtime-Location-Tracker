package com.mahasin.inforootdelivery;

public class LocationEvent {
    private String latitude;
    private String longitude;

    public LocationEvent(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
