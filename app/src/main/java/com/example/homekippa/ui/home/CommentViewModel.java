package com.example.homekippa.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommentViewModel extends ViewModel {

    public static MutableLiveData<String> comment = new MutableLiveData<>();
    public static MutableLiveData<Integer> counter = new MutableLiveData<>();

    public CommentViewModel() {
        comment.setValue("0");
        counter.setValue(0);
    }

    public MutableLiveData<String> getCommentNum() {
        if (comment == null) {
            comment = new MutableLiveData<String>();
        }
        return comment;
    }

    public MutableLiveData<Integer> getCounter() {
        return counter;
    }

    public static void increase() {
        counter.setValue(counter.getValue() + 1);
    }

    public static void decrease() {
        counter.setValue(counter.getValue() - 1);
    }


}
