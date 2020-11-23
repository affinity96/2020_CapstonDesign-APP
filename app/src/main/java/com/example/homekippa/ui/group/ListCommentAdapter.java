package com.example.homekippa.ui.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;
import com.example.homekippa.data.CommentData;
import com.example.homekippa.data.CommentResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
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
        try {
            setPostCommentData(holder, position);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setPostCommentData(MyViewHolder holder, int position) throws ParseException {
        SingleItemComment comment = postComment_Items.get(position);
        getProfileImage(holder, comment.getGroupCommentProfile());
        holder.groupName.setText(comment.getGroupCommentGroupName());
        holder.NickName.setText(comment.getGroupCommentNickName());
        holder.groupLocation.setText(comment.getGroupCommentLocation());
        holder.commentContent.setText(comment.getGroupCommentContent());

        setCommentDate(holder, position);

        //TODO 삭제 버튼 보이고 안보이고 check

    }

    private void setCommentDate(MyViewHolder holder, int position) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String olddate = commentData.get(position).getDate();
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
                Log.d("date", String.valueOf(newdate));


        holder.commentDate.setText(newdate);
    }

    private void getProfileImage(MyViewHolder holder, String url) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "ListCommentAdapter";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(context).load(bitmap).circleCrop().into(holder.profile);
                    holder.profile.setImageBitmap(bitmap);

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
        TextView commentDate;


        MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.imageView_CommentGroupProfile);
            groupName = (TextView) view.findViewById(R.id.textView__CommentGroupName);
            NickName = (TextView) view.findViewById(R.id.textView__CommentNickName);
            groupLocation = (TextView) view.findViewById(R.id.textView__CommentGroupLocation);
            commentContent = (TextView) view.findViewById(R.id.textView_commentContent);
            deleteComment = (TextView) view.findViewById(R.id.textView_deleteComment);
            commentDate = (TextView) view.findViewById(R.id.textView_CommentDate);

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
