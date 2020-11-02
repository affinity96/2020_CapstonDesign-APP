//package com.example.homekippa;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;
//public class CollectionFragment extends Fragment {
//    CollectionAdapter collectionAdapter;
//    ViewPager2 viewPager;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_tap_group, container, false);
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        collectionAdapter = new CollectionAdapter(this);
//        TabLayout tabLayout= view.findViewById(R.id.tab_layout);
//        viewPager = view.findViewById(R.id.pager);
//        viewPager.setAdapter(collectionAdapter);
//        new TabLayoutMediator(tabLayout, viewPager,
//                (tab, position) -> tab.setText("OBJECT " + (position + 1))
//        ).attach();
//    }
//}
//
//class CollectionAdapter extends FragmentStateAdapter {
//    public CollectionAdapter(Fragment fragment) {
//        super(fragment);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        // Return a NEW fragment instance in createFragment(int)
//        Fragment fragment = new ObjectFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
//        args.putInt(ObjectFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public int getItemCount() {
//        return 100;
//    }
//}
//
//class ObjectFragment extends Fragment {
//    public static final String ARG_OBJECT = "object";
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_tap_group, container, false);
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        ((TextView) view.findViewById(android.R.id.text1))
//                .setText(Integer.toString(args.getInt(ARG_OBJECT)));
//    }
//}
//
//
//
