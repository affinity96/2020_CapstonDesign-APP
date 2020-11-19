package com.example.homekippa.ui.group;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import okhttp3.ResponseBody;
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
        } else {
            group = groupData.get(position);
        }

//        Glide.with(context).load(R.drawable.dog_woong).circleCrop().into(holder.postGroupProfile);
//        holder.postGroupProfile.setImageResource(post.getGroupPostProfile());

        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postCommentNum.setText(String.valueOf(post.getCommentNum()));
        holder.postLikedNum.setText(String.valueOf(post.getLikeNum()));
        holder.postGroupName.setText(group.getName());
        holder.postGroupAddress.setText(group.getAddress());

        setLikeImage(holder, position);
        setPostImageAdapter(holder, post.getGroupPostImage());
        setClickListenerOnHolder(holder, position);

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
                intent.putExtra("pos", position);

                ((Activity) context).startActivity(intent);
                setViewModelOnComment(holder, position);

            }
        });

        holder.postLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleItemPost post = post_Items.get(position);

                LikeData likeData = new LikeData(post.getPostId(), userData.getUserId(), v.isActivated());
                service.setLike(likeData).enqueue(new Callback<LikeResponse>() {
                    @Override
                    public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                        if (response.code() == 200) {
                            Log.d("like", "success");
                            if (!v.isActivated()) {
                                holder.postLikedNum.setText(String.valueOf(post.getLikeNum() + 1));
                                post.setLikeNum(post.getLikeNum() + 1);
                            } else {
                                holder.postLikedNum.setText(String.valueOf(post.getLikeNum() - 1));
                                post.setLikeNum(post.getLikeNum() - 1);
                            }

                            v.setActivated(!v.isActivated());
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

    private void setViewModelOnComment(MyViewHolder holder, int position) {

        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PostViewModel.class);
        viewModel.getPostList().observe((LifecycleOwner) context, new Observer<List<SingleItemPost>>() {
            @Override
            public void onChanged(List<SingleItemPost> singleItemPosts) {
                holder.postCommentNum.setText(String.valueOf(singleItemPosts.get(position).getCommentNum()));
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
        if (likeCheck.get(position)) holder.postLikeImage.setActivated(true);
        else holder.postLikeImage.setActivated(false);
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

        ImageView postGroupProfile;
        ImageView postLikeImage;
        ImageView postCommentImage;

        MyViewHolder(View view) {
            super(view);
//            postGroupProfile = (ImageView) view.findViewById(R.id.imageView_PostGroupProfile);
//            getGroupProfileImage();
            postGroupName = (TextView) view.findViewById(R.id.textView__PostGroupName);
            postGroupAddress = (TextView) view.findViewById(R.id.textView__PostGroupLocation);
            postTitle = (TextView) view.findViewById(R.id.textView_PostTitle);
            postContent = (TextView) view.findViewById(R.id.textView_PostContent);
            postLikedNum = (TextView) view.findViewById(R.id.textView_PostLikedNum);
            postCommentNum = (TextView) view.findViewById(R.id.textView_PostCommentNum);
            recyclerView_postImages = (RecyclerView) view.findViewById(R.id.listview_PostImages);
            postLikeImage = (ImageView) view.findViewById(R.id.imageView_PostLiked);
            postCommentImage = (ImageView) view.findViewById(R.id.imageView_PostComment);
        }

//        private void getGroupProfileImage() {
//            Log.d("url", groupData.getImage());
//            service.getProfileImage(groupData.getImage()).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    String TAG = "YesGroup";
//                    if (response.isSuccessful()) {
//
//                        Log.d(TAG, "server contacted and has file");
//                        InputStream is = response.body().byteStream();
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//
////                        Glide.with(ListPostAdapter.this).load(bitmap).circleCrop().into(imageView_groupProfile);
//
//                    } else {
//                        Log.d(TAG, "server contact failed");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
////                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
////              Log.e("createGroup error",t.getMessage());
//                    t.printStackTrace();
//                }
//            });
//        }
    }
}