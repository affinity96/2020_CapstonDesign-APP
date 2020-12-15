package com.example.homekippa.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.ChattingRoomActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> {
    private List<ChatProfile> chatList;
    private String userName;
    private ServiceApi service;
    private Activity activity;
    public UserData userData;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_nickname;
        public TextView textView_msg;
        public ImageView imageView_User;
        public View rootView;
        public String userId;

        public MyViewHolder(@NonNull View v) {
            super(v);
            textView_nickname = v.findViewById(R.id.textView_nickname);
            textView_msg = v.findViewById(R.id.testView_msg);
            imageView_User = v.findViewById(R.id.imageView_chatUser);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
        }

        public void setOnClickListener(View.OnClickListener monClickListener){
            rootView.setOnClickListener(monClickListener);
        }
    }

    public ListChatAdapter(List<ChatProfile> myDataset, Activity activity, String myNickName, UserData userData) {
        chatList = myDataset;
        this.userName = myNickName;
        this.activity = activity;
        this.userData = userData;
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    public ListChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_chat, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        ChatProfile chat = chatList.get(position);

        holder.textView_nickname.setText(chat.getUserName());
        holder.textView_msg.setText(chat.getMessage());

        service.getProfileImage(chat.getImage()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(activity).load(bitmap).circleCrop().into(holder.imageView_User);

                } else {
                    Glide.with(activity.getApplicationContext()).load(R.drawable.simplelogo).circleCrop().into(holder.imageView_User);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), ChattingRoomActivity.class);
                intent.putExtra("chatUserId", chat.getUserId());
                intent.putExtra("chatUserName", chat.getUserName());
                intent.putExtra("chatUserImage", chat.getImage());
                intent.putExtra("userData", userData);
                activity.startActivity(intent);
            }
        });
/*
        if(chat.getUserName().equals(this.userName)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.textView_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }

    public ChatProfile getChat(int position){
        return chatList != null ? chatList.get(position) : null;
    }

    public void addChat(ChatProfile chat) {
        chatList.add(chat);
        notifyItemInserted(chatList.size()-1);
    }
}
