package com.example.homekippa.ui.group;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.MainActivity;
import com.example.homekippa.ui.home.PostViewModel;
import com.example.homekippa.ui.home.PostDetailActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.MyViewHolder> {
    private ArrayList<SingleItemPost> post_Items = new ArrayList<>();
    private ArrayList<GroupData> groupData = new ArrayList<>();
    private ArrayList<Boolean> likeCheck = new ArrayList<>();
    private UserData userData;

    private PostViewModel viewModel;

    private Context context;
    private boolean isgroup;

    Intent intent;

    private ServiceApi service;

    public ListPostAdapter(Context context, ArrayList<SingleItemPost> postItems, ArrayList<GroupData> groupData, ArrayList<Boolean> likeCheck, boolean isgroup) {
        this.context = context;
        this.post_Items = postItems;
        this.groupData = groupData;
        this.likeCheck = likeCheck;
        this.isgroup = isgroup;
    }

    public ListPostAdapter(Context context, ArrayList<SingleItemPost> postItems, GroupData groupData, ArrayList<Boolean> likeCheck, boolean isGroup) {
        this.context = context;
        this.post_Items = postItems;
        this.isgroup = isGroup;
        this.likeCheck = likeCheck;
        this.groupData.add(groupData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_post, parent, false);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        userData = ((MainActivity) context).getUserData();

        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PostViewModel.class);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setPostData(holder, position);
        setViewModel(holder, position);
    }

    private void setPostData(MyViewHolder holder, int position) {

        SingleItemPost post = post_Items.get(position);
        GroupData group;
        if (isgroup) {
            group = groupData.get(0);
        } else {
            group = groupData.get(position);
        }

//        Glide.with(context).load(R.drawable.dog_woong).circleCrop().into(holder.postGroupProfile);
//        holder.postGroupProfile.setImageResource(post.getGroupPostProfile());

        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postCommentNum.setText(String.valueOf(post.getCommentNum()));
        holder.postLikedNum.setText(String.valueOf(viewModel.getPostList().getValue().get(position).getLikeNum()));
        holder.postGroupName.setText(group.getName());
        holder.postGroupAddress.setText(group.getAddress());
        setLikeImage(holder, position);

        setClickListenerOnHolder(holder, position);
        setPostImageAdapter(holder, post.getGroupPostImage());

    }

    private void setClickListenerOnHolder(MyViewHolder holder, int position) {
        holder.postCommentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, PostDetailActivity.class);

                SingleItemPost post = post_Items.get(position);
                GroupData group = setGroupData(position);

                intent.putExtra("post", post);
                intent.putExtra("group", group);
                intent.putExtra("user", userData);
                intent.putExtra("isliked", likeCheck.get(position));
                intent.putExtra("pos", position);

                ((Activity) context).startActivity(intent);
            }
        });

        holder.postLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleItemPost post = post_Items.get(position);

                LikeData likeData = new LikeData(post.getPostId(), userData.getUserId(), !v.isActivated());
                service.setLike(likeData).enqueue(new Callback<LikeResponse>() {
                    @Override
                    public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                        if (response.code() == 200) {
                            Log.d("like", "success");
                            if (!v.isActivated()) {
                                Log.d("like", "Increase");
                                PostViewModel.setLiveLikeNum(position, 1);
                                PostViewModel.setLiveLikeCheck(position, true);
                            } else {
                                PostViewModel.setLiveLikeNum(position, -1);
                                PostViewModel.setLiveLikeCheck(position, false);
                            }
//                            v.setActivated(!v.isActivated());
                        }
                    }

                    @Override
                    public void onFailure(Call<LikeResponse> call, Throwable t) {
                        Log.d("like", "fail");
                    }
                });
            }
        });
    }

    private void setViewModel(MyViewHolder holder, int position) {

        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PostViewModel.class);
        viewModel.getPostList().observe((LifecycleOwner) context, new Observer<List<SingleItemPost>>() {
            @Override
            public void onChanged(List<SingleItemPost> singleItemPosts) {
                holder.postCommentNum.setText(String.valueOf(singleItemPosts.get(position).getCommentNum()));
                holder.postLikedNum.setText(String.valueOf(singleItemPosts.get(position).getLikeNum()));
                boolean isliked = viewModel.getLikeCheck().getValue().get(position);
                holder.postLikeImage.setActivated(isliked);
            }
        });
        viewModel.getLikeCheck().observe((LifecycleOwner) context, new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> likecheck) {
                holder.postLikedNum.setText(String.valueOf(viewModel.getPostList().getValue().get(position).getLikeNum()));
                boolean isliked = viewModel.getLikeCheck().getValue().get(position);
                holder.postLikeImage.setActivated(isliked);
            }
        });
    }

    private GroupData setGroupData(int position) {
        GroupData group;
        if (isgroup) {
            group = groupData.get(0);
        } else {
            group = groupData.get(position);
        }
        return group;
    }

    private void setLikeImage(MyViewHolder holder, int position) {
        boolean isliked = viewModel.getLikeCheck().getValue().get(position);
        holder.postLikeImage.setActivated(isliked);

    }

    public void setPostImageAdapter(MyViewHolder holder, ArrayList<SingleItemPostImage> postImageList) {
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

        Button postLikeImage;
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
            postLikeImage = (Button) view.findViewById(R.id.imageView_PostLiked);
            postCommentImage = (ImageView) view.findViewById(R.id.imageView_PostComment);
        }
    }
}