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

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button_SignUp;
    private EditText id_login;
    private EditText pw_login;
    private String TAG = "Sign In";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        button_SignUp = findViewById(R.id.button_SignUp);
        id_login = findViewById(R.id.editText_ID);
        pw_login = findViewById(R.id.editText_PW);
        mAuth = FirebaseAuth.getInstance();

        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_login.getText().toString().trim();
                String pw = pw_login.getText().toString().trim();
                Log.d(TAG, id);
                Log.d(TAG, pw);

                mAuth.createUserWithEmailAndPassword(id, pw)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else{
                                Log.w(TAG, "회원가입 실패", task.getException());
                                Toast.makeText(getApplicationContext(), "회원가입 오류", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }
}
