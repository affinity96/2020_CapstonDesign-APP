package com.example.homekippa.data;

import com.example.homekippa.ui.group.SingleItemPost;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("groupData")
    private ArrayList<GroupData> groupData;

    @SerializedName("postData")
    private ArrayList<SingleItemPost> postData;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<GroupData> getGroupData() {
        return groupData;
    }

    public ArrayList<SingleItemPost> getPostData() {
        return postData;
    }
}
