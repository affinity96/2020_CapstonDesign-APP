package com.example.homekippa.data;

public class GroupInviteData {
    private int from_group;
    private String to_user;

    public int getFrom_group() {
        return from_group;
    }

    public String getTo_user() {
        return to_user;
    }

    public GroupInviteData(int from_group, String to_user) {
        this.from_group = from_group;
        this.to_user = to_user;
    }
}
