package com.example.homekippa.ui.notifications;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.PostDetailActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.NotiData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListNotiAdapter extends RecyclerView.Adapter<ListNotiAdapter.MyViewHolder> {
    private List<NotiData> noti_Items;
    private Context context;

    public ListNotiAdapter(Context context, ArrayList<NotiData> postItems) {
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
        NotiData noti = noti_Items.get(position);

        holder.notiGroupName.setText(noti.getFrom_Name());
        holder.notiDescription.setText(noti.getContent());
        holder.alarm_code = noti.getAlarm_code();
        holder.alarm_extra = noti.getExtra();
        //Todo - 시간 구현
        holder.notiTime.setText("1분 전");
    }

    @Override
    public int getItemCount() {
        return noti_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notiGroupName;
        TextView notiTime;
        TextView notiDescription;
        String alarm_code;
        String alarm_extra;

        MyViewHolder(View view) {
            super(view);
            notiGroupName = view.findViewById(R.id.textView__NotiGroupName);
            notiDescription = view.findViewById(R.id.textView__NotiDescription);
            notiTime = view.findViewById(R.id.textView__NotiTime);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alarm_code.equals("GROUP_INVITE")){
                        int groupId = Integer.parseInt(alarm_extra);
                        Toast.makeText(context, alarm_extra + "클릭됨", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}