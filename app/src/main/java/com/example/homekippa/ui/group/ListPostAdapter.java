package com.example.homekippa.ui.group;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.PostDetailActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.MyViewHolder> {
    private ArrayList<SingleItemPost> post_Items;
    private ArrayList<GroupData> groupData = new ArrayList<>();

    private Context context;
    private boolean isgroup;

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
//        holder.postGroupName.setText(post.getGroup_id());
//        holder.postGroupLocation.setText(post.getGroupPostLocation());

        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postCommentNum.setText(String.valueOf(post.getCommentNum()));
        holder.postLikedNum.setText(String.valueOf(post.getLikeNum()));
        holder.postGroupName.setText(group.getGroupName());
        holder.postGroupAddress.setText(group.getGroupAddress());


//        setPostImageAdapter(holder, post_Items.get(position).getGroupPostImage());
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

            //각 게시글(PostListItem) 클릭
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    SingleItemPost post = post_Items.get(getAdapterPosition());
                    intent.putExtra("post", post);
                    context.startActivity(intent);
                }
            });

        }
    }
}