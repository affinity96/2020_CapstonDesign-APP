package com.example.homekippa.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {

    private final String userId;
    private final String userName;
    private final int groupId;
    private final String userImage;
    private final String userBirth;
    private final String userPhone;
    private final String userEmail;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public UserData(String userId, String userName, int groupId, String userImage, String userBirth, String userPhone, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.groupId = groupId;
        this.userImage = userImage;
        this.userBirth = userBirth;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public UserData(Parcel parcel) {
        this.userId = parcel.readString();
        this.userName = parcel.readString();
        this.groupId = parcel.readInt();
        this.userImage = parcel.readString();
        this.userBirth = parcel.readString();
        this.userPhone = parcel.readString();
        this.userEmail = parcel.readString();
    }

    // create Parcelable
    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel parcel) {
            return new UserData(parcel);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.userId);
        parcel.writeString(this.userName);
        parcel.writeInt(this.groupId);
        parcel.writeString(this.userImage);
        parcel.writeString(this.userBirth);
        parcel.writeString(this.userPhone);
        parcel.writeString(this.userEmail);
    }
}
