package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class SignUpData {
    @SerializedName("userName")
    private String userName;

    @SerializedName("userId")
    private String userId;

    @SerializedName("userPhone")
    private String userPhone;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("userPW")
    private String userPW;

    public SignUpData(String userId, String phone, String email, String userPW, String name) {
        this.userName = name;
        this.userId = userId;
        this.userPW = userPW;
        this.userPhone = phone;
        this.userEmail = email;
    }
}
