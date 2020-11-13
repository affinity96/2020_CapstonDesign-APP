package com.example.homekippa.ui.group;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.MainActivity;
import com.example.homekippa.PostDetailActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.security.acl.Group;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.MyViewHolder> {
    private ArrayList<SingleItemPost> post_Items = new ArrayList<>();
    private ArrayList<GroupData> groupData = new ArrayList<>();
    private UserData userData;

    private Context context;
    private boolean isgroup;

    private ServiceApi service;

    public ListPostAdapter(Context context, ArrayList<SingleItemPost> postItems, ArrayList<GroupData> groupData, boolean isgroup) {
        this.context = context;
        this.post_Items = postItems;
        this.groupData = groupData;
        this.isgroup = isgroup;
    }

    public ListPostAdapter(Context context, ArrayList<SingleItemPost> postItems, GroupData groupData, boolean isGroup) {
        this.context = context;
        this.post_Items = postItems;
        this.isgroup = isGroup;
        this.groupData.add(groupData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_post, parent, false);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        userData = ((MainActivity) context).getUserData();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setPostData(holder, position);

    }

    private void setPostData(MyViewHolder holder, int position) {

        SingleItemPost post = post_Items.get(position);
        GroupData group;
        if (isgroup) {
            group = groupData.get(0);
            Log.d("group name", group.getName());
        } else {
            group = groupData.get(position);
            Log.d("group position", String.valueOf(position));
//            Log.d("group name", group.getGroupName());
        }

//        Glide.with(context).load(R.drawable.dog_woong).circleCrop().into(holder.postGroupProfile);
//        holder.postGroupProfile.setImageResource(post.getGroupPostProfile());
//        holder.postGroupName.setText(post.getGroup_id());
//        holder.postGroupLocation.setText(post.getGroupPostLocation());

        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postCommentNum.setText(String.valueOf(post.getCommentNum()));
        holder.postLikedNum.setText(String.valueOf(post.getLikeNum()));
        holder.postGroupName.setText(group.getName());
        holder.postGroupAddress.setText(group.getAddress());
        ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();

        SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
        post_ImageList.add(postImage);
        postImage = new SingleItemPostImage(R.drawable.dog_woong);
        post_ImageList.add(postImage);
        post_Items.get(position).setGroupPostImage(post_ImageList);
        setPostImageAdapter(holder, post.getGroupPostImage());

    }

    private void setPostImageAdapter(MyViewHolder holder, ArrayList<SingleItemPostImage> postImageList) {
        ListPostImageAdapter adapter = new ListPostImageAdapter(postImageList);
        holder.recyclerView_postImages.setLayoutManager(new LinearLayoutManager(context
                , LinearLayoutManager.HORIZONTAL
                , false));
        holder.recyclerView_postImages.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return post_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //        ImageView postGroupProfile;
        TextView postGroupName;
        TextView postGroupAddress;
        TextView postTitle;
        TextView postContent;
        TextView postLikedNum;
        TextView postCommentNum;
        RecyclerView recyclerView_postImages;

        ImageView postLikeImage;
        ImageView postCommentImage;

        MyViewHolder(View view) {
            super(view);
//            postGroupProfile = (ImageView) view.findViewById(R.id.imageView_PostGroupProfile);
            postGroupName = (TextView) view.findViewById(R.id.textView__PostGroupName);
            postGroupAddress = (TextView) view.findViewById(R.id.textView__PostGroupLocation);
            postTitle = (TextView) view.findViewById(R.id.textView_PostTitle);
            postContent = (TextView) view.findViewById(R.id.textView_PostContent);
            postLikedNum = (TextView) view.findViewById(R.id.textView_PostLikedNum);
            postCommentNum = (TextView) view.findViewById(R.id.textView_PostCommentNum);
            recyclerView_postImages = (RecyclerView) view.findViewById(R.id.listview_PostImages);
            postLikeImage = (ImageView) view.findViewById(R.id.imageView_PostLiked);
            postCommentImage = (ImageView) view.findViewById(R.id.imageView_PostComment);

            //각 게시글(PostListItem) 클릭
            postCommentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    SingleItemPost post = post_Items.get(getAdapterPosition());
                    GroupData group;

                    if (isgroup) {
                        group = groupData.get(0);
                    } else {
                        group = groupData.get(getAdapterPosition());
                        Log.d("group name", group.getName());
                    }

                    for (SingleItemPostImage sit : post.getGroupPostImage()) {
                        Log.d("ListPostAdatper, set", String.valueOf(sit.getPostImageId()));
                    }


                    intent.putExtra("post", post);
                    intent.putExtra("group", group);
                    context.startActivity(intent);
                }
            });

            postLikeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleItemPost post = post_Items.get(getAdapterPosition());
                    if (!v.isActivated()) {
                        postLikedNum.setText(String.valueOf(post.getLikeNum() + 1));
                        post.setLikeNum(post.getLikeNum() + 1);
                    } else {
                        postLikedNum.setText(String.valueOf(post.getLikeNum() - 1));
                        post.setLikeNum(post.getLikeNum() - 1);
                    }
                    v.setActivated(!v.isActivated());
                    Log.d("liked bool", String.valueOf(v.isActivated()));
                    LikeData likeData = new LikeData(post.getPostId(), userData.getUserId(), v.isActivated());

                    service.setLike(likeData).enqueue(new Callback<LikeResponse>() {
                        @Override
                        public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                            if (response.code() == 200) {
                                Log.d("like", "success");
                            }
                        }

                        @Override
                        public void onFailure(Call<LikeResponse> call, Throwable t) {
                            Log.d("like", "fail");

                        }
                    });


                    //TODO: like num update!

                }
            });

        }
    }
}