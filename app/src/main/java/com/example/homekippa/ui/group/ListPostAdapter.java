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

import java.io.InputStream;
import java.text.ParseException;
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

    private PostViewModel postViewModel;
    private GroupViewModel groupViewModel;

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

        postViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PostViewModel.class);
        groupViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(GroupViewModel.class);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (isgroup) {
            setGroupViewModel(holder, position);
            try {
                setPostData(holder, position);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            setHomeViewModel(holder, position);
            try {
                setPostData(holder, position);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private void setPostData(MyViewHolder holder, int position) throws ParseException {
        SingleItemPost post;
        if (isgroup) {
            post = groupViewModel.getPostList().getValue().get(position);
        } else {
            post = postViewModel.getPostList().getValue().get(position);
        }

        GroupData group;
        if (isgroup) {
            group = groupData.get(0);
        } else {
            group = groupData.get(position);
        }

        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postCommentNum.setText(String.valueOf(post.getCommentNum()));
        if (isgroup) {
            holder.postLikedNum.setText(String.valueOf(groupViewModel.getPostList().getValue().get(position).getLikeNum()));
        } else
            holder.postLikedNum.setText(String.valueOf(postViewModel.getPostList().getValue().get(position).getLikeNum()));

        holder.postGroupName.setText(group.getName());
        holder.postGroupAddress.setText(group.getAddress());
        getProfileImage(holder, group.getImage());

        if (post.getImage() == null) {
            holder.recyclerView_postImages.setVisibility(View.GONE);
        } else {
//            setImageData(post.getImage(), position);
            setPostImageAdapter(holder, post.getGroupPostImage());
        }

//        String pd = post.getDate();
//        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
//        Date postdate = fm.parse(pd);
//        holder.postDate.setText(String.valueOf(postdate));

        setLikeImage(holder, position);
        setClickListenerOnHolder(holder, position);
    }

    private void getProfileImage(MyViewHolder holder, String url) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "ListPostAdapter";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(context).load(bitmap).circleCrop().into(holder.postGroupProfile);
                    holder.postGroupProfile.setImageBitmap(bitmap);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void setImageData(String url, int position) {
        ArrayList<SingleItemPostImage> post_ImageList = new ArrayList<>();
        SingleItemPostImage postImage = new SingleItemPostImage(url);
        post_ImageList.add(postImage);
//        postImage = new SingleItemPostImage(R.drawable.dog_woong);
//        post_ImageList.add(postImage);
//        if (isgroup) {
//            List<SingleItemPost> posts = new ArrayList<>();
//            posts.addAll(groupViewModel.getPostList().getValue());
//            for (SingleItemPost sit : posts) {
//                sit.setGroupPostImage(post_ImageList);
//            }
//            for (SingleItemPost sit : posts) {
//                Log.d("posts group for", sit.getGroupPostImage().toString());
//            }
//            groupViewModel.getPostList().setValue(posts);
//        } else {
//            List<SingleItemPost> posts = new ArrayList<>();
//            posts.addAll(postViewModel.getPostList().getValue());
//            for (SingleItemPost sit : posts) {
//                sit.setGroupPostImage(post_ImageList);
//            }
//            Log.d("pos _for", String.valueOf(position));
//            for (SingleItemPost sit : posts) {
//                Log.d("posts post__ for", sit.getGroupPostImage().toString());
//            }
//            postViewModel.getPostList().setValue(posts);
//        }

    }

    private void setClickListenerOnHolder(MyViewHolder holder, int position) {
        holder.postCommentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, PostDetailActivity.class);

                SingleItemPost post;
                if (isgroup) {
                    post = groupViewModel.getPostList().getValue().get(position);
                    GroupData group = setGroupData(position);
                    intent.putExtra("post", post);
                    intent.putExtra("group", group);
                    intent.putExtra("user", userData);
                    intent.putExtra("isliked", likeCheck.get(position));
                    intent.putExtra("isgroup", isgroup);
                    intent.putExtra("pos", position);
                } else {
                    post = postViewModel.getPostList().getValue().get(position);
                    GroupData group = setGroupData(position);
                    intent.putExtra("post", post);
                    intent.putExtra("group", group);
                    intent.putExtra("user", userData);
                    intent.putExtra("isliked", likeCheck.get(position));
                    intent.putExtra("isgroup", isgroup);
                    intent.putExtra("pos", position);
                }
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
                            if (isgroup) {
                                if (!v.isActivated()) {
                                    Log.d("like", "Increase");
                                    GroupViewModel.setLiveLikeNum(position, 1);
                                    GroupViewModel.setLiveLikeCheck(position, true);
                                } else {
                                    GroupViewModel.setLiveLikeNum(position, -1);
                                    GroupViewModel.setLiveLikeCheck(position, false);
                                }
                            } else {
                                if (!v.isActivated()) {
                                    Log.d("like", "Increase");
                                    PostViewModel.setLiveLikeNum(position, 1);
                                    PostViewModel.setLiveLikeCheck(position, true);
                                } else {
                                    PostViewModel.setLiveLikeNum(position, -1);
                                    PostViewModel.setLiveLikeCheck(position, false);
                                }
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

    private void setHomeViewModel(MyViewHolder holder, int position) {

        postViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PostViewModel.class);

        postViewModel.getPostList().observe((LifecycleOwner) context, new Observer<List<SingleItemPost>>() {
            @Override
            public void onChanged(List<SingleItemPost> singleItemPosts) {
                holder.postCommentNum.setText(String.valueOf(singleItemPosts.get(position).getCommentNum()));
                holder.postLikedNum.setText(String.valueOf(singleItemPosts.get(position).getLikeNum()));
                boolean isliked = postViewModel.getLikeCheck().getValue().get(position);
                holder.postLikeImage.setActivated(isliked);
            }
        });
        postViewModel.getLikeCheck().observe((LifecycleOwner) context, new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> likecheck) {
                holder.postLikedNum.setText(String.valueOf(postViewModel.getPostList().getValue().get(position).getLikeNum()));
                boolean isliked = postViewModel.getLikeCheck().getValue().get(position);
                holder.postLikeImage.setActivated(isliked);
            }
        });
    }

    private void setGroupViewModel(MyViewHolder holder, int position) {
        groupViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(GroupViewModel.class);
        groupViewModel.getPostList().observe((LifecycleOwner) context, new Observer<List<SingleItemPost>>() {
            @Override
            public void onChanged(List<SingleItemPost> singleItemPosts) {
                holder.postCommentNum.setText(String.valueOf(singleItemPosts.get(position).getCommentNum()));
                holder.postLikedNum.setText(String.valueOf(singleItemPosts.get(position).getLikeNum()));
                Log.d("like", groupViewModel.getLikeCheck().getValue().get(position).toString());
                Log.d("like", String.valueOf(position));
                boolean isliked = groupViewModel.getLikeCheck().getValue().get(position);
                holder.postLikeImage.setActivated(isliked);
            }
        });
        groupViewModel.getLikeCheck().observe((LifecycleOwner) context, new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> likecheck) {
                holder.postLikedNum.setText(String.valueOf(groupViewModel.getPostList().getValue().get(position).getLikeNum()));
                boolean isliked = groupViewModel.getLikeCheck().getValue().get(position);
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
        boolean isliked;
        if (isgroup) isliked = groupViewModel.getLikeCheck().getValue().get(position);
        else isliked = postViewModel.getLikeCheck().getValue().get(position);

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
        TextView postGroupName;
        TextView postGroupAddress;
        TextView postTitle;
        TextView postContent;
        TextView postLikedNum;
        TextView postCommentNum;
        TextView postDate;
        RecyclerView recyclerView_postImages;

        ImageView postGroupProfile;
        Button postLikeImage;
        ImageView postCommentImage;

        MyViewHolder(View view) {
            super(view);
            postGroupProfile = (ImageView) view.findViewById(R.id.imageView_PostGroupProfile);
            postGroupName = (TextView) view.findViewById(R.id.textView__PostGroupName);
            postGroupAddress = (TextView) view.findViewById(R.id.textView__PostGroupLocation);
            postTitle = (TextView) view.findViewById(R.id.textView_PostTitle);
            postContent = (TextView) view.findViewById(R.id.textView_PostContent);
            postLikedNum = (TextView) view.findViewById(R.id.textView_PostLikedNum);
            postCommentNum = (TextView) view.findViewById(R.id.textView_PostCommentNum);
            postDate = (TextView) view.findViewById(R.id.textView_PostDate);
            recyclerView_postImages = (RecyclerView) view.findViewById(R.id.listview_PostImages);
            postLikeImage = (Button) view.findViewById(R.id.imageView_PostLiked);
            postCommentImage = (ImageView) view.findViewById(R.id.imageView_PostComment);
        }

    }
}