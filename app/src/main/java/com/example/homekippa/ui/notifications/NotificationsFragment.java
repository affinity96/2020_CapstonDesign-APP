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
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.SingleItemDailyWork;
import com.example.homekippa.ui.group.YesGroup;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private ArrayList<SingleItemNoti> notiList = new ArrayList<>();
    private RecyclerView listView_noti;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        listView_noti = root.findViewById(R.id.listView_Notifications);

        setNotiListView(listView_noti);

        return root;
    }

    private void setNotiListView(RecyclerView listView) {
        getNotiData();

        ListNotiAdapter workAdapter = new ListNotiAdapter(this.getContext(), notiList);

        LinearLayoutManager dLayoutManager = new LinearLayoutManager(getActivity());
        dLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(dLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        listView.setAdapter(workAdapter);
    }

    private void getNotiData() {
        SingleItemNoti noti = new SingleItemNoti(R.drawable.dog_tan, "웅이네", "3시간");
        notiList.add(noti);
        noti = new SingleItemNoti(R.drawable.dog_thang, "땡이네집인데ㅐ? ", "1일");
        notiList.add(noti);


    }


}