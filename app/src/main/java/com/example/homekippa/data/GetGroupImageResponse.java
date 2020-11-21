package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GetGroupImageResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public static InputStream byteArrayToInputStream(byte[] srcBytes) {
        return new ByteArrayInputStream(srcBytes);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
