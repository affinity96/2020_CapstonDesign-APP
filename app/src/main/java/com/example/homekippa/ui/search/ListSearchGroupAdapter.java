package com.example.homekippa.ui.search;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.GroupFragment;

import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSearchGroupAdapter extends RecyclerView.Adapter<ListSearchGroupAdapter.MyViewHolder> {
    private ArrayList<GroupData> group_Items;
    private Activity activity;
    private ServiceApi service;

    public ListSearchGroupAdapter(ArrayList<GroupData> groupItems, Activity activity) {
        this.group_Items = groupItems;
        this.activity = activity;
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @NonNull
    @Override
    public ListSearchGroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_searchresult, parent, false);
        return new ListSearchGroupAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSearchGroupAdapter.MyViewHolder holder, int position) {
        setUserData(holder, position);
    }

    private void setUserData(ListSearchGroupAdapter.MyViewHolder holder, int position) {
        GroupData group = group_Items.get(position);
        holder.groupData = group;
        holder.searchGroupName.setText(group.getName());
        holder.searchGroupDesc.setText(group.getIntroduction());
        Glide.with(activity.getApplicationContext()).load(R.drawable.simplelogo).circleCrop().into(holder.searchGroupImage);
        holder.searchGroupImage.setImageResource(R.drawable.simplelogo);
        holder.button_InviteUser.setVisibility(View.INVISIBLE);

        service.getProfileImage(group.getImage()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(activity).load(bitmap).circleCrop().into(holder.searchGroupImage);

                } else {
                    Glide.with(activity.getApplicationContext()).load(R.drawable.simplelogo).circleCrop().into(holder.searchGroupImage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return group_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView searchGroupName;
        TextView searchGroupDesc;
        ImageView searchGroupImage;
        Button button_InviteUser;
        GroupData groupData;

        MyViewHolder(View view) {
            super(view);
            searchGroupName = view.findViewById(R.id.textView_SearchUserName);
            searchGroupDesc = view.findViewById(R.id.textView_SearchUserDesc);
            searchGroupImage = view.findViewById(R.id.imageView_SearchUserProfile);
            button_InviteUser = view.findViewById(R.id.button_InviteUser);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GroupFragment groupFragment = new GroupFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("groupData", groupData);
                    groupFragment.setArguments(bundle);
                    ((MainActivity)activity).changeFragment(groupFragment);
                    Log.d("검색", "클릭됨");
                }
            });

        }
    }
}