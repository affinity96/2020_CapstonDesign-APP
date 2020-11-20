package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class WeatherLocationData {

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    public WeatherLocationData(Double lat , Double lon){
        this.lat = lat;
        this.lon = lon;
    }
}
