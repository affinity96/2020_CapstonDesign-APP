package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class GetFollowData {

    @SerializedName("follower")
    private List<Integer> followerList;

    @SerializedName("following")
    private List<Integer> followingList;

    public List<Integer> getFollowerList() {
        return followerList;
    }

    public List<Integer> getFollowingList() {
        return followingList;
    }


}
