package com.example.homekippa.data;

public class NotiData {
    private int id;
    private String from_name;
    private String content;
    private String alarm_code;
    private String extra;

    public int getId() {
        return id;
    }

    public String getFrom_Name() {
        return from_name;
    }

    public String getContent() {
        return content;
    }

    public String getAlarm_code() {
        return alarm_code;
    }

    public String getExtra() {
        return extra;
    }

    public NotiData(int id, String from_name, String content, String alarm_code, String extra) {
        this.id = id;
        this.from_name = from_name;
        this.content = content;
        this.alarm_code = alarm_code;
        this.extra = extra;
    }
}
