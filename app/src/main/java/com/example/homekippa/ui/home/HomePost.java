package com.example.homekippa.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homekippa.R;
import com.example.homekippa.ui.group.ListPostAdapter;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import java.util.ArrayList;

public class HomePost extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<SingleItemPost> postList = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    //TODO: get parameters deciding the fragment type: eg) post of followers or post of groups nearby
    public HomePost() {
        // Required empty public constructor
    }

    public static HomePost newInstance(String param1, String param2) {
        HomePost fragment = new HomePost();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home_post, container, false);
        RecyclerView listView_posts = root.findViewById(R.id.listView_HomePost);

        setPostListView(listView_posts);

        return root;
    }

    private void setPostListView(RecyclerView listView) {
        getPostData();

        ListPostAdapter postAdapter = new ListPostAdapter(getActivity(), postList);
        listView.setAdapter(postAdapter);
        LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(pLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

    //TODO: set HomePostData and ImageData
    private void getPostData() {

        ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
        SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
        post_ImageList.add(postImage);
        postImage = new SingleItemPostImage(R.drawable.dog_woong);
        post_ImageList.add(postImage);

        SingleItemPost post = new SingleItemPost(R.drawable.dog_woong, "웅이네 집", "경기도 용인시 기흥구 영덕동", "햇살 좋은날!", "미야옹!", post_ImageList);
        postList.add(post);
        post = new SingleItemPost(R.drawable.dog_thang, "땡이네 콩 ", "경기도 용인시 기흥구 신갈동 ", "햇살 안좋은날!!", "멍!!", post_ImageList);
        postList.add(post);
        post = new SingleItemPost(R.drawable.dog_tan, "웅콩탄멍! ", "경기도 용인시 기흥구 영덕동", "햇살 더 좋은날!", "뀨? !", post_ImageList);
        postList.add(post);
    }
}