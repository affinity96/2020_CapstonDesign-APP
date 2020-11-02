package com.example.homekippa.data;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class CreateGroupData {
    @SerializedName("groupName")
    private String groupName;

    @SerializedName("groupAddress")
    private  String groupAddress;

    @SerializedName("filePath")
    private Uri imgPath;

//    @SerializedName("file")
//    private File imgFile;

    public  CreateGroupData(String groupName, String groupAddress, Uri imgPath){
        this.groupName = groupName;
        this.groupAddress = groupAddress;
        this.imgPath = imgPath;
//        this.imgFile = imgFile;

    }

}
