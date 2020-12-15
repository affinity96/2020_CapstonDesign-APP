package com.example.homekippa.chat;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatData implements Comparable<ChatData>{
    private String message;
    private String from;
    private Date time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ChatData(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public ChatData(String message, String from, Date time) {
        this.message = message;
        this.from = from;
        this.time = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("from", from);
        return result;
    }

    @Override
    public int compareTo(ChatData chatData) {
        return this.time.compareTo(chatData.time);
    }
}
