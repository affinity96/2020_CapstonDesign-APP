package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

import java.security.acl.Group;
import java.util.ArrayList;

public class CommentGetResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("comment")
    private ArrayList<CommentData> comments;

    @SerializedName("users")
    private ArrayList<UserData> users;

    @SerializedName("groups")
    private ArrayList<GroupData> groups;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<CommentData> getComments() {
        return comments;
    }

    public ArrayList<UserData> getUsers() {
        return users;
    }

    public ArrayList<GroupData> getGroups() {
        return groups;
    }
}
