package com.example.homekippa.ui.notifications;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;
import com.example.homekippa.data.NotiData;
import com.example.homekippa.ui.group.YesGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class ListNotiAdapter extends RecyclerView.Adapter<ListNotiAdapter.MyViewHolder> {
    private List<NotiData> noti_Items;
    private Context context;
    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public ListNotiAdapter(Context context, ArrayList<NotiData> postItems) {
        this.context = context;
        this.noti_Items = postItems;
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
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });

/*            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alarm_code.equals("GROUP_INVITE")){
//                        int groupId = Integer.parseInt(alarm_extra);
                        //Todo - 클릭시 초대 받는 화면 구현

                        FragmentManager fragmentManager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();

                        YesGroup yesGroup = new YesGroup();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.constraintlayout_Noti, yesGroup).commit();
                    }
                }
            });*/
        }
    }
}