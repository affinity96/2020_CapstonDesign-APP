package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class AddPetDesData {

    @SerializedName("petRegNum")
    private String petRegNum;

    @SerializedName("petName")
    private String petName;

    @SerializedName("petSpecies")
    private String petSpecies;

    @SerializedName("petNeutralization")
    private String petNeutralization;

    @SerializedName("petGender")
    private String petGender;

    @SerializedName("GroupId")
    private int groupId;

    @SerializedName("petBirth")
    private String petBirth;

    public AddPetDesData(String petRegNum, String petName, String petSpecies, String petGender, String petNeutralization, int groupId, String petBirth){
        this.petRegNum = petRegNum;
        this.petSpecies = petSpecies;
        this.petName = petName;
        this.petGender = petGender;
        this.petNeutralization = petNeutralization;
        this.groupId = groupId;
        this.petBirth = petBirth;
    }

}
