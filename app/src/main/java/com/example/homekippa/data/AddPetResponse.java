package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class AddPetResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("petName")
    private String petName;

    @SerializedName("petGender")
    private String petGender;

    @SerializedName("petSpecies")
    private String petSpecies;

    @SerializedName("petNeutralization")
    private String petNeutralization;

    public int getCode() {
        return code;
    }

    public String getPetName(){return  petName;}

    public String getPetGender(){return  petGender;}

    public String getPetSpecies(){return  petSpecies;}

    public String getPetNeutralization(){return  petNeutralization;}

    public String getMessage() {
        return message;
    }
}
