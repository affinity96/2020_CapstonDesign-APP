package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class EditDailyWorkData {

    @SerializedName("id")
    private int id;


    @SerializedName("dailyWorkName")
    private String dailyWorkName;

    @SerializedName("dailyWorkDesc")
    private String dailyWorkDesc;

    @SerializedName("dailyWorkTime")
    private  String dailyWorkTime;

    @SerializedName("dailyWorkAlarm")
    private String dailyWorkAlarm;

    public  EditDailyWorkData(int id, String dailyWorkName, String dailyWorkDesc, String dailyWorkTime, String dailyWorkAlarm){
        this.id = id;

        this.dailyWorkName = dailyWorkName;
        this.dailyWorkDesc = dailyWorkDesc;
        this.dailyWorkTime = dailyWorkTime;
        this.dailyWorkAlarm = dailyWorkAlarm;
    }


}
