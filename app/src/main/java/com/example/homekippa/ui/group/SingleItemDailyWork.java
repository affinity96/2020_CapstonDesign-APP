package com.example.homekippa.ui.group;

public class SingleItemDailyWork {
    private String workName;
    private String workAlarm;
    private String workDoneTime;
    private String workWhoDid;
    private int workImage;

    public SingleItemDailyWork(String name, String alarm, String donetime, String person, int imageID) {
        workName = name;
        workAlarm = alarm;
        workDoneTime = donetime;
        workWhoDid = person;
        workImage=imageID;
    }

    public String getWorkName() {
        return workName;
    }

    public String getWorkAlarm() {
        return workAlarm;
    }

    public String getWorkDoneTime() {
        return workDoneTime;
    }

    public String getWorkWhoDid() {
        return workWhoDid;
    }

    public int getWorkImage() {
        return workImage;
    }
}
