package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class FollowData {

    @SerializedName("to_id")
    private int follow_to_groupid;

    @SerializedName("from_id")
    private int follow_from_groupid;

    public FollowData(int follow_from_groupid, int follow_to_groupid) {
        this.follow_to_groupid = follow_to_groupid;
        this.follow_from_groupid = follow_from_groupid;
    }

    public int getFollow_to_groupid() {
        return follow_to_groupid;
    }

    public int getFollow_from_groupid() {
        return follow_from_groupid;
    }
}
