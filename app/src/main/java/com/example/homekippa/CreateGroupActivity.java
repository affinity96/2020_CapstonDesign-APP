package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.searchAddress.searchAddress;

import static java.lang.Math.random;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText  editText_groupName;
    private Button button_createGroup;
    private TextView moveToSearchAddress;
    private ServiceApi service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editText_groupName = findViewById(R.id.editText_groupName);
        button_createGroup = findViewById(R.id.button_createGroup);
        moveToSearchAddress = findViewById(R.id.moveToSearchAddress);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        moveToSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), searchAddress.class);
                startActivity(intent);
            }
        });

        button_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = editText_groupName.getText().toString();


                //groupNameTag : 그룹 이름에 랜덤숫자를 합친 것을 의미합니다.
                String groupNameTag = makeGroupName(groupName);

            }
        });


    }

    public String makeGroupName(String groupName){

        int randNum = (int) (Math.random()*100000);

        String stringraNum =Integer.toString(randNum);

        String groupNameTag= groupName.concat(stringraNum);
        Log.d("createGroupName",groupNameTag);

        return groupNameTag;
    }


}