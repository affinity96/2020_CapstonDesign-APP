package com.example.homekippa.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.homekippa.ui.group.ListPostAdapter;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

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

    private ListPostAdapter postAdapter;
    private PostViewModel postViewModel;

//    private PostResponse wholePosts;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        if (!isGroupCreated()) {
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

        if (!isGroupCreated()) {
            setPostListView(listView_posts);
        }

        return root;
    }


    public void setPostListView(RecyclerView listView) {
        service.getHomePost(tab_).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse wholePosts = response.body();

                    postList = wholePosts.getPostData();
                    groupList = wholePosts.getGroupData();
                    likeList = wholePosts.getLikeData();

                    postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

//                    setImageData();

                    ArrayList<Boolean> checkLikeList = setLikeData(likeList);
                    postViewModel.getPostList().setValue(postList);
                    postViewModel.getLikeCheck().setValue(checkLikeList);

                    setPostAdapter(listView, checkLikeList);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("homepost", "에러");
                Log.e("homepost", t.getMessage());
            }
        });

    }


    private void setPostAdapter(RecyclerView listView, ArrayList<Boolean> checkLikeList) {
        //Setting Sample Image Data
        postAdapter = new ListPostAdapter(getActivity(), (ArrayList<SingleItemPost>) postViewModel.getPostList().getValue(), groupList, checkLikeList, false);
        listView.setAdapter(postAdapter);

        LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(pLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setImageData() {

        //TODO: Change the sample Image Data!!!!!!
        //Setting Sample Image Data
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
        return ((MainActivity) getActivity()).getGroupData() == null;
    }
}