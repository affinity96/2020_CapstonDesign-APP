package com.example.homekippa.ui.group;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.homekippa.AddPostActivity;
import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupPostResponse;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupPost extends Fragment {


    private static final String ARG_PARAM1 = "GroupPost";
    private static final String ARG_PARAM2 = "param2";
    private ServiceApi service;
    private UserData userData;
    private GroupData groupData;

    private String mParam1;
    private String mParam2;

    private Button button_Add_Post;
    private ArrayList<SingleItemPost> postList = new ArrayList<>();
    private List<List<LikeData>> likeList = new ArrayList<>();

    private RecyclerView listView_posts;
    private TextView textView_groupName;
    private TextView textView_address;

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

        service = RetrofitClient.getClient().create(ServiceApi.class);
        userData = ((MainActivity) getActivity()).getUserData();
        groupData = ((MainActivity) getActivity()).getGroupData();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_group_post, container, false);
        listView_posts = root.findViewById(R.id.listView_GroupPost);
        textView_groupName = root.findViewById(R.id.textView_GroupPostName);
        textView_address = root.findViewById(R.id.textView_GroupPostAddress);

        setGroupView();
        setPostListView(listView_posts);

        button_Add_Post = root.findViewById(R.id.button_Add_Post);
        button_Add_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("groupData", groupData);
                startActivity(intent);
            }
        });

        return root;
    }

    private void setGroupView() {
        //TODO:: add group profile Image
        textView_groupName.setText(groupData.getName());
        textView_address.setText(groupData.getAddress());
    }

    private void setPostListView(RecyclerView listView) {
        service.getGroupPost(groupData.getId()).enqueue(new Callback<GroupPostResponse>() {

            @Override
            public void onResponse(Call<GroupPostResponse> call, Response<GroupPostResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("post", "success");
                    GroupPostResponse groupPostResponse = response.body();

                    postList = groupPostResponse.getPostData();
                    likeList = groupPostResponse.getLikeData();

                    //TODO: Change the sample Image Data!!!!!!
                    setImageData();

                    ArrayList<Boolean> checkLikeList = setLikeData(likeList);
                    setPostAdapter(listView, checkLikeList);

                }
            }

            @Override
            public void onFailure(Call<GroupPostResponse> call, Throwable t) {
                Log.d("post", "에러");
                Log.e("post", t.getMessage());
            }
        });
    }

    private void setImageData() {
        ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
        SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
        post_ImageList.add(postImage);
        postImage = new SingleItemPostImage(R.drawable.dog_woong);
        post_ImageList.add(postImage);
        for (SingleItemPost sit : postList) {
            sit.setGroupPostImage(post_ImageList);
        }
    }

    private void setPostAdapter(RecyclerView listView, ArrayList<Boolean> checkLikeList) {

        ListPostAdapter postAdapter = new ListPostAdapter(getActivity(), postList, groupData, checkLikeList, true);
        LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(pLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(postAdapter);
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
}