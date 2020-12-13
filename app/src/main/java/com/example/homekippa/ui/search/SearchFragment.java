package com.example.homekippa.ui.search;

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
import android.widget.Toast;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private Button button;
    private RecyclerView listview_groups;
    private ServiceApi service;
    private ArrayList<GroupData> searchGroupList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = root.findViewById(R.id.searchView_Group);
        listview_groups = root.findViewById(R.id.listView_Search_Groups);
        button = root.findViewById(R.id.button_SearchGroup);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchListView(listview_groups, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery(searchView.getQuery(), true);
            }
        });

        return root;
    }

    private void setSearchListView(RecyclerView listView_search_Groups, String filter) {
        service.getGroupSearchResult(filter).enqueue(new Callback<List<GroupData>>(){
            @Override
            public void onResponse(Call<List<GroupData>> call, Response<List<GroupData>> response) {
                if (response.isSuccessful()) {
                    searchGroupList.clear();
                    List<GroupData> groups = response.body();
                    searchGroupList.addAll(groups);

                    ListSearchGroupAdapter listAdapter = new ListSearchGroupAdapter(searchGroupList, getActivity());
                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    listView_search_Groups.setLayoutManager(pLayoutManager);
                    listView_search_Groups.setItemAnimator(new DefaultItemAnimator());
                    listView_search_Groups.setAdapter(listAdapter);

                    if(groups.isEmpty()){
                        Toast.makeText(getActivity().getApplicationContext(), "검색 결과가 없습니다", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupData>> call, Throwable t) {

            }
        });
    }
}
