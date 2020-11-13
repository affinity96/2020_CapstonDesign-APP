package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class LikeData {
    @SerializedName("PostId")
    private int post_id;

    @SerializedName("UserId")
    private String user_id;

    @SerializedName("isLiked")
    private boolean isLiked;

    public LikeData(int post_id, String user_id, boolean isLiked) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.isLiked = isLiked;
    }

}
