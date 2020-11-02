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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupFragment extends Fragment {

    private GroupViewModel groupViewModel;

    GroupCollectionAdapter groupCollectionAdapter;
    ViewPager2 viewpager;
    private String[] tabTitles = new String[]{"그룹명", "추억 저장소"};

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupViewModel =
                ViewModelProviders.of(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        connectViewPagerToTab(view);

    }

    private void connectViewPagerToTab(@Nullable View view) {
        groupCollectionAdapter = new GroupCollectionAdapter(this);
        viewpager = view.findViewById(R.id.group_pager);
        viewpager.setAdapter(groupCollectionAdapter);
        TabLayout tabLayout = view.findViewById(R.id.group_tab_layout);
        new TabLayoutMediator(tabLayout, viewpager, (tab, position) -> tab.setText(tabTitles[position])).attach();
    }
}

class GroupCollectionAdapter extends FragmentStateAdapter {
    public GroupCollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                boolean groupCreated = true;
                if (groupCreated) {
                    return new YesGroup();
                } else {
                    return new NoGroup();
                }
            case 1:
                return new NoGroup();
        }
        return new YesGroup();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}