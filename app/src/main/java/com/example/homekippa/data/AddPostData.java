package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class AddPostData {
    @SerializedName("GroupId")
    private int GroupId;

    @SerializedName("UserId")
    private String UserId;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;
    @SerializedName("area")
    private String area;
    public AddPostData(int GroupId, String UserId, String title, String content, String area) {
        this.GroupId = GroupId;
        this.UserId = UserId;
        this.title = title;
        this.content = content;
        this.area = area;
    }

}
