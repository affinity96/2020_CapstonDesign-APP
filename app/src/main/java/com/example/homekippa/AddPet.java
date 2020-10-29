package com.example.homekippa;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddPet extends AppCompatActivity {

    private EditText editText_petReg_num;


    @SuppressLint("WrongViewCast")
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_add_pet);

        editText_petReg_num = findViewById(R.id.editText_petReg_num);





    }




}
