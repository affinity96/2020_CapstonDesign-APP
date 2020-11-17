package com.example.homekippa.data;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class LikeData {
    @SerializedName("post_id")
    private int post_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("isLiked")
    private boolean isLiked;

    public LikeData(int post_id, String user_id, boolean isLiked) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.isLiked = isLiked;
    }

    public LikeData(int post_id, String user_id) {
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "postid=" + post_id +
                ", userid=" + user_id +
                '}';
    }
}
