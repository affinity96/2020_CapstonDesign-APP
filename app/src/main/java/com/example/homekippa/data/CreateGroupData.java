package com.example.homekippa.data;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class CreateGroupData {
    @SerializedName("groupNameTag")
    private String groupNameTag;

    @SerializedName("groupAddress")
    private  String groupAddress;

    @SerializedName("filePath")
    private Uri imgPath;

//    @SerializedName("file")
//    private File imgFile;

    public  CreateGroupData(String groupNameTag, String groupAddress, Uri imgPath){
        this.groupNameTag = groupNameTag;
        this.groupAddress = groupAddress;
        this.imgPath = imgPath;
//        this.imgFile = imgFile;

    }

}
