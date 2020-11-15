package com.example.homekippa.ui.group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;
import com.example.homekippa.data.CommentData;
import com.example.homekippa.data.CommentResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.MyViewHolder> {


    private Context context;
    private final ArrayList<SingleItemComment> postComment_Items;
    private ArrayList<CommentData> commentData;

    private ServiceApi service;

    public ListCommentAdapter(Context context, ArrayList<SingleItemComment> postCommentItems, ArrayList<CommentData> commentData) {
        this.context = context;
        this.commentData = commentData;
        this.postComment_Items = postCommentItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_comment, parent, false);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setPostCommentData(holder, position);
    }

    private void setPostCommentData(MyViewHolder holder, int position) {
        SingleItemComment comment = postComment_Items.get(position);
        holder.profile.setImageResource(comment.getGroupCommentProfile());
        holder.groupName.setText(comment.getGroupCommentGroupName());
        holder.NickName.setText(comment.getGroupCommentNickName());
        holder.groupLocation.setText(comment.getGroupCommentLocation());
        holder.commentContent.setText(comment.getGroupCommentContent());
        //TODO 삭제 버튼 보이고 안보이고 check
    }

    @Override
    public int getItemCount() {
        return postComment_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView groupName;
        TextView NickName;
        TextView groupLocation;
        TextView commentContent;
        TextView deleteComment;

        MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.imageView_CommentGroupProfile);
            groupName = (TextView) view.findViewById(R.id.textView__CommentGroupName);
            NickName = (TextView) view.findViewById(R.id.textView__CommentNickName);
            groupLocation = (TextView) view.findViewById(R.id.textView__CommentGroupLocation);
            commentContent = (TextView) view.findViewById(R.id.textView_commentContent);
            deleteComment = (TextView) view.findViewById(R.id.textView_deleteComment);

            deleteComment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("comment", String.valueOf(commentData.get(2).getPost_id()));


                    service.deleteComment(commentData.get(getAdapterPosition()).getId()).enqueue(new Callback<CommentResponse>() {
                        @Override
                        public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                            if (response.code() == 200) {
                                //TODO comment num down...
                                Log.d("delete comment", "success");
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentResponse> call, Throwable t) {

                        }
                    });

                }
            });
        }
    }
}
