package com.example.homekippa.ui.group;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleItemPostImage implements Parcelable {
    private int PostImageId;

    public SingleItemPostImage(int postImageId) {
        PostImageId = postImageId;
    }

    protected SingleItemPostImage(Parcel in) {
        PostImageId = in.readInt();
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

    public int getPostImageId() {
        return PostImageId;
    }

    public void setPostImageId(int postImageId) {
        PostImageId = postImageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PostImageId);
    }
}
