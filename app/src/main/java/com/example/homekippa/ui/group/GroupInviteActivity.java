package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupInviteData;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupInviteActivity extends AppCompatActivity {
    private UserData userData;
    private GroupData groupData;
    private SearchView searchView_user;
    private RecyclerView listView_users;
    private RecyclerView listView_Search_Users;
    private ServiceApi service;
    private Button button_SearchUser;
    private ArrayList<UserData> userList = new ArrayList<>();
    private ArrayList<UserData> searchUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_invite_activity);

        groupData = (GroupData)getIntent().getExtras().get("groupData");
        userData = (UserData)getIntent().getExtras().get("userData");

        searchView_user = findViewById(R.id.searchView_user);
        listView_users = findViewById(R.id.listView_Users);
        listView_Search_Users = findViewById(R.id.listView_Search_Users);
        button_SearchUser = findViewById(R.id.button_SearchUser);
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

       // setUserListView(listView_users);
        searchView_user.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                setSearchListView(listView_Search_Users, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        button_SearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView_user.setQuery(searchView_user.getQuery(), true);
            }
        });
    }

    private void setSearchListView(RecyclerView listView_search_users, String filter) {
        service.getUserSearchResult(filter).enqueue(new Callback<List<UserData>>(){
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful()) {
                    searchUserList.clear();
                    List<UserData> users = response.body();
                    searchUserList.addAll(users);

                    ListSearchUserAdapter userAdapter = new ListSearchUserAdapter(searchUserList);
                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                    pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    listView_search_users.setLayoutManager(pLayoutManager);
                    listView_search_users.setItemAnimator(new DefaultItemAnimator());
                    listView_search_users.setAdapter(userAdapter);

                    if(users.isEmpty()){
                        Toast.makeText(getApplicationContext(), "검색 결과가 없습니다", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {

            }
        });
    }

    private void setUserListView(RecyclerView listView) {
        Log.d("사용자 확인", "들어옴");
        service.getUsersInGroup(groupData.getId()).enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful()) {
                    List<UserData> users = response.body();
                    userList.addAll(users);
                    Log.d("사용자 확인", response.body().toString());

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

    class ListSearchUserAdapter extends RecyclerView.Adapter<ListSearchUserAdapter.MyViewHolder> {
        private ArrayList<UserData> user_Items;

        public ListSearchUserAdapter(ArrayList<UserData> userItems) {
            this.user_Items = userItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_searchresult, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            setUserData(holder, position);
        }

        private void setUserData(MyViewHolder holder, int position) {
            UserData user = user_Items.get(position);
            holder.searchUserName.setText(user.getUserName());
            Glide.with(getApplicationContext()).load(R.drawable.simplelogo).circleCrop().into(holder.searchUserImage);
            holder.searchUserImage.setImageResource(R.drawable.simplelogo);
            holder.button_InviteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    service.sendGroupInvite(new GroupInviteData(groupData, userData, user)).enqueue(new Callback<UidRespense>() {
                        @Override
                        public void onResponse(Call<UidRespense> call, Response<UidRespense> response) {
                            if(response.isSuccessful() && response.body().getResult()){
                                holder.button_InviteUser.setEnabled(false);
                                Toast.makeText(getApplicationContext(), "그룹 초대를 보냈습니다", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UidRespense> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "그룹 초대 전송 실패", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return user_Items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView searchUserName;
            TextView searchUserDesc;
            ImageView searchUserImage;
            Button button_InviteUser;

            MyViewHolder(View view) {
                super(view);
                searchUserName = view.findViewById(R.id.textView_SearchUserName);
                searchUserDesc = view.findViewById(R.id.textView_SearchUserDesc);
                searchUserImage = view.findViewById(R.id.imageView_SearchUserProfile);
                button_InviteUser = view.findViewById(R.id.button_InviteUser);
            }
        }
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
            holder.userName.setText(user.getUserName());
            Glide.with(getApplicationContext()).load(R.drawable.simplelogo).circleCrop().into(holder.userImage);
            holder.userImage.setImageResource(R.drawable.simplelogo);
        }

        @Override
        public int getItemCount() {
            return user_Items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView userName;
            ImageView userImage;

            MyViewHolder(View view) {
                super(view);
                userName = view.findViewById(R.id.listitem_PetName);
                userImage = view.findViewById(R.id.listitem_PetImage);

            }
        }
    }
}