package com.example.homekippa.ui.home;

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

import com.example.homekippa.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    HomeCollectionAdapter homeCollectionAdapter;
    ViewPager2 viewpager2;

    private String[] tabTitles = new String[]{"팔로우", "우리동네"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
//        connectViewPagerToTab(view);
        homeCollectionAdapter = new HomeCollectionAdapter(this);
        viewpager2 = view.findViewById(R.id.home_pager);
        viewpager2.setAdapter(homeCollectionAdapter);
        TabLayout tabLayout = view.findViewById(R.id.home_tab_layout);
        new TabLayoutMediator(tabLayout, viewpager2, (tab, position) -> tab.setText(tabTitles[position])).attach();
    }

    private void connectViewPagerToTab(View view) {

    }
}

class HomeCollectionAdapter extends FragmentStateAdapter {

    public HomeCollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    //TODO: change the parameter to distinct "팔로워" tab and "우리동네"
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                Fragment fragment = new HomePost();
                fragment.setArguments(args);
                return fragment;
            case 1:
                Fragment fragment1 = new HomePost();
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