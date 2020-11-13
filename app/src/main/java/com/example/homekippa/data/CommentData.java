package com.example.homekippa.data;

import com.google.gson.annotations.SerializedName;

public class CommentData {
    @SerializedName("id")
    private int id;
    @SerializedName("post_id")
    private int post_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("content")
    private String content;

    @SerializedName("date")
    private String date;

    public CommentData(int postId, String userId, String str_comment, String date_comment) {
        this.post_id = postId;
        this.user_id = userId;
        this.content = str_comment;
        this.date = date_comment;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
