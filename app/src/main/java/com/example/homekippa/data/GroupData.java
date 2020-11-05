package com.example.homekippa.data;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupData implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }
    public GroupData(Parcel parcel) {
        this.groupId = parcel.readInt();
        this.groupName = parcel.readString();
        this.groupImage = parcel.readString();
        this.groupAddress = parcel.readString();
        this.groupIntro = parcel.readString();
        this.groupBackground = parcel.readString();
        this.groupTag = parcel.readInt();
    }


    public static final Parcelable.Creator<GroupData> CREATOR = new Parcelable.Creator<GroupData>() {
        @Override
        public GroupData createFromParcel(Parcel parcel) {
            return new GroupData(parcel);
        }
        @Override
        public GroupData[] newArray(int size) {
            return new GroupData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.groupId);
        parcel.writeString(this.groupName);
        parcel.writeString(this.groupImage);
        parcel.writeString(this.groupAddress);
        parcel.writeString(this.groupIntro);
        parcel.writeString(this.groupBackground);
        parcel.writeInt(this.groupTag);
    }
}
