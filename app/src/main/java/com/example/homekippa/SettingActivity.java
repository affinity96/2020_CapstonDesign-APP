package com.example.homekippa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.ExitResponse;
import com.example.homekippa.data.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    private UserData userData;
    private Button button_ExitKippa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        button_ExitKippa = findViewById(R.id.button_ExitKippa);
        Intent intent = getIntent();
        UserData userData = (UserData) intent.getExtras().get("userData");

        button_ExitKippa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ExitKippaActivity.class);
                intent.putExtra("userData", userData);
                 startActivity(intent);

            }
        });
    }
}
