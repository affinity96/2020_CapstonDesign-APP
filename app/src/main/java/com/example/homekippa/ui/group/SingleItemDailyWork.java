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
    private String done_time;
    private String done_user_id;
    private String done_user_image;

    public SingleItemDailyWork(int id, int group_id, int pet_id, String title, int count, String alarm, String desc, String time, int done, String done_time, String done_user_id, String done_user_image) {
        this.id = id;
        this.group_id = group_id;
        this.pet_id = pet_id;
        this.title = title;
        this.count = count;
        this.alarm = alarm;
        this.desc = desc;
        this.time = time;
        this.done = done;
        this.done_time = done_time;
        this.done_user_id = done_user_id;
        this.done_user_image = done_user_image;
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
        done_time = in.readString();
        done_user_id = in.readString();
        done_user_image = in.readString();
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
        dest.writeString(done_time);
        dest.writeString(done_user_id);
        dest.writeString(done_user_image);
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

    public String getDone_time(){return done_time;}

    public String getDone_user_id() {
        return done_user_id;
    }

    public String getDone_user_image() {
        return done_user_image;
    }

    public void setDone_user_image(String url){
        this.done_user_image=url;
    }
}
