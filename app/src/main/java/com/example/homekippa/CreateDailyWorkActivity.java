package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.homekippa.data.CreateDailyWorkData;
import com.example.homekippa.data.CreateDailyWorkResponse;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;
import com.example.homekippa.network.ServiceApi;

import java.time.LocalTime;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDailyWorkActivity extends AppCompatActivity {

    private EditText editText_dailyWorkName;
    private EditText editText_dailyWorkDesc;
    private EditText editText_dailyWorkTime;
    private EditText editText_dailyWorkAlarm;
    private Button button_gotocreateDailyWork;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_daily_work);

        editText_dailyWorkName = findViewById(R.id.editText_dailyWorkName);
        editText_dailyWorkDesc = findViewById(R.id.editText_dailyWorkDesc);
        editText_dailyWorkTime = findViewById(R.id.editText_dailyWorkTime);
        editText_dailyWorkAlarm = findViewById(R.id.editText_dailyWorkAlarm);
        button_gotocreateDailyWork = findViewById(R.id.button_gotocreateDailyWork);

        editText_dailyWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker=new TimePickerDialog(CreateDailyWorkActivity.this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editText_dailyWorkTime.setText(String.format("%02d:%02d", hourOfDay,minute));

                    }
                }, Calendar.HOUR_OF_DAY,Calendar.MINUTE,false);
                timePicker.show();
            }
        });

        editText_dailyWorkAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker=new TimePickerDialog(CreateDailyWorkActivity.this, android.R.style.Theme_Holo_Light_Dialog,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editText_dailyWorkAlarm.setText(String.format("%02d:%02d", hourOfDay,minute));
                    }
                }, Calendar.HOUR_OF_DAY,Calendar.MINUTE,false);
                timePicker.show();
            }
        });

//        button_gotocreateDailyWork.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createDailyWork(new createDailyWorkData());
//            }
//        });

    }

    private void createDailyWork(CreateDailyWorkData data) {
        service.createDailyWork(data).enqueue(new Callback<CreateDailyWorkResponse>() {
            @Override
            public void onResponse(Call<CreateDailyWorkResponse> call, Response<CreateDailyWorkResponse> response) {
                CreateDailyWorkResponse result = response.body();

                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CreateDailyWorkResponse> call, Throwable t) {
                Toast.makeText(CreateDailyWorkActivity.this, "일과추가 에러 발생", Toast.LENGTH_LONG).show();
                Log.e("일과추가 에러 발생", t.getMessage());
                t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌
            }


        });
    }
}