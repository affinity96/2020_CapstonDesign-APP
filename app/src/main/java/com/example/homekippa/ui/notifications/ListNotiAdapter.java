package com.example.homekippa.ui.notifications;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;

import java.util.ArrayList;

public class ListNotiAdapter extends RecyclerView.Adapter<ListNotiAdapter.MyViewHolder> {
    private ArrayList<SingleItemNoti> noti_Items;

    private Context context;

    public ListNotiAdapter(Context context, ArrayList<SingleItemNoti> postItems) {
        this.context = context;
        this.noti_Items = postItems;
    }

    public ListNotiAdapter(ArrayList<SingleItemNoti> notiList) {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_notification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        setNotiData(holder, position);

    }


    private void setNotiData(MyViewHolder holder, int position) {
        SingleItemNoti noti = noti_Items.get(position);

        Glide.with(context).load(noti.getNotiGroupProfile()).circleCrop().into(holder.notiGroupProfile);
        holder.notiGroupProfile.setImageResource(noti.getNotiGroupProfile());
        holder.notiGroupName.setText(noti.getNotiGroupName());
        holder.notiTime.setText(noti.getNotiTime());

    }

    @Override
    public int getItemCount() {
        return noti_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView notiGroupProfile;
        TextView notiGroupName;
        TextView notiTime;


        MyViewHolder(View view) {
            super(view);
            notiGroupProfile = (ImageView) view.findViewById(R.id.imageView_NotiGroupProfile);
            notiGroupName = (TextView) view.findViewById(R.id.textView__NotiGroupName);
            notiTime = (TextView) view.findViewById(R.id.textView__NotiTime);
        }
    }
}