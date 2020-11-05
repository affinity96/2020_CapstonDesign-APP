package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class AddPetData {

    @SerializedName("petNum")
    private String petNum;



//    @SerializedName("file")
//    private File imgFile;

    public AddPetData(String petNum){
        this.petNum = petNum;
    }
}
