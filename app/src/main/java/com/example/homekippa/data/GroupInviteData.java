package com.example.homekippa.data;

public class GroupInviteData {
    private GroupData from_group;
    private UserData from_user;
    private UserData to_user;

    public GroupData getFrom_group() {
        return from_group;
    }

    public UserData getTo_user() {
        return to_user;
    }

    public UserData getFrom_user() {
        return from_user;
    }

    public GroupInviteData(GroupData from_group, UserData from_user, UserData to_user) {
        this.from_group = from_group;
        this.from_user = from_user;
        this.to_user = to_user;
    }
}
