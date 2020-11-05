package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class CreateDailyWorkData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("groupName")
    private String groupName;

    @SerializedName("dailyWorkName")
    private String dailyWorkName;

    @SerializedName("dailyWorkDesc")
    private String dailyWorkDesc;

    @SerializedName("dailyWorkTime")
    private  String dailyWorkTime;

    @SerializedName("dailyWorkAlarm")
    private String dailyWorkAlarm;

    public  CreateDailyWorkData(String userId, String groupName, String dailyWorkName, String dailyWorkDesc, String dailyWorkTime, String dailyWorkAlarm){
        this.userId = userId;
        this.groupName = groupName;
        this.dailyWorkName = dailyWorkName;
        this.dailyWorkDesc = dailyWorkDesc;
        this.dailyWorkTime = dailyWorkTime;
        this.dailyWorkAlarm = dailyWorkAlarm;


    }


}
