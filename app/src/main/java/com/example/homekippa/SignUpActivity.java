package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.data.UidData;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.ui.datepicker.DatePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;

import org.w3c.dom.Text;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText phone_login;
    private EditText email_login;
    private EditText pw_login;
    private TextView birth_login;
    private EditText name_login;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button button_SignUp = findViewById(R.id.button_SignUp);
        phone_login = findViewById(R.id.editText_Phone);
        email_login = findViewById(R.id.editText_email);
        pw_login = findViewById(R.id.editText_PW);
        birth_login = findViewById(R.id.text_Birth);
        name_login = findViewById(R.id.editText_Name);
        mAuth = FirebaseAuth.getInstance();

        service = RetrofitClient.getClient().create(ServiceApi.class);
/*
        button_ID_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_login.getText().toString().trim();
                if(id.isEmpty()){
                    id_login.setError("ID를 입력하세요");
                }
                else{
                    startCheckUid(new UidData(id));
                }
            }
        });*/

        birth_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = phone_login.getText().toString().trim();
                final String email = email_login.getText().toString().trim();
                final String pw = pw_login.getText().toString().trim();
                final String birth = birth_login.getText().toString().trim();
                final String name = name_login.getText().toString().trim();

                if(email.isEmpty()){
                    email_login.setError("생년월일을 입력하세요");
                }
                else if(phone.isEmpty()) {
                    phone_login.setError("PW를 입력하세요");
                }
                else if(pw.isEmpty()) {
                    pw_login.setError("PW를 입력하세요");
                }
                else if(birth.isEmpty()){
                    birth_login.setError("생년월일을 입력하세요");
                }
                else if(name.isEmpty()){
                    name_login.setError("닉네임을 입력하세요");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("회원 가입", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startSignUp(new SignUpData(user.getUid(), phone, email, name, birth));
                            } else {
                                // If sign in fails, display a message to the user.
                                Exception e = task.getException();
                                Log.w("회원 가입", "createUserWithEmail:failure", e);
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        final CheckBox checkbox_Agree = (CheckBox)findViewById(R.id.checkbox_Agree);
        checkbox_Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox_Agree.isChecked()== true){
                    Toast.makeText(getApplicationContext(),"이용 약관에 동의하셨습니다", Toast.LENGTH_SHORT).show(); // 토스트 : 팝업으로 송출

                }
            }
        });

    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "-" + month_string + "-" + day_string);

        birth_login.setText(dateMessage);
    }

    private void startSignUp(SignUpData data) {
        service.userSignUp(data).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                SignUpResponse result = response.body();
                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
//                        showProgress(false);

                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
                t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌
//                        showProgress(false);
            }
        });
    }
}
