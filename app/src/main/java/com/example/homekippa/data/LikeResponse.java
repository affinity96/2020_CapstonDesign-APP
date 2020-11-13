package com.example.homekippa.data;

import com.example.homekippa.ui.group.SingleItemPost;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LikeResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
