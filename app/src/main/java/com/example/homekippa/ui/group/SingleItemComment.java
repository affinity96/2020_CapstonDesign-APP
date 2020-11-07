package com.example.homekippa.ui.group;

public class SingleItemComment {

    private int groupCommentProfile;
    private String groupCommentName;
    private String groupCommentLocation;
    private String groupCommentContent;

    public void SingleItemPost(int profile, String groupName, String groupLocation, String commentContent) {
        groupCommentProfile = profile;
        groupCommentName = groupName;
        groupCommentLocation = groupLocation;
        groupCommentContent = commentContent;
    }

    public int getGroupCommentProfile() {
        return groupCommentProfile;
    }

    public String getGroupCommentName() {
        return groupCommentName;
    }

    public String getGroupCommentLocation() {
        return groupCommentLocation;
    }

    public String getGroupCommentContent() {
        return groupCommentContent;
    }
}
