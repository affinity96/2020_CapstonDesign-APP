package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.ui.group.ListCommentAdapter;
import com.example.homekippa.ui.group.ListPostImageAdapter;
import com.example.homekippa.ui.group.SingleItemComment;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "postDetail";

    private SingleItemPost post;
    ImageView postGroupProfile;
    TextView postGroupName;
    TextView postGroupLocation;
    TextView postTitle;
    TextView postContent;
    RecyclerView recyclerView_postImages;
    RecyclerView recyclerView_postComments;
    ArrayList<SingleItemPostImage> post_ImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();

        postGroupProfile = (ImageView) findViewById(R.id.imageView_DetailPostGroupProfile);
        postGroupName = (TextView) findViewById(R.id.textView__DetailPostGroupName);
        postGroupLocation = (TextView) findViewById(R.id.textView__DetailPostGroupLocation);
        postTitle = (TextView) findViewById(R.id.textView_DetailPostTitle);
        postContent = (TextView) findViewById(R.id.textView_DetailPostContent);
        recyclerView_postImages = (RecyclerView) findViewById(R.id.listview_DetailPostImages);
        recyclerView_postComments = (RecyclerView) findViewById(R.id.listview_PostComments);
        post = (SingleItemPost) intent.getExtras().get("post");

        postGroupProfile.setImageResource(post.getGroupPostProfile());
        postGroupName.setText(post.getGroupPostName());
        postGroupLocation.setText(post.getGroupPostLocation());
        postTitle.setText(post.getGroupPostTitle());
        postContent.setText(post.getGroupPostContent());
        post_ImageList = post.getGroupPostImage();

        setPostImage(post_ImageList);
        setPostComment(recyclerView_postComments);
    }

    private void setPostImage(ArrayList<SingleItemPostImage> post_ImageList) {
        ListPostImageAdapter adapter = new ListPostImageAdapter(post_ImageList);
        recyclerView_postImages.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL
                , false));
        recyclerView_postImages.setAdapter(adapter);

    }

    //TODO: Set Post Comment
    private void setPostComment(RecyclerView listView) {

        ArrayList<SingleItemComment> comment_List = new ArrayList<>();
        SingleItemComment comment = new SingleItemComment(R.drawable.dog_thang, "땡이네 콩 ", "경기도 용인시 기흥구 흥덕중앙로", "어머나!!!!!넘모 귕영웡용 ");
        comment_List.add(comment);

        ListCommentAdapter commentAdapter = new ListCommentAdapter(comment_List);
        listView.setAdapter(commentAdapter);

        LinearLayoutManager pLayoutManager = new LinearLayoutManager(this);
        pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(pLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

}