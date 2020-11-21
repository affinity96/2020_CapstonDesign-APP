package com.example.homekippa.ui.group;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleItemPostImage implements Parcelable {
    private String PostImage;

    public SingleItemPostImage(String postImage) {
        PostImage = postImage;
    }

    protected SingleItemPostImage(Parcel in) {
        PostImage = in.readString();
    }

    public static final Creator<SingleItemPostImage> CREATOR = new Creator<SingleItemPostImage>() {
        @Override
        public SingleItemPostImage createFromParcel(Parcel in) {
            return new SingleItemPostImage(in);
        }

        @Override
        public SingleItemPostImage[] newArray(int size) {
            return new SingleItemPostImage[size];
        }
    };

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PostImage);
    }
}
