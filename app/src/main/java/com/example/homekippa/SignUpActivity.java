package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button_SignUp;
    private EditText id_login;
    private EditText pw_login;
    private EditText age_login;
    private EditText name_login;
    private String TAG = "Sign In";
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        button_SignUp = findViewById(R.id.button_SignUp);
        id_login = findViewById(R.id.editText_ID);
        pw_login = findViewById(R.id.editText_PW);
        age_login = findViewById(R.id.editText_Age);
        name_login = findViewById(R.id.editText_Name);
        mAuth = FirebaseAuth.getInstance();

        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_login.getText().toString().trim();
                String pw = pw_login.getText().toString().trim();
                final String age = age_login.getText().toString().trim();
                final String name = name_login.getText().toString().trim();
                Log.d(TAG, id);
                Log.d(TAG, pw);

                mAuth.createUserWithEmailAndPassword(id, pw)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                // ID 토큰 가져와서 서버로 전송하기 (회원가입과 동시에 로그인이 되어야하는듯)
                                user.getIdToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    String idToken = task.getResult().getToken();
                                                    startSignUp(new SignUpData(age, name, idToken));
                                                } else {
                                                    Log.w(TAG, "토큰 가져오기 실패", task.getException());
                                                    Toast.makeText(getApplicationContext(), "토큰 가져오기 오류", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            } else{
                                Log.w(TAG, "회원가입 실패", task.getException());
                                Toast.makeText(getApplicationContext(), "회원가입 오류", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
        });
    }
}
