package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

import com.example.homekippa.MainActivity;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
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

    private Editable dailyWorkName;
    private Editable dailyWorkDesc;
    private Editable dailyWorkTime;
    private Editable dailyWorkAlarm;

    private Button button_gotocreateDailyWork;
    private GroupData groupData;
    private UserData userData;
    private ServiceApi service;
    private int petId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_daily_work);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        editText_dailyWorkName = (EditText) this.findViewById(R.id.editText_dailyWorkName);
        editText_dailyWorkDesc = (EditText) this.findViewById(R.id.editText_dailyWorkDesc);
        editText_dailyWorkTime = (EditText) this.findViewById(R.id.editText_dailyWorkTime);
        editText_dailyWorkAlarm = (EditText) this.findViewById(R.id.editText_dailyWorkAlarm);
        button_gotocreateDailyWork = (Button) this.findViewById(R.id.button_gotocreateDailyWork);

        Intent intent = getIntent();

        UserData userData = (UserData) intent.getExtras().get("userData");
        GroupData groupData = (GroupData) intent.getExtras().get("groupData");
        petId = (int) intent.getExtras().get("petId");

        dailyWorkName = editText_dailyWorkName.getText();
        dailyWorkDesc = editText_dailyWorkDesc.getText();
        dailyWorkTime = editText_dailyWorkTime.getText();
        dailyWorkAlarm = editText_dailyWorkAlarm.getText();

        editText_dailyWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePicker = new TimePickerDialog(CreateDailyWorkActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editText_dailyWorkTime.setText(String.format("%02d:%02d", hourOfDay, minute));

                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                timePicker.show();
            }
        });

        editText_dailyWorkAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(CreateDailyWorkActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editText_dailyWorkAlarm.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                timePicker.show();
            }
        });

        button_gotocreateDailyWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("아", "머냐ㅐ");
                dailyWorkTime = editText_dailyWorkTime.getText();
                dailyWorkAlarm = editText_dailyWorkAlarm.getText();
                Log.d("타임", dailyWorkTime.toString());
                Log.d("알람", dailyWorkAlarm.toString());
                Log.d("유저ㅗ이앎", "흠" + userData.getGroupId());

                Log.d("펫", "응" + petId);
                Log.d("디이엣", dailyWorkDesc.toString());
                Log.d("네임", dailyWorkName.toString());

                createDailyWork(new CreateDailyWorkData(userData.getGroupId(), petId, dailyWorkName.toString(), dailyWorkDesc.toString(), dailyWorkTime.toString(), dailyWorkAlarm.toString()));

            }
        });

    }

    private void createDailyWork(CreateDailyWorkData data) {
        service.createDailyWork(data).enqueue(new Callback<CreateDailyWorkResponse>() {

            @Override
            public void onResponse(Call<CreateDailyWorkResponse> call, Response<CreateDailyWorkResponse> response) {
                CreateDailyWorkResponse result = response.body();
                if (result.getCode() == 200) {
                    Toast.makeText(CreateDailyWorkActivity.this, "일과가 성공적으로 등록되었습니다!", Toast.LENGTH_LONG).show();

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