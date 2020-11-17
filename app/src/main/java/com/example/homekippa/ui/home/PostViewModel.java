package com.example.homekippa.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.homekippa.R;
import com.example.homekippa.data.GetLikeData;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel {
    private ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
    public static MutableLiveData<List<SingleItemPost>> post = new MutableLiveData<>();

    public PostViewModel() {
    }

    public MutableLiveData<List<SingleItemPost>> getPostList() {
        if (post == null) {
            post = new MutableLiveData<>();
        }
        return post;
    }

    public static void increase(int pos) {
        List<SingleItemPost> posts = new ArrayList<>();
        posts.addAll(post.getValue());
        posts.get(pos).setCommentNum(posts.get(pos).getCommentNum() + 1);
        post.setValue(posts);
    }

//    public static void decrease(int pos) {
//        counter.setValue(counter.getValue() - 1);
//    }

}
