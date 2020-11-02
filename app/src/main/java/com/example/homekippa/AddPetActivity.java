package com.example.homekippa;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddPetActivity extends AppCompatActivity {

    private EditText editText_petReg_num;
    private Button button_petReg;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_add_pet);

        editText_petReg_num = findViewById(R.id.editText_petReg_num);
        button_petReg = findViewById(R.id.button_petReg);

        button_petReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petReg = editText_petReg_num.getText().toString();
                Log.d("heelo",petReg);

            }
        });










    }




}
