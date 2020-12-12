package com.example.homekippa.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {
    @SerializedName("id")
    private final String userId;
    @SerializedName("name")
    private final String userName;
    @SerializedName("group_id")
    private final int groupId;
    @SerializedName("image")
    private final String userImage;
    @SerializedName("birth")
    private final String userBirth;
    @SerializedName("phone")
    private final String userPhone;
    @SerializedName("email")
    private final String userEmail;
    @SerializedName("gender")
    private final int userGender;
    @SerializedName("tokken")
    private final String userTokken;

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

    public int getUserGender(){ return userGender;}

    public String getUserBirth() {
        return userBirth;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserTokken() {
        return userTokken;
    }

    public UserData(String userId, String userName, int groupId, String userImage, String userBirth, String userPhone, String userEmail, int userGender, String userTokken) {
        this.userId = userId;
        this.userName = userName;
        this.groupId = groupId;
        this.userImage = userImage;
        this.userBirth = userBirth;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userGender =userGender;
        this.userTokken = userTokken;
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
        this.userGender = parcel.readInt();
        this.userTokken = parcel.readString();
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
        parcel.writeInt(this.userGender);
        parcel.writeString(this.userTokken);
    }
}
