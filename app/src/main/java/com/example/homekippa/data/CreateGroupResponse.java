package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class CreateGroupResponse {
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

//TODO: 이부분을 잘 모르겠네요w