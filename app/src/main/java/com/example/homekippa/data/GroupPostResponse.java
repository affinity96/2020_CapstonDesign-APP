package com.example.homekippa.data;

import com.example.homekippa.SingleItemPost;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GroupPostResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("postData")
    private ArrayList<SingleItemPost> postData;

    @SerializedName("likeData")
    private List<List<LikeData>> likeData;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<SingleItemPost> getPostData() {
        return postData;
    }

    public List<List<LikeData>> getLikeData() {
        return likeData;
    }
}
