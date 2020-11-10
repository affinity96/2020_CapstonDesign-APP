package com.example.homekippa.ui.home;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.ListPostAdapter;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePost extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private UserData userData;
    private GroupData groupData;

    /***
     * tab_=F : as Follwer TAb
     * tab_=L : as Location TAb
     */
    private String tab_;
    private ArrayList<SingleItemPost> postList = new ArrayList<>();
    private ArrayList<GroupData> groupList = new ArrayList<>();

    private ServiceApi service;

    private String mParam1;
    private String mParam2;

    //TODO: get parameters deciding the fragment type: eg) post of followers or post of groups nearby
    public HomePost(String tab_) {
        this.tab_ = tab_;
    }

    public HomePost() {
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

        userData = ((MainActivity) getActivity()).getUserData();
        groupData = ((MainActivity) getActivity()).getGroupData();

        service = RetrofitClient.getClient().create(ServiceApi.class);

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

        switch (tab_) {
            case "F":
            case "L":
                service.getLocationPost(groupData.getGroupId()).enqueue(new Callback<PostResponse>() {

                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d("location", "success");
                            PostResponse LocationPosts = response.body();
                            Log.d("location body", LocationPosts.toString());

                            postList = LocationPosts.getPostData();
                            groupList = LocationPosts.getGroupData();

                            ListPostAdapter postAdapter = new ListPostAdapter(getActivity(), postList, groupList, false);
                            listView.setAdapter(postAdapter);
                            LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                            pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            listView.setLayoutManager(pLayoutManager);
                            listView.setItemAnimator(new DefaultItemAnimator());
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        Log.d("location", "에러");
                        Log.e("location", t.getMessage());
                    }
                });
                break;
        }
    }

    //TODO: set HomePostData and ImageData
    private void getPostData() {

        ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
        SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
        post_ImageList.add(postImage);
        postImage = new SingleItemPostImage(R.drawable.dog_woong);
        post_ImageList.add(postImage);

//        SingleItemPost post = new SingleItemPost(R.drawable.dog_woong, "웅이네 집", "경기도 용인시 기흥구 영덕동", "햇살 좋은날!", "미야옹!", post_ImageList);
//        postList.add(post);
//        post = new SingleItemPost(R.drawable.dog_thang, "땡이네 콩 ", "경기도 용인시 기흥구 신갈동 ", "햇살 안좋은날!!", "멍!!", post_ImageList);
//        postList.add(post);
//        post = new SingleItemPost(R.drawable.dog_tan, "웅콩탄멍! ", "경기도 용인시 기흥구 영덕동", "햇살 더 좋은날!", "뀨? !", post_ImageList);
//        postList.add(post);
    }
}