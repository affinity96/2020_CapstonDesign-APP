package com.example.homekippa.ui.group;

public class SingleItemPost {

    private int groupPostProfile;
    private String groupPostName;
    private String groupPostLocation;
    private String groupPostTitle;
    private String groupPostContent;

    public SingleItemPost(int groupProfile, String groupName, String groupLocation, String postTitle, String postContent) {
        this.groupPostProfile = groupProfile;
        this.groupPostName = groupName;
        this.groupPostLocation = groupLocation;
        this.groupPostTitle = postTitle;
        this.groupPostContent = postContent;
    }

    public String getGroupPostName() {
        return groupPostName;
    }

    public void setGroupPostName(String groupPostName) {
        this.groupPostName = groupPostName;
    }

    public String getGroupPostLocation() {
        return groupPostLocation;
    }

    public void setGroupPostLocation(String groupPostLocation) {
        this.groupPostLocation = groupPostLocation;
    }

    public String getGroupPostTitle() {
        return groupPostTitle;
    }

    public void setGroupPostTitle(String groupPostTitle) {
        this.groupPostTitle = groupPostTitle;
    }

    public String getGroupPostContent() {
        return groupPostContent;
    }

    public void setGroupPostContent(String groupPostContent) {
        this.groupPostContent = groupPostContent;
    }

    public int getGroupPostProfile() {
        return groupPostProfile;
    }

    public void setGroupPostProfile(int groupPostProfile) {
        this.groupPostProfile = groupPostProfile;
    }
}
