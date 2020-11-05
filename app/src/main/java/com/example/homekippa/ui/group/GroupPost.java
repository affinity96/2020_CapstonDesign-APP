package com.example.homekippa.ui.group;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homekippa.AddPostActivity;
import com.example.homekippa.CreateDailyWorkActivity;
import com.example.homekippa.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupPost extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<SingleItemPost> postList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button button_Add_Post;

    public GroupPost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupPost.
     */
    // TODO: Rename and change types and number of parameters
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

        button_Add_Post = root.findViewById(R.id.button_Add_Post);
        button_Add_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        setPostListView(listView_posts);

        // Inflate the layout for this fragment
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

    private void getPostData() {
        SingleItemPost post = new SingleItemPost(R.drawable.dog_woong, "웅이네 집", "경기도 용인시 기흥구 영덕동", "햇살 좋은날!\uD83C\uDF3B", "미야옹!");
        postList.add(post);
        post = new SingleItemPost(R.drawable.dog_thang, "땡이네 콩 ", "경기도 용인시 기흥구 신갈동 ", "햇살 안좋은날!!", "멍!!");
        postList.add(post);
        post = new SingleItemPost(R.drawable.dog_tan, "웅콩탄멍! ", "경기도 용인시 기흥구 영덕동", "햇살 더 좋은날!", "뀨? !");
        postList.add(post);

    }


}