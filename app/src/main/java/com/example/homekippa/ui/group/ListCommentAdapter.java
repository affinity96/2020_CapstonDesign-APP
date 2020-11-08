package com.example.homekippa.ui.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;

import java.util.ArrayList;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.MyViewHolder> {
    private final ArrayList<SingleItemComment> postComment_Items;

    public ListCommentAdapter(ArrayList<SingleItemComment> postCommentItems) {
        this.postComment_Items = postCommentItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_comment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setPostCommentData(holder, position);
    }

    private void setPostCommentData(MyViewHolder holder, int position) {
        SingleItemComment comment = postComment_Items.get(position);
        holder.profile.setImageResource(comment.getGroupCommentProfile());
        holder.groupName.setText(comment.getGroupCommentName());
        holder.groupLocation.setText(comment.getGroupCommentLocation());
        holder.commentContent.setText(comment.getGroupCommentContent());
    }

    @Override
    public int getItemCount() {
        return postComment_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView groupName;
        TextView groupLocation;
        TextView commentContent;

        MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.imageView_CommentGroupProfile);
            groupName = (TextView) view.findViewById(R.id.textView__CommentGroupName);
            groupLocation = (TextView) view.findViewById(R.id.textView__CommentGroupLocation);
            commentContent = (TextView) view.findViewById(R.id.textView_commentContent);
        }
    }
}
