package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.data.CreateGroupData;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.searchAddress.searchAddress;

import java.io.File;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainGroupActivity extends AppCompatActivity {
    //group 생성이 된 상태라면
    // layout
    private LinearLayout Layout_NoGroup;
    private LinearLayout Layout_YesGroup;
    private Button button_GoCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_group);
        Layout_NoGroup=(LinearLayout)findViewById(R.id.linearLayout_nogroup);
        button_GoCreateGroup=(Button)findViewById(R.id.button_gotocreateGroup);


        //create 상태 확인
        boolean groupCreated=false;
        if(groupCreated){
            Layout_NoGroup.setVisibility(View.GONE);
        }else {
            Layout_NoGroup.setVisibility(View.VISIBLE);

        }

        button_GoCreateGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainGroupActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });




    }



}
