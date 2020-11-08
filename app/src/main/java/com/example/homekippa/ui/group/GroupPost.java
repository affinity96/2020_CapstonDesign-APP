package com.example.homekippa.ui.group;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.homekippa.AddPostActivity;
import com.example.homekippa.R;

import java.util.ArrayList;
import java.util.List;


public class GroupPost extends Fragment {


    private static final String ARG_PARAM1 = "GroupPost";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button button_Add_Post;
    private ArrayList<SingleItemPost> postList = new ArrayList<>();

    public GroupPost() {
        // Required empty public constructor
    }


    public static GroupPost newInstance(String param1, String param2) {
        GroupPost fragment = new GroupPost();
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

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_group_post, container, false);
        RecyclerView listView_posts = root.findViewById(R.id.listView_GroupPost);

        setPostListView(listView_posts);

        button_Add_Post = root.findViewById(R.id.button_Add_Post);
        button_Add_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

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

    //TODO: set GroupPostData and ImageData
    private void getPostData() {

        ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
        SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_woong);
        post_ImageList.add(postImage);
        postImage = new SingleItemPostImage(R.drawable.base_cover);
        post_ImageList.add(postImage);

        SingleItemPost post = new SingleItemPost(R.drawable.dog_woong, "웅이네 집", "경기도 용인시 기흥구 영덕동", "햇살 좋은날!\uD83C\uDF3B", "미야옹!", post_ImageList);
        postList.add(post);
        post = new SingleItemPost(R.drawable.dog_woong, "웅이네 집", "경기도 용인시 기흥구 영덕동", "아잉!\uD83C\uDF3B", "미야옹!", post_ImageList);
        postList.add(post);
        post = new SingleItemPost(R.drawable.dog_woong, "웅이네 집", "경기도 용인시 기흥구 영덕동", "바봉양!!\uD83C\uDF3B", "미야옹!", post_ImageList);
        postList.add(post);
    }


}