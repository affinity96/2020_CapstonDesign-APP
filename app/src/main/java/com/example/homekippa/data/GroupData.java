package com.example.homekippa.data;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupData implements Parcelable {
    private final int id;
    private final String name;
    private final String image;
    private final String address;
    private final String introduction;
    private final String background;
    private final int tag;
    private final String area;

    public GroupData(int id, String name, String image, String address, String introduction, String background, int tag, String area) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.address = address;
        this.introduction = introduction;
        this.background = background;
        this.tag = tag;
        this.area = area;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public GroupData(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.image = parcel.readString();
        this.address = parcel.readString();
        this.introduction = parcel.readString();
        this.background = parcel.readString();
        this.tag = parcel.readInt();
        this.area = parcel.readString();
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
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.image);
        parcel.writeString(this.address);
        parcel.writeString(this.introduction);
        parcel.writeString(this.background);
        parcel.writeInt(this.tag);
        parcel.writeString(this.area);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getBackground() {
        return background;
    }

    public int getTag() {
        return tag;
    }

    public String getArea() {
        return area;
    }

}
