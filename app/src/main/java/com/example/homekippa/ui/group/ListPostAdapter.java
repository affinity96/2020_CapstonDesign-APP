package com.example.homekippa.ui.group;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;

import java.util.ArrayList;

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.MyViewHolder> {
    private ArrayList<SingleItemPost> post_Items;
    private Context context;
    ArrayList<SingleItemPostImage> postImageList = new ArrayList<>();

    public ListPostAdapter(Context context, ArrayList<SingleItemPost> postItems) {
        this.context = context;

        this.post_Items = postItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_post, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        getPostImageData();
        ListPostImageAdapter adapter = new ListPostImageAdapter(postImageList);
        setPostData(holder, position);
        holder.recyclerView_postImages.setLayoutManager(new LinearLayoutManager(context
                , LinearLayoutManager.HORIZONTAL
                , false));
        holder.recyclerView_postImages.setAdapter(adapter);
    }

    private void getPostImageData() {
        SingleItemPostImage postImage = new SingleItemPostImage(R.drawable.dog_tan);
        postImageList.add(postImage);
        postImage = new SingleItemPostImage(R.drawable.dog_woong);
        postImageList.add(postImage);
    }

    private void setPostData(MyViewHolder holder, int position) {
        SingleItemPost post = post_Items.get(position);
//            Glide.with(getActivity()).load(R.drawable.dog_woong).circleCrop().into(holder.postGroupProfile);
        holder.postGroupProfile.setImageResource(post.getGroupPostProfile());
        holder.postGroupName.setText(post.getGroupPostName());
        holder.postGroupLocation.setText(post.getGroupPostLocation());
        holder.postTitle.setText(post.getGroupPostTitle());
        holder.postContent.setText(post.getGroupPostContent());

    }

    @Override
    public int getItemCount() {
        return post_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postGroupProfile;
        TextView postGroupName;
        TextView postGroupLocation;
        TextView postTitle;
        TextView postContent;
        RecyclerView recyclerView_postImages;


        MyViewHolder(View view) {
            super(view);
            postGroupProfile = (ImageView) view.findViewById(R.id.imageView_PostGroupProfile);
            postGroupName = (TextView) view.findViewById(R.id.textView_PostGroupName);
            postGroupLocation = (TextView) view.findViewById(R.id.textView_PostGroupLocation);
            postTitle = (TextView) view.findViewById(R.id.textView_PostTitle);
            postContent = (TextView) view.findViewById(R.id.textView_PostContent);
            recyclerView_postImages = (RecyclerView) view.findViewById(R.id.listview_PostImages);

        }
    }
}