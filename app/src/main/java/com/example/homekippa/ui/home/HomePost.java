package com.example.homekippa.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GetLikeData;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.ListPostAdapter;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import org.w3c.dom.Comment;

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


    ListPostAdapter postAdapter;

    private PostViewModel postViewModel;

    private UserData userData;
    private GroupData groupData;

    /***
     * tab_=F : as Follwer TAb
     * tab_=L : as Location TAb
     */
    private String tab_;
    //    private ArrayList<SingleItemPost> postList = new ArrayList<>();
    private ArrayList<SingleItemPost> postList;
    private ArrayList<GroupData> groupList = new ArrayList<>();
    private List<List<GetLikeData>> likeList = new ArrayList<>();

    private ServiceApi service;

    private RecyclerView listView_posts;

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
        service = RetrofitClient.getClient().create(ServiceApi.class);

        userData = ((MainActivity) getActivity()).getUserData();

        if (!isGroupCreated()) {
            groupData = ((MainActivity) getActivity()).getGroupData();
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home_post, container, false);
        listView_posts = root.findViewById(R.id.listView_HomePost);

        if (!isGroupCreated()) {
            setPostListView(listView_posts);
        }

        return root;
    }


    public void setPostListView(RecyclerView listView) {
        getPostData();

        switch (tab_) {
            case "F":
                Log.d("group body", String.valueOf(groupData.getId()));
                service.getLocationPost().enqueue(new Callback<PostResponse>() {

                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            PostResponse FollwerPosts = response.body();

                            postList = FollwerPosts.getPostData();
                            groupList = FollwerPosts.getGroupData();
                            likeList = FollwerPosts.getLikeData();


                            postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
                            postViewModel.getPostList().setValue(postList);

                            Log.d("like check", groupList.get(0).toString());
                            Log.d("like check", String.valueOf(groupList.get(0).getName()));

//                            for (int i = 0; i < likeList.size(); i++) {
//                                for (int j = 0; j < likeList.get(i).size(); j++) {
//                                    Log.d("like index", likeList.get(i).get(j).toString());
//                                    Log.d("like index", String.valueOf(likeList.get(i).get(j).getPost_id()));
//                                }
//                            }

                            //TODO: Change the sample Image Data!!!!!!
                            //Setting Sample Image Data
                            ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
                            SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
                            post_ImageList.add(postImage);
                            postImage = new SingleItemPostImage(R.drawable.dog_woong);
                            post_ImageList.add(postImage);

                            for (SingleItemPost sit : postList) {
                                sit.setGroupPostImage(post_ImageList);
                            }
                            //Setting Sample Image Data

                            postAdapter = new ListPostAdapter(getActivity(), (ArrayList<SingleItemPost>) postList, groupList, false);
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
            case "L":
//                Log.d("group body", String.valueOf(groupData.getId()));
//                service.getLocationPost().enqueue(new Callback<PostResponse>() {
//
//                    @Override
//                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
//                        if (response.isSuccessful()) {
//                            PostResponse LocationPosts = response.body();
//
//                            postList = LocationPosts.getPostData();
//                            groupList = LocationPosts.getGroupData();
//                            likeList = LocationPosts.getLikeData();
//
//                            postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
//                            postViewModel.getPostList().setValue(postList);
//
//                            Log.d("like check", groupList.get(0).toString());
//                            Log.d("like check", String.valueOf(groupList.get(0).getName()));
//
////                            for (int i = 0; i < likeList.size(); i++) {
////                                for (int j = 0; j < likeList.get(i).size(); j++) {
////                                    Log.d("like index", likeList.get(i).get(j).toString());
////                                    Log.d("like index", String.valueOf(likeList.get(i).get(j).getPost_id()));
////                                }
////                            }
//
//                            //TODO: Change the sample Image Data!!!!!!
//                            //Setting Sample Image Data
//                            ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
//                            SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
//                            post_ImageList.add(postImage);
//                            postImage = new SingleItemPostImage(R.drawable.dog_woong);
//                            post_ImageList.add(postImage);
//
//                            for (SingleItemPost sit : postList) {
//                                sit.setGroupPostImage(post_ImageList);
//                            }
//                            //Setting Sample Image Data
//
//                            postAdapter = new ListPostAdapter(getActivity(), (ArrayList<SingleItemPost>) postList, groupList, false);
//                            listView.setAdapter(postAdapter);
//                            LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
//                            pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                            listView.setLayoutManager(pLayoutManager);
//                            listView.setItemAnimator(new DefaultItemAnimator());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<PostResponse> call, Throwable t) {
//                        Log.d("location", "에러");
//                        Log.e("location", t.getMessage());
//                    }
//                });
//                break;
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

    public boolean isGroupCreated() {
        return ((MainActivity) getActivity()).getGroupData() == null;
    }
}