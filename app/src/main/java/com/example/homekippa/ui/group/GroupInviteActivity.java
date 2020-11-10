package com.example.homekippa.ui.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

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
/*        Log.d("반려동물 확인", "들어옴");
        service.getPetsData(groupData.getGroupId()).enqueue(new Callback<List<SingleItemPet>>() {
            @Override
            public void onResponse(Call<List<SingleItemPet>> call, Response<List<SingleItemPet>> response) {
                if (response.isSuccessful()) {
                    Log.d("반려동물 확인", "성공");
                    List<SingleItemPet> pets = response.body();
                    Log.d("반려동물 아이디 확인", pets.get(0).getName());
                    petList.addAll(pets);

                    petId = pets.get(0).getId();//나중에 바꿔야 할 부분. 일단 가장 처음 강아지의 아이디만을 petId라 해놓음!

                    YesGroup.ListPetAdapter petAdapter = new YesGroup.ListPetAdapter(petList);

                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
                    pLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    listView.setLayoutManager(pLayoutManager);
                    listView.setItemAnimator(new DefaultItemAnimator());
                    listView.setAdapter(petAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SingleItemPet>> call, Throwable t) {
                Log.d("반려동물 확인", "에러");
                Log.e("반려동물 확인", t.getMessage());
            }
        });*/
    }

    public GroupData getGroupData(){
        return this.groupData;
    }
}