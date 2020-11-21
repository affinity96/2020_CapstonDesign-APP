package com.example.homekippa.ui.group;

public class SingleItemComment {

    private String groupCommentProfile;
    private String groupCommentGroupName;
    private String groupCommentNickName;
    private String groupCommentLocation;
    private String groupCommentContent;

    public SingleItemComment(String profile, String groupName, String nickName, String groupLocation, String commentContent) {
        groupCommentProfile = profile;
        groupCommentGroupName = groupName;
        groupCommentNickName = nickName;
        groupCommentLocation = groupLocation;
        groupCommentContent = commentContent;
    }

    public String getGroupCommentProfile() {
        return groupCommentProfile;
    }

    public String getGroupCommentGroupName() {
        return groupCommentGroupName;
    }

    public String getGroupCommentLocation() {
        return groupCommentLocation;
    }

    public String getGroupCommentContent() {
        return groupCommentContent;
    }

    public String getGroupCommentNickName() {
        return groupCommentNickName;
    }
}
