package com.example.homekippa.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;

import java.util.Collections;
import java.util.List;

public class ListChattingRoomAdapter extends RecyclerView.Adapter<ListChattingRoomAdapter.MyViewHolder>{
    private List<ChatData> chatList;
    private Activity activity;
    private Bitmap bitmap;

    public ListChattingRoomAdapter(List<ChatData> myDataset, Activity activity, Bitmap bitmap){
        chatList = myDataset;
        this.activity = activity;
        this.bitmap = bitmap;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_chat, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatData chat = chatList.get(position);

        holder.textView_nickname.setText(chat.getMessage());
        holder.setWhere(chat.getFrom());
        Glide.with(activity).load(bitmap).circleCrop().into(holder.imageView_User);
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }

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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void setWhere(String from){
            if(from.equals("false")){
                imageView_User.setVisibility(View.INVISIBLE);
                textView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
        }
    }

    public void addChat(ChatData chat) {
        chatList.add(chat);
        Collections.sort(chatList);
        notifyItemInserted(chatList.size()-1);
    }
}
