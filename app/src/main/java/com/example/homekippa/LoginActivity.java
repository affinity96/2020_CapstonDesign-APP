package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.function.Loading;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextID;
    private EditText editTextPW;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private TextView gotoSignTextview;
    private ServiceApi service;
    final Loading loading = new Loading();

    Intent intent;


    private GroupData groupData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = new Intent(getApplicationContext(), MainActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();
        service = RetrofitClient.getClient().create(ServiceApi.class);

        if (curUser != null && curUser.isEmailVerified()) {
            loading.loading(LoginActivity.this);
            getUserToken(curUser.getUid());
        }

        editTextID = findViewById(R.id.editText_ID);
        editTextPW = findViewById(R.id.editText_PW);
        buttonLogin = findViewById(R.id.button_Login);
        gotoSignTextview = findViewById(R.id.textView_gosignup);


        //버튼이 클릭되면 여기 리스너로 옴
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = editTextID.getText().toString();
                String PW = editTextPW.getText().toString();

                if (ID.isEmpty()) {
                    editTextID.setError("아이디를 입력하세요");
                } else if (PW.isEmpty()) {
                    editTextPW.setError("비밀번호를 입력하세요");
                } else {
                    loading.loading(LoginActivity.this);
                    requestLogin(ID, PW);
                }
            }
        });
        gotoSignTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void requestLogin(String ID, String PW) {
        mAuth.signInWithEmailAndPassword(ID, PW)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            curUser = mAuth.getCurrentUser();

                            if (curUser.isEmailVerified()) {
                                getUserToken(curUser.getUid());
                            } else {
                                mAuth.signOut();
                                Toast.makeText(getApplicationContext(), "이메일 인증을 완료해주세요", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Exception e = task.getException();
                            Log.w("로그인", "createUserWithEmail:failure", e);
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void requestUserData(String ID, String token) {
        service.getUserData(ID, token).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    Log.d("로그인", "성공");
                    UserData userData = response.body();
//                    Log.d("cheeck user", userData.getUserBirth());
//                    Log.d("cheeck user", userData.getUserName());

                    intent.putExtra("user", userData);
                    Log.d("로그인", userData.getUserId());
                    Log.d("로그인", userData.getUserImage());

                    if (userData.getGroupId() != 0) {
                        getGroupData(userData.getGroupId());
                    } else {
                        loading.loadingEnd();
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("로그인", "에러");
                Log.e("로그인", t.getMessage());
                loading.loadingEnd();
                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getGroupData(int ID) {

        service.getGroupData(ID).enqueue(new Callback<GroupData>() {
            @Override
            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                if (response.isSuccessful()) {
                    Log.d("그룹 확인", "성공");
                    groupData = response.body();
                    intent.putExtra("group", groupData);
                    Log.d("그룹 확인", String.valueOf(groupData.getId()));
                    loading.loadingEnd();
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<GroupData> call, Throwable t) {
                Log.d("그룹 확인", "에러");
                Log.e("그룹 확인", t.getMessage());
            }
        });
    }

    public void getUserToken(String ID){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("푸시 알림", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = "";
                        // Get new FCM registration token
                        if(!task.getResult().isEmpty()) {
                            token = task.getResult();
                        }

                        requestUserData(ID, token);
                        // Log and toast
                        Log.d("푸시 알림", "토큰 수신: " + token);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}