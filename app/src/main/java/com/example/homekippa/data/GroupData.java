package com.example.homekippa.data;

public class GroupData {
    private final int groupId;
    private final String groupName;
    private final String groupImage;
    private final String groupAddress;
    private final String groupIntro;
    private final String groupBackground;
    private final int groupTag;

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public String getGroupAddress() {
        return groupAddress;
    }

    public String getGroupIntro() {
        return groupIntro;
    }

    public String getGroupBackground() {
        return groupBackground;
    }

    public int getGroupTag() {
        return groupTag;
    }

    public GroupData(int groupId, String groupName, String groupImage, String groupAddress, String groupIntro, String groupBackground, int groupTag) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupAddress = groupAddress;
        this.groupIntro = groupIntro;
        this.groupBackground = groupBackground;
        this.groupTag = groupTag;
    }
}
