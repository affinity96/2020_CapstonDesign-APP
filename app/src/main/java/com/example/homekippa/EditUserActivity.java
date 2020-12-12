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
import android.widget.Toast;

import com.example.homekippa.data.EditDailyWorkData;
import com.example.homekippa.data.EditDailyWorkResponse;
import com.example.homekippa.data.EditUserData;
import com.example.homekippa.data.EditUserResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.datepicker.DatePickerFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    private ServiceApi service;
    private MainActivity main;

    private EditText email_login;
    private EditText phone_login;

    private EditText name_login;

    private Button button_gotoEditUser;

    private Editable userEmail;
    private Editable userName;
    private Editable userPhone;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Intent intent = getIntent();
        userData = (UserData) intent.getExtras().get("userData");
        service = RetrofitClient.getClient().create(ServiceApi.class);

        email_login = (EditText) this.findViewById(R.id.editText_email);
        phone_login = (EditText) this.findViewById(R.id.editText_Phone);

        name_login = (EditText) this.findViewById(R.id.editText_Name);
        button_gotoEditUser = (Button) this.findViewById(R.id.button_gotoEditUser);

        main = new MainActivity();


        String id = userData.getUserId();
        String name = userData.getUserName();
        String phone = userData.getUserPhone();

        String email = userData.getUserEmail();

        email_login.setText(email);
        phone_login.setText(phone);

        name_login.setText(name);

        userEmail = email_login.getText();
        userName = name_login.getText();
        userPhone = phone_login.getText();


        button_gotoEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("아", "머냐ㅐ");

                editUser(new EditUserData(id, userName.toString(), userPhone.toString()));

            }
        });


    }


    private void editUser(EditUserData data) {

        service.userEdit(data).enqueue(new Callback<EditUserResponse>() {

            @Override
            public void onResponse(Call<EditUserResponse> call, Response<EditUserResponse> response) {
                EditUserResponse result = response.body();
                if (result.getCode() == 200) {
                    Toast.makeText(EditUserActivity.this, "사용자 정보가 성공적으로 수정되었습니다!", Toast.LENGTH_LONG).show();
                    service.getUserData(userData.getUserId(), userData.getUserTokken()).enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if (response.isSuccessful()) {
                                UserData userData = response.body();
                                main.setUserData(userData);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {

                        }
                    });
                    finish();
                }
            }

            @Override
            public void onFailure(Call<EditUserResponse> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, "사용자 정보 수정 에러 발생", Toast.LENGTH_LONG).show();
                Log.e("사용자 정보 수정 에러 발생", t.getMessage());
                t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌

            }


        });
    }


}