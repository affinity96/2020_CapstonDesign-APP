package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.homekippa.ui.searchAddress.searchAddress;

public class StartActivity extends AppCompatActivity {
    Button button_Login;
    Button button_Sign;
    Button button_searchAddress;
    Button button_createNewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button_Login = findViewById(R.id.button_Login);
        button_Sign = findViewById(R.id.button_Sign);
        button_searchAddress =findViewById(R.id.button_searchAddress);
        button_createNewGroup=findViewById(R.id.button_createNewGroup);


        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        button_Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        button_searchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), searchAddress.class);
                startActivity(intent);
            }
        });

        button_createNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });



    }
}
