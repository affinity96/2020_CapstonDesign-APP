package com.example.homekippa.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homekippa.R;
import com.example.homekippa.data.CommentData;
import com.example.homekippa.data.CommentGetResponse;
import com.example.homekippa.data.CommentResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.GroupPost;
import com.example.homekippa.ui.group.ListCommentAdapter;
import com.example.homekippa.ui.group.ListPostImageAdapter;
import com.example.homekippa.ui.group.SingleItemComment;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "postDetail";

    private PostViewModel viewModel;

    private GroupData group;
    private UserData user;
    private Intent intent;
    private int postPosition;
    private boolean isliked;


    private SingleItemPost post;
    ImageView postGroupProfile;
    TextView postGroupName;
    TextView postNickName;
    TextView postGroupLocation;
    TextView postTitle;
    TextView postContent;
    TextView postLikeNum;
    TextView postCommentNum;
    Button postLikedImage;

    TextView comment;
    ArrayList<CommentData> comment_List = new ArrayList<>();
    ArrayList<UserData> user_List = new ArrayList<>();
    ArrayList<GroupData> group_List = new ArrayList<>();

    RecyclerView recyclerView_postImages;
    RecyclerView recyclerView_postComments;
    ArrayList<SingleItemPostImage> post_ImageList;
    EditText commentInput;
    private TextView postComment;
    private int commentNum;

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        setPostDetail();

        postComment = (TextView) findViewById(R.id.textView_postComment);
        commentInput = (EditText) findViewById(R.id.editText_comment);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable str_comment = commentInput.getText();
                //TODO change the date format
                String date_comment = "2020-11-11";

                CommentData commentData = new CommentData(post.getPostId(), user.getUserId(), str_comment.toString(), date_comment);
                service.setComment(commentData).enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if (response.code() == 200) {
                            Log.d("comment", "success");
                            commentInput.setText(null);

                            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            setPostComment(recyclerView_postComments);

//                            Log.d("comment", viewModel.getCounter().getValue().toString());
                            PostViewModel.increaseComment(postPosition);
                            setPostDetail();


                        }
                    }

                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {
                        Log.d("comment", "fail");
                    }
                });
            }
        });


    }

    private void setPostDetail() {

        intent = getIntent();
        post = (SingleItemPost) intent.getExtras().get("post");
        group = (GroupData) intent.getExtras().get("group");
        user = (UserData) intent.getExtras().get("user");
        postPosition = (int) intent.getExtras().get("pos");


        viewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(PostViewModel.class);

        postGroupProfile = (ImageView) findViewById(R.id.imageView_DetailPostGroupProfile);
        postGroupName = (TextView) findViewById(R.id.textView__DetailPostGroupName);
        postGroupLocation = (TextView) findViewById(R.id.textView__DetailPostGroupLocation);
        postTitle = (TextView) findViewById(R.id.textView_DetailPostTitle);
        postContent = (TextView) findViewById(R.id.textView_DetailPostContent);
        postLikeNum = (TextView) findViewById(R.id.textView_LikedNum);
        postCommentNum = (TextView) findViewById(R.id.textView_commentNum);
        recyclerView_postImages = (RecyclerView) findViewById(R.id.listview_DetailPostImages);
        recyclerView_postComments = (RecyclerView) findViewById(R.id.listview_PostComments);
        postLikedImage = (Button) findViewById(R.id.imageView_DetailPostLiked);
        isliked = viewModel.getLikeCheck().getValue().get(postPosition);

//        postGroupProfile.setImageResource(post.getGroupPostProfile());


        post_ImageList = post.getGroupPostImage();
        postGroupName.setText(group.getName());
        postGroupLocation.setText(group.getAddress());
        postTitle.setText(post.getTitle());
        postContent.setText(post.getContent());
        postLikeNum.setText(String.valueOf(viewModel.getPostList().getValue().get(postPosition).getLikeNum()));
        postCommentNum.setText(String.valueOf(viewModel.getPostList().getValue().get(postPosition).getCommentNum()));
        postLikedImage.setActivated(isliked);
        getGroupProfileImage(group.getImage(), postGroupProfile);

        setPostImage(post_ImageList);
        setPostComment(recyclerView_postComments);

        postLikedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LikeData likeData = new LikeData(post.getPostId(), user.getUserId(), v.isActivated());
                service.setLike(likeData).enqueue(new Callback<LikeResponse>() {
                    @Override
                    public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                        if (response.code() == 200) {

                            if (!v.isActivated()) {
                                Log.d("like", "success");
                                PostViewModel.setLiveLikeNum(postPosition, 1);
                                PostViewModel.setLiveLikeCheck(postPosition, true);
                            } else {
                                PostViewModel.setLiveLikeNum(postPosition, -1);
                                PostViewModel.setLiveLikeCheck(postPosition, false);
                            }
                            setPostDetail();
//                            v.setActivated(!v.isActivated());
                        }
                    }

                    @Override
                    public void onFailure(Call<LikeResponse> call, Throwable t) {
                        Log.d("like", "fail");
                    }
                });
            }
        });

    }

    private void getGroupProfileImage(String url, ImageView imageView) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "PostDetailActivity";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(PostDetailActivity.this).load(bitmap).circleCrop().into(imageView);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
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

        service.getComment(post.getPostId()).enqueue(new Callback<CommentGetResponse>() {
            @Override
            public void onResponse(Call<CommentGetResponse> call, Response<CommentGetResponse> response) {
                if (response.code() == 200) {

                    CommentGetResponse commentGetResponse = response.body();

                    comment_List = commentGetResponse.getComments();
                    user_List = commentGetResponse.getUsers();
                    group_List = commentGetResponse.getGroups();
                    ArrayList<SingleItemComment> comments = new ArrayList<>();




                    //TODO: Change the image of GROUP
                    for (int i = 0; i < comment_List.size(); i++) {
                        Log.d("comment", comment_List.get(i).getDate());
                        SingleItemComment comment = new SingleItemComment(group_List.get(i).getImage() , group_List.get(i).getName(), user_List.get(i).getUserName(), group_List.get(i).getAddress(), comment_List.get(i).getContent());
                        comments.add(comment);
                    }

                    ListCommentAdapter commentAdapter = new ListCommentAdapter(getApplicationContext(), comments, comment_List);
                    listView.setAdapter(commentAdapter);

                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                    pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    listView.setLayoutManager(pLayoutManager);
                    listView.setItemAnimator(new DefaultItemAnimator());

                }
            }

            @Override
            public void onFailure(Call<CommentGetResponse> call, Throwable t) {

            }
        });

//        SingleItemComment comment = new SingleItemComment(R.drawable.dog_thang, "땡이네 콩 ", groupCommentNickName, "경기도 용인시 기흥구 흥덕중앙로", "어머나!!!!!넘모 귕영웡용 ");
//        comment_List.add(comment);

    }

    public SingleItemPost getPost() {
        return post;
    }
}