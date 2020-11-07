package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;


public class SingleItemPost extends Fragment {

    private int groupPostProfile;
    private String groupPostName;
    private String groupPostLocation;
    private String groupPostTitle;
    private String groupPostContent;


    public SingleItemPost(int groupProfile, String groupName, String groupLocation, String postTitle, String postContent) {
        this.groupPostProfile = groupProfile;
        this.groupPostName = groupName;
        this.groupPostLocation = groupLocation;
        this.groupPostTitle = postTitle;
        this.groupPostContent = postContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.listitem_post, container, false);
        RecyclerView listView_Images = root.findViewById(R.id.listview_PostImages);
//        setPostImageListView(listView_Images);

//        getPostImageData();
//        SingleItemPostImage.ListPostImageAdapter postImageAdapter = new SingleItemPostImage.ListPostImageAdapter(postImageList);

//        LinearLayoutManager iLayoutManager = new LinearLayoutManager(getActivity());
//        iLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        listView_Images.setLayoutManager(iLayoutManager);
//        listView_Images.setItemAnimator(new DefaultItemAnimator());
//        listView_Images.setAdapter(postImageAdapter);

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
}
