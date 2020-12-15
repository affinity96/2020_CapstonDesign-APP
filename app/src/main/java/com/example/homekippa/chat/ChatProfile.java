package com.example.homekippa.chat;

public class ChatProfile {
    private String userId;
    private String userName;
    private String message;
    private String image;

    public ChatProfile(String userId, String userName, String message, String image) {
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }
}
