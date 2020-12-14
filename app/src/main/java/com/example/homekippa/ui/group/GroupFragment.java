package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupFragment extends Fragment {
    private UserData userData;
    private GroupData groupData;

    GroupCollectionAdapter groupCollectionAdapter;
    ViewPager2 viewpager;

    private String[] tabTitles = new String[]{"그룹명", "추억 저장소"};

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        userData = ((MainActivity) getActivity()).getUserData();
        groupData = (GroupData) getArguments().get("groupData");
        connectViewPagerToTab(view);
    }

    private void connectViewPagerToTab(@Nullable View view) {
        groupCollectionAdapter = new GroupCollectionAdapter(this, userData, groupData);
        viewpager = view.findViewById(R.id.group_pager);
        viewpager.setAdapter(groupCollectionAdapter);
        TabLayout tabLayout = view.findViewById(R.id.group_tab_layout);
        new TabLayoutMediator(tabLayout, viewpager, (tab, position) -> tab.setText(tabTitles[position])).attach();
    }
}

class GroupCollectionAdapter extends FragmentStateAdapter {
    private UserData userData;
    private GroupData groupData;

    public GroupCollectionAdapter(Fragment fragment, UserData userData, GroupData groupData) {
        super(fragment);
        this.userData = userData;
        this.groupData = groupData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int groupId = userData.getGroupId();
        Bundle args = new Bundle();
        args.putParcelable("groupData", groupData);
        if (groupId == groupData.getId()) {
            args.putBoolean("myGroup", true);
        } else {
            args.putBoolean("myGroup", false);
        }
        switch (position) {
            case 0:
                Fragment fragment = new YesGroup();
                fragment.setArguments(args);
                return fragment;
            case 1:
                Fragment fragment1 = new GroupPost();
                fragment1.setArguments(args);
                return fragment1;

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}