package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class WeatheLocationResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("current_temperature")
    private String current_temperature;

    @SerializedName("current_weather")
    private String current_weather;

    //웹에서 정보를 받아서 바로 값만 받아오기?

    public int getCode() {
        return code;
    }

    public String getCurrent_temperature(){return  current_temperature;}

    public String getCurrent_weather(){return current_weather;}

    public String getMessage() {
        return message;
    }
}
