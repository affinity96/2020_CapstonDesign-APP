package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.homekippa.network.ServiceApi;

public class EditUserActivity extends AppCompatActivity {
    private ServiceApi service;
    private EditText email_login;

    private EditText phone_login;
    private EditText pw_login;
    private TextView birth_login;
    private EditText name_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
    }
}