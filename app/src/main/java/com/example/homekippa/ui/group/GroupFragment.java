package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;

public class GroupFragment extends Fragment {

    private GroupViewModel groupViewModel;

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupViewModel =
                ViewModelProviders.of(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group, container, false);

        boolean groupCreated = true;
        if (groupCreated) {
            ((MainActivity)getActivity()).replaceGroupFragment(YesGroup.newInstance());
        } else {
            ((MainActivity)getActivity()).replaceGroupFragment(NoGroup.newInstance());
        }


        return root;
    }
}