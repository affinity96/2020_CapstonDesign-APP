package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class CreateDailyWorkData {

    @SerializedName("GroupId")
    private int GroupId;

    @SerializedName("PetId")
    private int PetId;

    @SerializedName("dailyWorkName")
    private String dailyWorkName;

    @SerializedName("dailyWorkDesc")
    private String dailyWorkDesc;

    @SerializedName("dailyWorkTime")
    private  String dailyWorkTime;

    @SerializedName("dailyWorkAlarm")
    private String dailyWorkAlarm;

    public  CreateDailyWorkData(int GroupId, int PetId, String dailyWorkName, String dailyWorkDesc, String dailyWorkTime, String dailyWorkAlarm){
        this.GroupId = GroupId;
        this.PetId = PetId;
        this.dailyWorkName = dailyWorkName;
        this.dailyWorkDesc = dailyWorkDesc;
        this.dailyWorkTime = dailyWorkTime;
        this.dailyWorkAlarm = dailyWorkAlarm;


    }


}
