package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;

import java.util.ArrayList;


public class SingleItemPost extends Fragment implements Parcelable {

    private int groupPostProfile;
    private String groupPostName;
    private String groupPostLocation;
    private String groupPostTitle;
    private String groupPostContent;
    private ArrayList<SingleItemPostImage> groupPostImage;


    public SingleItemPost(int groupProfile, String groupName, String groupLocation, String postTitle, String postContent, ArrayList<SingleItemPostImage> groupPostImage) {
        this.groupPostProfile = groupProfile;
        this.groupPostName = groupName;
        this.groupPostLocation = groupLocation;
        this.groupPostTitle = postTitle;
        this.groupPostContent = postContent;
        this.groupPostImage = groupPostImage;
    }

    protected SingleItemPost(Parcel in) {
        groupPostProfile = in.readInt();
        groupPostName = in.readString();
        groupPostLocation = in.readString();
        groupPostTitle = in.readString();
        groupPostContent = in.readString();
        groupPostImage = in.createTypedArrayList(SingleItemPostImage.CREATOR);
    }

    public static final Creator<SingleItemPost> CREATOR = new Creator<SingleItemPost>() {
        @Override
        public SingleItemPost createFromParcel(Parcel in) {
            return new SingleItemPost(in);
        }

        @Override
        public SingleItemPost[] newArray(int size) {
            return new SingleItemPost[size];
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.listitem_post, container, false);

        // Inflate the layout for this fragment
        return root;
    }

    private void setPostImageListView(RecyclerView listView) {

    }


    //getters and setters
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

    public ArrayList<SingleItemPostImage> getGroupPostImage() {
        return groupPostImage;
    }

    public void setGroupPostImage(ArrayList<SingleItemPostImage> groupPostImage) {
        this.groupPostImage = groupPostImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(groupPostProfile);
        dest.writeString(groupPostName);
        dest.writeString(groupPostLocation);
        dest.writeString(groupPostTitle);
        dest.writeString(groupPostContent);
        dest.writeTypedList(groupPostImage);
    }
}
