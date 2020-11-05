package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.homekippa.data.UserData;
import com.example.homekippa.function.Loading;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();
        service = RetrofitClient.getClient().create(ServiceApi.class);

        if(curUser != null && curUser.isEmailVerified()){
            loading.loading(LoginActivity.this);
            requestUserData(curUser.getUid());
        }

        editTextID = findViewById(R.id.editText_ID);
        editTextPW = findViewById(R.id.editText_PW);
        buttonLogin = findViewById(R.id.button_Login);
        gotoSignTextview=findViewById(R.id.textView_gosignup);


        //버튼이 클릭되면 여기 리스너로 옴
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = editTextID.getText().toString();
                String PW = editTextPW.getText().toString();

                if(ID.isEmpty()){
                    editTextID.setError("아이디를 입력하세요");
                }
                else if(PW.isEmpty()){
                    editTextPW.setError("비밀번호를 입력하세요");
                }
                else {
                    loading.loading(LoginActivity.this);
                    requestLogin(ID, PW);
                }
            }
        });
        gotoSignTextview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void requestLogin(String ID, String PW){
        mAuth.signInWithEmailAndPassword(ID, PW)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            curUser = mAuth.getCurrentUser();
                            if(curUser.isEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                                requestUserData(curUser.getUid());
                            }
                            else{
                                mAuth.signOut();
                                Toast.makeText(getApplicationContext(), "이메일 인증을 완료해주세요", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Exception e = task.getException();
                            Log.w("로그인", "createUserWithEmail:failure", e);
                            Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void requestUserData(String ID) {
        service.getUserData(ID).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    Log.d("로그인", "성공");
                    UserData userData = response.body();
                    loading.loadingEnd();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("user", userData);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("로그인", "에러");
                Log.e("로그인", t.getMessage());
            }
        });
    }
}