package com.example.homekippa.ui.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupInviteActivity extends AppCompatActivity {
    private GroupData groupData;
    private SearchView searchView_user;
    private RecyclerView listView_users;
    private RecyclerView listView_Search_Users;
    private ServiceApi service;
    private ArrayList<UserData> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_invite_activity);

        groupData = (GroupData)getIntent().getExtras().get("groupData");

        searchView_user = findViewById(R.id.searchView_user);
        listView_users = findViewById(R.id.listView_Users);
        listView_Search_Users = findViewById(R.id.listView_Search_Users);
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setUserListView(listView_users);
    }

    private void setUserListView(RecyclerView listView) {
        Log.d("사용자 확인", "들어옴");
        service.getUsersInGroup(groupData.getGroupId()).enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful()) {
                    List<UserData> users = response.body();
                    userList.addAll(users);

                    ListUserAdapter userAdapter = new ListUserAdapter(userList);
                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                    pLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listView.setLayoutManager(pLayoutManager);
                    listView.setItemAnimator(new DefaultItemAnimator());
                    listView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                Log.d("반려동물 확인", "에러");
                Log.e("반려동물 확인", t.getMessage());
            }
        });
    }

    public GroupData getGroupData(){
        return this.groupData;
    }


    class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.MyViewHolder> {
        private ArrayList<UserData> user_Items;

        public ListUserAdapter(ArrayList<UserData> userItems) {
            this.user_Items = userItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_pet, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            setUserData(holder, position);
        }

        private void setUserData(MyViewHolder holder, int position) {
            UserData user = user_Items.get(position);
            holder.petName.setText(user.getUserName());
            Glide.with(getApplicationContext()).load(R.drawable.simplelogo).circleCrop().into(holder.petImage);
            holder.petImage.setImageResource(R.drawable.simplelogo);
        }

        @Override
        public int getItemCount() {
            return user_Items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView petName;
            ImageView petImage;

            MyViewHolder(View view) {
                super(view);
                petName = view.findViewById(R.id.listitem_PetName);
                petImage = view.findViewById(R.id.listitem_PetImage);

            }
        }
    }
}