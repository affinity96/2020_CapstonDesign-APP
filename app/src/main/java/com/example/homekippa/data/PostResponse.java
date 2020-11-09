package com.example.homekippa.data;

import com.example.homekippa.ui.group.SingleItemPost;
import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("groupData")
    private GroupData groupData;

    @SerializedName("postData")
    private SingleItemPost postData;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
