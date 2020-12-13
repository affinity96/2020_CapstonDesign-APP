package com.example.homekippa.chat;

import android.content.Context;
import android.content.Intent;
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

import com.example.homekippa.ChattingRoomActivity;
import com.example.homekippa.R;

import java.util.List;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> {
    private List<ChatProfile> chatList;
    private String userName;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_nickname;
        public TextView textView_msg;
        public View rootView;

        public MyViewHolder(@NonNull View v) {
            super(v);
            textView_nickname = v.findViewById(R.id.textView_nickname);
            textView_msg = v.findViewById(R.id.testView_msg);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
        }
    }

    public ListChatAdapter(List<ChatProfile> myDataset, Context context, String myNickName) {
        chatList = myDataset;
        this.userName = myNickName;
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
