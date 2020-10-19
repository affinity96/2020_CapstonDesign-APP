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

    @SerializedName("userBirth")
    private String userBirth;

    public SignUpData(String userId, String phone, String email, String name, String birth) {
        this.userName = name;
        this.userId = userId;
        this.userPhone = phone;
        this.userEmail = email;
        this.userBirth = birth;
    }
}
