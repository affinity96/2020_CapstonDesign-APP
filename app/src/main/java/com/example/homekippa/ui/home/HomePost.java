package com.example.homekippa.ui.home;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ListPostAdapter;
import com.example.homekippa.SingleItemPost;
import com.example.homekippa.SingleItemPostImage;
import com.example.homekippa.ui.group.YesGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePost extends Fragment {

    private ServiceApi service;
    /***
     * tab_=F : as Follwer TAb
     * tab_=L : as Location TAb
     */
    private String tab_;

    private UserData userData;
    private GroupData groupData;

    private ArrayList<SingleItemPost> postList;
    private ArrayList<GroupData> groupList = new ArrayList<>();
    private List<List<LikeData>> likeList = new ArrayList<>();
    private RecyclerView listView_posts;
    private ImageView empty_Img;

    private ListPostAdapter postAdapter;
    private FollowViewModel followViewModel;
    private LocationViewModel locationViewModel;

    //TODO: get parameters deciding the fragment type: eg) post of followers or post of groups nearby
    public HomePost(String tab_) {
        this.tab_ = tab_;
    }

    public HomePost() {
    }

    public static HomePost newInstance(String param1, String param2) {
        HomePost fragment = new HomePost();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPostListView(listView_posts);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        followViewModel = new ViewModelProvider(requireActivity()).get(FollowViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

        if (isGroupCreated()) {
            groupData = ((MainActivity) getActivity()).getGroupData();
        }
        userData = ((MainActivity) getActivity()).getUserData();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home_post, container, false);
        listView_posts = root.findViewById(R.id.listView_HomePost);
        empty_Img = root.findViewById(R.id.empty_post);

        if (!isGroupCreated()) {
            setPostListView(listView_posts);
        }
        return root;
    }


    public void setPostListView(RecyclerView listView) {
        if (groupData != null) {
            service.getHomePost(groupData.getId(), tab_, groupData.getArea()).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (response.isSuccessful()) {
                        PostResponse wholePosts = response.body();

                        postList = wholePosts.getPostData();
                        groupList = wholePosts.getGroupData();
                        likeList = wholePosts.getLikeData();

                        ArrayList<Boolean> checkLikeList = setLikeData(likeList);
                        setImageData();

                        if (tab_ == "F") {
                            followViewModel.setPostList(postList);
                            followViewModel.getLikeCheck().setValue(checkLikeList);
                            setPostAdapter(listView, checkLikeList, (ArrayList<SingleItemPost>) followViewModel.getPostList().getValue());
                        } else {
                            locationViewModel.getPostList().setValue(postList);
                            locationViewModel.getLikeCheck().setValue(checkLikeList);
                            setPostAdapter(listView, checkLikeList, (ArrayList<SingleItemPost>) locationViewModel.getPostList().getValue());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    Log.d("homepost", "에러");
                    Log.e("homepost", t.getMessage());
                }
            });
        } else {
            ArrayList<Boolean> checkLikeList = new ArrayList<>();
            if (tab_ == "F") {
                setPostAdapter(listView, checkLikeList, (ArrayList<SingleItemPost>) followViewModel.getPostList().getValue());
            } else {
                setPostAdapter(listView, checkLikeList, (ArrayList<SingleItemPost>) locationViewModel.getPostList().getValue());
            }
        }


    }


    private void setPostAdapter(RecyclerView listView, ArrayList<Boolean> checkLikeList, ArrayList<SingleItemPost> list) {
        //Setting Sample Image Data
        if (checkLikeList.isEmpty()) {
            Log.d("homePost", "noposts");
            listView.setVisibility(View.GONE);
            empty_Img.setVisibility(View.VISIBLE);

        } else {
            listView.setVisibility(View.VISIBLE);
            empty_Img.setVisibility(View.GONE);
            postAdapter = new ListPostAdapter(getActivity(), list, groupList, checkLikeList, false, tab_);
            listView.setAdapter(postAdapter);

            LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
            pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            listView.setLayoutManager(pLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
        }

    }

    private void setImageData() {
        for (SingleItemPost p : postList) {
            ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
            SingleItemPostImage postImage = new SingleItemPostImage(p.getImage());
            post_ImageList.add(postImage);
            p.setGroupPostImage(post_ImageList);
        }
    }

    private ArrayList<Boolean> setLikeData(List<List<LikeData>> likeList) {

        ArrayList<Boolean> checkLike = new ArrayList<Boolean>();

        int i = 0;
        for (List<LikeData> like : likeList) {
            LikeData l = new LikeData(postList.get(i).getPostId(), userData.getUserId());
            i = i + 1;
            if (like.contains(l)) {
                checkLike.add(true);
            } else {
                checkLike.add(false);
            }
        }
        return checkLike;
    }

    public boolean isGroupCreated() {
        return ((MainActivity) getActivity()).getGroupData() != null;
    }
}