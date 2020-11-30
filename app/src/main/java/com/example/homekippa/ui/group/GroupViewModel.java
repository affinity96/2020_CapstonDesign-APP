package com.example.homekippa.ui.group;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.homekippa.SingleItemPost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupViewModel extends ViewModel {

    public static MutableLiveData<List<SingleItemPost>> post = new MutableLiveData<>();
    public static MutableLiveData<List<Boolean>> likeCheck = new MutableLiveData<>();
    public MutableLiveData<InputStream> groupProfile= new MutableLiveData<>();

    public GroupViewModel() {

    }

    public MutableLiveData<List<SingleItemPost>> getPostList() {
        if (post == null) {
            post = new MutableLiveData<>();
        }
        return post;
    }

    public MutableLiveData<List<Boolean>> getLikeCheck() {
        if (likeCheck == null) {
            likeCheck = new MutableLiveData<>();
        }
        return likeCheck;
    }

    public MutableLiveData<InputStream> getGroupProfile(){
        if (groupProfile == null) {
            groupProfile = new MutableLiveData<>();
        }
        return groupProfile;
    }

    public static void increaseComment(int pos) {
        List<SingleItemPost> posts = new ArrayList<>();
        posts.addAll(post.getValue());
        posts.get(pos).setCommentNum(posts.get(pos).getCommentNum() + 1);
        post.setValue(posts);
    }

    public static void setLiveLikeNum(int pos, int val) {
        List<SingleItemPost> posts = new ArrayList<>();
        posts.addAll(post.getValue());
        posts.get(pos).setLikeNum(posts.get(pos).getLikeNum() + (val));
        Log.d("view model", String.valueOf(val));
        Log.d("view model", String.valueOf(posts.get(pos).getLikeNum()));

        post.setValue(posts);
    }

    public static void setLiveLikeCheck(int pos, boolean val) {
        List<Boolean> likecheck = new ArrayList<>();
        likecheck.addAll(likeCheck.getValue());
        likecheck.set(pos, val);
        likeCheck.setValue(likecheck);
    }
}