package com.example.homekippa.ui.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;

import java.util.ArrayList;
public class ListPostImageAdapter extends RecyclerView.Adapter<ListPostImageAdapter.MyViewHolder> {
    private ArrayList<SingleItemPostImage> postImage_Items;

    public ListPostImageAdapter(ArrayList<SingleItemPostImage> postImageItems) {
        this.postImage_Items = postImageItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_postimage, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPostImageAdapter.MyViewHolder holder, int position) {
        setPostImageData(holder, position);

    }

    private void setPostImageData(MyViewHolder holder, int position) {
        SingleItemPostImage postImage = postImage_Items.get(position);
        holder.postImage.setImageResource(postImage.getPostImageId());
    }

    @Override
    public int getItemCount() {
        return postImage_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        MyViewHolder(View view) {
            super(view);
            postImage = (ImageView) view.findViewById(R.id.listitem_PostImage);
        }
    }
}