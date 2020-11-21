package com.example.homekippa.ui.group;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class SingleItemDailyWork implements Parcelable {
    private int id;
    private int group_id;
    private int pet_id;
    private String title;
    private int count;
    private String alarm;
    private String desc;
    private String time;
    private int done;


    public SingleItemDailyWork(int id, int group_id, int pet_id, String title, int count, String alarm, String desc, String time, int done) {
        this.id = id;
        this.group_id = group_id;
        this.pet_id = pet_id;
        this.title = title;
        this.count = count;
        this.alarm = alarm;
        this.desc = desc;
        this.time = time;
        this.done = done;
    }


    protected SingleItemDailyWork(Parcel in) {
        id = in.readInt();
        group_id = in.readInt();
        pet_id = in.readInt();
        title = in.readString();
        count = in.readInt();
        alarm = in.readString();
        desc = in.readString();
        time = in.readString();
        done = in.readInt();
    }

    public static final Creator<SingleItemDailyWork> CREATOR = new Creator<SingleItemDailyWork>() {
        @Override
        public SingleItemDailyWork createFromParcel(Parcel in) {
            return new SingleItemDailyWork(in);
        }

        @Override
        public SingleItemDailyWork[] newArray(int size) {
            return new SingleItemDailyWork[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(group_id);
        dest.writeInt(pet_id);
        dest.writeString(title);
        dest.writeInt(count);
        dest.writeString(alarm);
        dest.writeString(desc);
        dest.writeString(time);
        dest.writeInt(done);

    }


    public int getId() {
        return id;
    }

    public int getGroupId() {
        return group_id;
    }

    public int getPetId() {
        return pet_id;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

    public String getAlarm() {
        return alarm;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public int getDone(){ return done;}



}
