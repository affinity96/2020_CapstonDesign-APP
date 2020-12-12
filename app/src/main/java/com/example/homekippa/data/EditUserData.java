package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class EditUserData {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;




    public  EditUserData(String id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;

    }

}
