package com.example.homekippa.ui.walk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WalkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WalkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
