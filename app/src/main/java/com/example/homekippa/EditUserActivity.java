package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.homekippa.data.EditDailyWorkData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.datepicker.DatePickerFragment;

public class EditUserActivity extends AppCompatActivity {
    private ServiceApi service;

    private EditText email_login;
    private EditText phone_login;
    private TextView birth_login;
    private EditText name_login;

    private Button button_gotoEditUser;

    private Editable userEmail;
    private Editable userName;
    private Editable userPhone;
    private Editable userBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Intent intent = getIntent();
        UserData userData = (UserData) intent.getExtras().get("userData");

        service = RetrofitClient.getClient().create(ServiceApi.class);

        email_login = (EditText) this.findViewById(R.id.editText_email);
        phone_login = (EditText) this.findViewById(R.id.editText_Phone);
        birth_login = (TextView) this.findViewById(R.id.text_Birth);
        name_login = (EditText) this.findViewById(R.id.editText_Name);
        button_gotoEditUser = (Button) this.findViewById(R.id.button_gotoEditUser);



        String id = userData.getUserId();
        String name = userData.getUserName();
        String phone = userData.getUserPhone();
        String birth = userData.getUserBirth();
        String email = userData.getUserEmail();

        email_login.setText(email);
        phone_login.setText(phone);
        birth_login.setText(birth);
        name_login.setText(name);

        userEmail = email_login.getText();
        userName = name_login.getText();
        userPhone = phone_login.getText();


        birth_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });


        button_gotoEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("아", "머냐ㅐ");

               // editUser(new EditDailyWorkData(id, dailyWorkName.toString(), dailyWorkDesc.toString(), dailyWorkTime.toString(), dailyWorkAlarm.toString()));

            }
        });





    }


    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "-" + month_string + "-" + day_string);

        birth_login.setText(dateMessage);
    }


}