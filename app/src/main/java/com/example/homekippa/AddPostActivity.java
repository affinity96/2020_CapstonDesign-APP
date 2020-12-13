package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.data.AddPostData;
import com.example.homekippa.data.AddPostResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {


    private ServiceApi service;

    private EditText editText_postTitle;
    private EditText editText_postContent;
    private Button button_CompletePost;
    private Editable postContent;
    private FrameLayout frameLayout_post;
    private ImageView post_image;
    private ImageButton button_cancel_post_image;
    private ImageButton button_Add_Post_Img;


    private TextView textView_postScope;
    private Spinner spinner_scope;
    private String scope;

    private static final String TAG = "addPost";
    private File tempFile;
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        editText_postTitle = (EditText) this.findViewById(R.id.editText_addPostTitle);
        editText_postContent = (EditText) this.findViewById(R.id.editText_postContent);
        button_CompletePost = (Button) this.findViewById(R.id.button_CompletePost);
        postContent = editText_postContent.getText();
        frameLayout_post = (FrameLayout) this.findViewById(R.id.frameLayout_post);
        post_image = (ImageView) this.findViewById(R.id.post_image);
        button_cancel_post_image = (ImageButton) this.findViewById(R.id.button_cancel_post_image);
        button_Add_Post_Img = (ImageButton) this.findViewById(R.id.button_Add_Post_Img);


        textView_postScope = this.findViewById(R.id.textView_postScope);
        spinner_scope = this.findViewById(R.id.spinner_scope);

        Intent intent = getIntent();

        UserData userData = (UserData) intent.getExtras().get("userData");
        GroupData groupData = (GroupData) intent.getExtras().get("groupData");

        Log.d("ted","tedPermission()");

        button_Add_Post_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermission) goToAlbum();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        button_cancel_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout_post.setVisibility(View.INVISIBLE);
                post_image.setImageBitmap(null);
                tempFile = null;
            }
        });

        button_CompletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost(groupData.getId(), userData.getUserId(), postContent.toString(), editText_postTitle.getText().toString(), groupData.getArea(), scope);
            }
        });

        spinner_scope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("spinner",parent.getItemAtPosition(position).toString());
                String checkedScope = parent.getItemAtPosition(position).toString();

                if(checkedScope.equals("전체공개")){
                    scope= "wholeScope";
                }else if(checkedScope.equals("팔로우공개")){
                    scope= "followScope";
                }else if(checkedScope.equals("비공개")){
                    scope = "closedScope";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        } else {
            if (requestCode == PICK_FROM_ALBUM) {

                Uri photoUri = intent.getData();
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

                Cursor cursor = null;

                try {
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index));

                    Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                setImage();

            }
        }
    }



    private void setImage() {
        frameLayout_post.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        post_image.setImageBitmap(originalBm);

    }

    public void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void addPost(int groupId, String userId, String content, String title, String area, String scope) {

        if (tempFile != null) {
            String str_groupId = String.valueOf(groupId);
            String str_area = String.valueOf(area);
            HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

            RequestBody group_Id = RequestBody.create(MediaType.parse("text/plain"), str_groupId);
            data.put("groupId", group_Id);
            RequestBody user_Id = RequestBody.create(MediaType.parse("text/plain"), userId);
            data.put("userId", user_Id);
            RequestBody Title = RequestBody.create(MediaType.parse("text/plain"), title);
            data.put("title", Title);
            RequestBody Content = RequestBody.create(MediaType.parse("text/plain"), content);
            data.put("content", Content);
            RequestBody area_ = RequestBody.create(MediaType.parse("text/plain"), str_area);
            data.put("area", area_);
            RequestBody scope_ = RequestBody.create(MediaType.parse("text/plain"), scope);
            data.put("scope", scope_);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            service.addPostWithPhoto(data, uploadFile).enqueue(new Callback<AddPostResponse>() {

                @Override
                public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                    AddPostResponse result = response.body();
                    Log.d("코드", String.format("%d", result.getCode()));
                    if (result.getCode() == 200) {
                        Toast.makeText(AddPostActivity.this, "게시글이 성공적으로 등록되었습니다!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<AddPostResponse> call, Throwable t) {
                    Toast.makeText(AddPostActivity.this, "게시글 추가 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("게시글 추가 에러 발생", t.getMessage());
                    t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌

                }
            });
        } else {
            AddPostData data = new AddPostData(groupId, userId, title, content, area, scope);

            service.addPost(data).enqueue(new Callback<AddPostResponse>() {

                @Override
                public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                    AddPostResponse result = response.body();
                    Log.d("코드", String.format("%d", result.getCode()));
                    if (result.getCode() == 200) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<AddPostResponse> call, Throwable t) {
                    Toast.makeText(AddPostActivity.this, "게시글 추가 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("게시글 추가 에러 발생", t.getMessage());
                    t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌

                }
            });
        }
    }
}