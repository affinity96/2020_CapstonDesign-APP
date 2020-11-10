package com.example.homekippa.data;
import com.google.gson.annotations.SerializedName;

public class AddPostResponse {
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
