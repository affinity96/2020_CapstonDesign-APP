package com.example.homekippa.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.NotiData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.GroupFragment;
import com.example.homekippa.ui.group.GroupInviteActivity;
import com.example.homekippa.ui.group.SingleItemDailyWork;
import com.example.homekippa.ui.group.YesGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private ArrayList<NotiData> notiList = new ArrayList<>();
    private RecyclerView listView_noti;
    private ServiceApi service;
    private UserData userData;
    private String alarm_extra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        userData = ((MainActivity)getActivity()).getUserData();
        Log.d("notification", "create");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        listView_noti = root.findViewById(R.id.listView_Notifications);

        setNotiListView(listView_noti);
        return root;
    }

    private void setNotiListView(RecyclerView listView) {
//        getNotiData(userData.getUserId());
        service.getNotiData(userData.getUserId()).enqueue(new Callback<List<NotiData>>() {
            @Override
            public void onResponse(Call<List<NotiData>> call, Response<List<NotiData>> response) {
                List<NotiData> notis = response.body();
                if(!notis.isEmpty()) {
                    notiList.addAll(notis);
                }

                ListNotiAdapter workAdapter = new ListNotiAdapter(getContext(), notiList);
                workAdapter.setOnItemClickListener(new ListNotiAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        NotiData notidata = notiList.get(pos);
                        if (notidata.getAlarm_code().equals("GROUP_INVITE")){
                            Log.d("notidata group code", notidata.getExtra());
                            service.getGroupData(Integer.parseInt(notidata.getExtra())).enqueue(new Callback<GroupData>() {
                                @Override
                                public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                                    GroupData groupData = response.body();

                                    if(groupData != null){
                                        GroupFragment groupFragment = new GroupFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("groupData", groupData);
                                        groupFragment.setArguments(bundle);
                                        ((MainActivity)getActivity()).changeFragment(groupFragment);
                                    }
                                }

                                @Override
                                public void onFailure(Call<GroupData> call, Throwable t) {

                                }
                            });
                        }
                }});

                LinearLayoutManager dLayoutManager = new LinearLayoutManager(getActivity());
                dLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                listView.setLayoutManager(dLayoutManager);
                listView.setItemAnimator(new DefaultItemAnimator());
                listView.setAdapter(workAdapter);
            }

            @Override
            public void onFailure(Call<List<NotiData>> call, Throwable t) {

            }
        });
    }

    public void setAlarm_extra(String alarm_extra){
        this.alarm_extra = alarm_extra;
    }
}