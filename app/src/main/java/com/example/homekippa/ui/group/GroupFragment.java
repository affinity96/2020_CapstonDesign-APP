package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;

public class GroupFragment extends Fragment {

    private GroupViewModel groupViewModel;
    Fragment fragment_yesGroup;
    Fragment fragment_noGroup;



    public static GroupFragment newInstance() {
        return new GroupFragment();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupViewModel =
                ViewModelProviders.of(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group, container, false);

        fragment_noGroup=new NoGroup();
        fragment_yesGroup=new YesGroup();



        boolean groupCreated = true;
        if (groupCreated) {
            ((MainActivity)getActivity()).replaceGroupFragment(YesGroup.newInstance());
        } else {
            ((MainActivity)getActivity()).replaceGroupFragment(NoGroup.newInstance());
        }
        return root;
    }
}