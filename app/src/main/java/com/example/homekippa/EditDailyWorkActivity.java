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
import com.example.homekippa.data.EditDailyWorkData;
import com.example.homekippa.data.EditDailyWorkResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;


import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDailyWorkActivity extends AppCompatActivity {
    private ServiceApi service;
    private EditText editText_dailyWorkName;
    private EditText editText_dailyWorkDesc;
    private EditText editText_dailyWorkTime;
    private EditText editText_dailyWorkAlarm;

    private Button button_gotoEditDailyWork;

    private Editable dailyWorkName;
    private Editable dailyWorkDesc;
    private Editable dailyWorkTime;
    private Editable dailyWorkAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_work);
        Intent intent = getIntent();
        service = RetrofitClient.getClient().create(ServiceApi.class);

        editText_dailyWorkName = (EditText) this.findViewById(R.id.editText_dailyWorkName);
        editText_dailyWorkDesc = (EditText) this.findViewById(R.id.editText_dailyWorkDesc);
        editText_dailyWorkTime = (EditText) this.findViewById(R.id.editText_dailyWorkTime);
        editText_dailyWorkAlarm = (EditText) this.findViewById(R.id.editText_dailyWorkAlarm);
        button_gotoEditDailyWork = (Button) this.findViewById(R.id.button_gotoEditDailyWork);

        int id = (int) intent.getExtras().get("id");
        String title = (String) intent.getExtras().get("title");
        String desc = (String) intent.getExtras().get("desc");
        String time = (String) intent.getExtras().get("time");
        String alarm = (String) intent.getExtras().get("alarm");

        editText_dailyWorkName.setText(title);
        editText_dailyWorkDesc.setText(desc);
        editText_dailyWorkTime.setText(time);
        editText_dailyWorkAlarm.setText(alarm);


        dailyWorkName = editText_dailyWorkName.getText();
        dailyWorkDesc = editText_dailyWorkDesc.getText();
        dailyWorkTime = editText_dailyWorkTime.getText();
        dailyWorkAlarm = editText_dailyWorkAlarm.getText();

        button_gotoEditDailyWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("아", "머냐ㅐ");
                dailyWorkTime = editText_dailyWorkTime.getText();
                dailyWorkAlarm = editText_dailyWorkAlarm.getText();


                editDailyWork(new EditDailyWorkData(id, dailyWorkName.toString(), dailyWorkDesc.toString(), dailyWorkTime.toString(), dailyWorkAlarm.toString()));

            }
        });

        editText_dailyWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePicker = new TimePickerDialog(EditDailyWorkActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
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
                TimePickerDialog timePicker = new TimePickerDialog(EditDailyWorkActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editText_dailyWorkAlarm.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                timePicker.show();
            }
        });

    }

    private void editDailyWork(EditDailyWorkData data) {

        service.editDailyWork(data).enqueue(new Callback<EditDailyWorkResponse>() {

            @Override
            public void onResponse(Call<EditDailyWorkResponse> call, Response<EditDailyWorkResponse> response) {
                EditDailyWorkResponse result = response.body();
                if (result.getCode() == 200) {
                    Toast.makeText(EditDailyWorkActivity.this, "일과가 성공적으로 수정되었습니다!", Toast.LENGTH_LONG).show();

                    finish();
                }
            }

            @Override
            public void onFailure(Call<EditDailyWorkResponse> call, Throwable t) {
                Toast.makeText(EditDailyWorkActivity.this, "일과수정 에러 발생", Toast.LENGTH_LONG).show();
                Log.e("일과수정 에러 발생", t.getMessage());
                t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌

            }


        });
    }
}