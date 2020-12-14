package com.example.homekippa.ui.group;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GroupFollowViewModel extends ViewModel {

    public static MutableLiveData<List<Integer>> follower = new MutableLiveData<>();
    public static MutableLiveData<List<Integer>> following = new MutableLiveData<>();

    public GroupFollowViewModel() {
    }

    public MutableLiveData<List<Integer>> getFollower() {
        if (follower == null) {
            follower = new MutableLiveData<>();
        }
        return follower;
    }

    public MutableLiveData<List<Integer>> getFollowing() {
        if (following == null) {
            following = new MutableLiveData<>();
        }
        return following;
    }

    public boolean checkFollow(int id) {
        return getFollowing().getValue().contains(id);
    }

    public Integer getFollowingNum() {
        Log.d("follow view ", String.valueOf(following.getValue().size()));
        if (following.getValue() != null) {
            return following.getValue().size();
        } else {
            return 0;
        }
    }

    public Integer getFollowerNum() {
        if (follower.getValue() != null)
            return follower.getValue().size();
        else {
            return 0;
        }
    }

    public void cancelFollowing(int id) {
        List<Integer> fo = new ArrayList<>();
        fo.addAll(following.getValue());
        fo.remove((Integer) id);
        following.setValue(fo);
    }

    public void addFollowing(int id) {
        MutableLiveData<List<Integer>> liveFollow = new MutableLiveData<>();
        List<Integer> fo = new ArrayList<>();
        fo.addAll(following.getValue());
        fo.add(id);
        liveFollow.setValue(fo);
        following = liveFollow;
//        following.setValue(fo);
    }
}