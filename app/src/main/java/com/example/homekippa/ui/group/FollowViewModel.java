package com.example.homekippa.ui.group;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.homekippa.SingleItemPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FollowViewModel extends ViewModel {

    public static MutableLiveData<List<Integer>> follower = new MutableLiveData<>();
    public static MutableLiveData<List<Integer>> following = new MutableLiveData<>();

    public FollowViewModel() {

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
        return following.getValue().size();
    }

    public Integer getFollowerNum() {
        return follower.getValue().size();
    }

    public void cancelFollowing(int id) {
        List<Integer> fo = new ArrayList<>();
        fo.addAll(following.getValue());
        fo.remove((Integer)id);
        following.setValue(fo);
    }

    public void addFollowing(int id) {
        List<Integer> fo = new ArrayList<>();
        fo.addAll(following.getValue());
        fo.add(id);
        following.setValue(fo);
    }
}