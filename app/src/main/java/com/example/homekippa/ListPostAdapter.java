package com.example.homekippa;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homekippa.data.DeletePostResponse;
import com.example.homekippa.data.DoneReportsResponse;
import com.example.homekippa.ui.group.GroupFragment;
import com.example.homekippa.ui.group.GroupViewModel;
import com.example.homekippa.ui.group.YesGroup;
import com.example.homekippa.ui.home.PostViewModel;
import com.example.homekippa.ui.home.PostDetailActivity;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private Cache cache;

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

        cache = new Cache(context);
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
        GroupData group;

        if (isgroup) {
            post = groupViewModel.getPostList().getValue().get(position);
            group = groupData.get(0);
        } else {
            post = postViewModel.getPostList().getValue().get(position);
            group = groupData.get(position);
        }


        if (isgroup) {
            holder.postLikedNum.setText(String.valueOf(groupViewModel.getPostList().getValue().get(position).getLikeNum()));
        } else
            holder.postLikedNum.setText(String.valueOf(postViewModel.getPostList().getValue().get(position).getLikeNum()));

        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postCommentNum.setText(String.valueOf(post.getCommentNum()));
        holder.postGroupName.setText(group.getName());
        holder.postGroupAddress.setText(group.getArea());

        if (post.getImage() == null) {
            holder.recyclerView_postImages.setVisibility(View.GONE);
        } else {
            setPostImageAdapter(holder, post.getGroupPostImage());
        }
        setPostDate(holder, position, post);

        getProfileImage(holder, group.getImage());
        setLikeImage(holder, position);
        setClickListenerOnHolder(holder, position);


//        String pd = post.getDate();
//        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
//        Date postdate = fm.parse(pd);
//        holder.postDate.setText(String.valueOf(postdate));

    }

    private void setPostDate(MyViewHolder holder, int position, SingleItemPost post) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String olddate = post.getDate();
        Log.d("date", String.valueOf(olddate));

        Date oldDate = dateFormat.parse(olddate);
        long reqDateTime = oldDate.getTime();

//        Date curDate = new Date();
//        curDate = dateFormat.parse(dateFormat.format(curDate));
//        long curDateTime = curDate.getTime();
//
//        long minute = (curDateTime - reqDateTime) / 60000;
//        Log.d("date", String.valueOf(minute));

        String newdate = new SimpleDateFormat("yyyy-MM-dd").format(oldDate);

        holder.postDate.setText(newdate);
    }

    private void getProfileImage(MyViewHolder holder, String url) {
        String[] w = url.split("/");
        String key = w[w.length - 1];

        Bitmap bit = cache.getBitmapFromCacheDir(key);
        if (bit != null) {
            Glide.with(context).load(bit).circleCrop().into(holder.postGroupProfile);
        } else {
            ImageLoadTask task = new ImageLoadTask(url, holder.postGroupProfile, context, false);
            task.execute();
//            service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    String TAG = "ListPostAdapter";
//                    if (response.isSuccessful()) {
//
//                        Log.d(TAG, "server contacted and has file");
//                        InputStream is = response.body().byteStream();
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//                        if (bitmap != null) {
//                            Glide.with(context).load(bitmap).circleCrop().into(holder.postGroupProfile);
////                            holder.postGroupProfile.setImageBitmap(bitmap);
//                            cache.saveBitmapToJpeg(bitmap, key);
//                        }
//                    } else {
//                        Log.d(TAG, "server contact failed");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
        }
    }

    private void setClickListenerOnHolder(MyViewHolder holder, int position) {
        SingleItemPost post;
        GroupData group;
        if (isgroup) {
            post = groupViewModel.getPostList().getValue().get(position);
            group = groupData.get(0);
        } else {
            post = postViewModel.getPostList().getValue().get(position);
            group = groupData.get(position);
        }
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
                        }
                    }

                    @Override
                    public void onFailure(Call<LikeResponse> call, Throwable t) {
                        Log.d("like", "fail");
                    }
                });
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("해당 게시글을 삭제하시겠습니까?");


                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SingleItemPost post = post_Items.get(position);
                                int postId = post.getPostId();
                                Log.d("재학아어딨어", String.format("%d", postId));
                                service.deletePost(postId).enqueue(new Callback<DeletePostResponse>() {
                                    @Override
                                    public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {
                                        if (response.code() == 200) {
                                            Log.d("포스트삭제", "success");
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
                                        Toast.makeText(v.getContext(), "게시글이 성공적으로 삭제되었습니다!", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailure(Call<DeletePostResponse> call, Throwable t) {
                                        Log.d("포스트삭제", "실패");
                                        Toast.makeText(v.getContext(), "게시글 삭제 에러", Toast.LENGTH_SHORT).show();

                                    }


                                });


                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "삭제를 취소하셨습니다", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.show();



            }
        });
        holder.postGroupProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int clicked_groupId = post.getGroupId();
                service.getGroupData(clicked_groupId).enqueue(new Callback<GroupData>() {
                    @Override
                    public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                        GroupData groupData = response.body();
                        if (groupData != null) {
                            GroupFragment groupFragment = new GroupFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("groupData", groupData);
                            groupFragment.setArguments(bundle);
                            ((MainActivity) context).changeFragment(groupFragment);
                        }
                    }

                    @Override
                    public void onFailure(Call<GroupData> call, Throwable t) {
                        Log.d("ListPostAdapter", "fail");
                    }
                });
            }
        });


    }

    private void setHomeViewModel(MyViewHolder holder, int position) {

        postViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PostViewModel.class);
        if(postViewModel.getPostList().getValue()!=null){
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

    }

    private void setGroupViewModel(MyViewHolder holder, int position) {

        groupViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(GroupViewModel.class);
        groupViewModel.getPostList().observe((LifecycleOwner) context, new Observer<List<SingleItemPost>>() {
            @Override
            public void onChanged(List<SingleItemPost> singleItemPosts) {
                holder.postCommentNum.setText(String.valueOf(singleItemPosts.get(position).getCommentNum()));
                holder.postLikedNum.setText(String.valueOf(singleItemPosts.get(position).getLikeNum()));

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
        Button deleteButton;

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
            deleteButton = (Button) view.findViewById(R.id.button_Delete_Post);
        }

    }
}