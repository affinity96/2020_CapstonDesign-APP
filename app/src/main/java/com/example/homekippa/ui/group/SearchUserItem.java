package com.example.homekippa.ui.group;

import android.widget.Button;

public class SearchUserItem {
    private String userImage;
    private String userName;
    private String userDesc;
    private Button inviteButton;

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public Button getInviteButton() {
        return inviteButton;
    }

    public SearchUserItem(String userImage, String userName, String userDesc, Button inviteButton) {
        this.userImage = userImage;
        this.userName = userName;
        this.userDesc = userDesc;
        this.inviteButton = inviteButton;
    }
}
