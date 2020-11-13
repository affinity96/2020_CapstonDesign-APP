package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homekippa.data.AddPostData;
import com.example.homekippa.data.AddPostResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {


    private ServiceApi service;

    private EditText editText_postContent;
    private Button button_CompletePost;
    private Editable postContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        editText_postContent = (EditText)this.findViewById(R.id.editText_postContent);
        button_CompletePost = (Button)this.findViewById(R.id.button_CompletePost);
        postContent = editText_postContent.getText();

        Intent intent = getIntent();

        UserData userData = (UserData) intent.getExtras().get("userData");
        GroupData groupData = (GroupData) intent.getExtras().get("groupData");

        button_CompletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPost(new AddPostData(groupData.getId(), userData.getUserId(), "im-si title", postContent.toString(),"im-si img"));
            }
        });

    }

    private void addPost(AddPostData data) {
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