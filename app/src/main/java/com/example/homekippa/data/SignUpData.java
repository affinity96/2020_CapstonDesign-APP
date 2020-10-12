package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class SignUpData {
    @SerializedName("userName")
    private String userName;

    @SerializedName("userAge")
    private String userAge;

    @SerializedName("userIdToken")
    private String userIdToken;

    public SignUpData(String userAge, String userName, String userIdToken) {
        this.userName = userName;
        this.userAge = userAge;
        this.userIdToken = userIdToken;
    }
}
