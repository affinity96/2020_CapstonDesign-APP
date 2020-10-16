package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.data.CreateGroupData;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.searchAddress.searchAddress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText  editText_groupName;
    private Button button_createGroup;
    private TextView moveToSearchAddress;
    private ServiceApi service;
    private ImageView groupProfilePhoto;
    private String imgPath;
    private Uri imgUrl;
    private File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editText_groupName = findViewById(R.id.editText_groupName);
        button_createGroup = findViewById(R.id.button_createGroup);
        moveToSearchAddress = findViewById(R.id.moveToSearchAddress);
        groupProfilePhoto = findViewById(R.id.imageView_groupImage);



        service = RetrofitClient.getClient().create(ServiceApi.class);

        editText_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_groupName.setText("");
            }
        });

        moveToSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), searchAddress.class);
                startActivityForResult(intent, 0);
            }
        });

        groupProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


        button_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = editText_groupName.getText().toString();
                Log.d("creategroup","here");

                final String groupAddress = moveToSearchAddress.getText().toString();

                if(groupName.isEmpty()){
                    editText_groupName.setText("GroupName을 입력하세요");
                }
                else if(groupAddress.isEmpty()){
                    moveToSearchAddress.setText("주소를 입력하세요");
                }
                else{
                    createGroup(new CreateGroupData(groupName, groupAddress,  imgUrl));

                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(resultCode != RESULT_OK)
        {
            Log.d("주소", "error");
            return;
        }
        else{
            if(requestCode == 0) {
                //수신성공 출력
                String result = intent.getStringExtra("address");
                moveToSearchAddress.setText(result);
            }
            else{
                try{
                    InputStream in = getContentResolver().openInputStream(intent.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);

                    imgUrl = intent.getData();
                    imgPath = getRealPathFromUrl(imgUrl);

                    file = new File(imgPath);
                    in.close();


                    groupProfilePhoto.setImageBitmap(img);
                }catch(Exception e)
                {
                    Log.d("exception", String.valueOf(e));
                }
            }
        }
    }

    private String getRealPathFromUrl(Uri imageUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, proj, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }





    private void createGroup(CreateGroupData data){
        Log.i("create","create");
        service.groupCreate(data).enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                CreateGroupResponse result =response.body();
                Toast.makeText(CreateGroupActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();

                if(result.getCode() == 200){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                Toast.makeText(CreateGroupActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("createGroup error",t.getMessage());
                t.printStackTrace();

            }
        });
    }


}