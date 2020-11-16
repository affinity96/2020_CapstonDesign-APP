package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class GetLikeData {
    @SerializedName("PostId")
    private int post_id;

    @SerializedName("UserId")
    private String user_id;

    public GetLikeData(int post_id, String user_id) {
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
