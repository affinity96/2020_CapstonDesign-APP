package com.example.homekippa.ui.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.R;

import java.util.ArrayList;

public class SingleItemPostImage {
    private int PostImageId;

    public SingleItemPostImage(int postImageId) {
        PostImageId = postImageId;
    }

    public int getPostImageId() {
        return PostImageId;
    }

    public void setPostImageId(int postImageId) {
        PostImageId = postImageId;
    }


}
