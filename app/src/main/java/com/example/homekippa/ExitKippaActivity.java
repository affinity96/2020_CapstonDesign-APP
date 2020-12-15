package com.example.homekippa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.ExitResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExitKippaActivity extends AppCompatActivity {

    private Button button_extiKippaService;
    private GroupData groupData;
    private ServiceApi service;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_exit_kippa);
        Intent intent = getIntent();
        UserData userData = (UserData) intent.getExtras().get("userData");
        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_extiKippaService = findViewById(R.id.button_ExitKippaService);


        button_extiKippaService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("정말 해피투게독을 탈퇴하시겠습니까?");
                builder.setMessage("회원님의 정보가 모두 삭제됩니다.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String userId = userData.getUserId();
                                Log.d("클릭됐는데",userId);
                                service.userExit(userId).enqueue(new Callback<ExitResponse>() {
                                    @Override
                                    public void onResponse(Call<ExitResponse> call, Response<ExitResponse> response) {
                                        Log.d("회원 퇄퇴 성공?", response.toString());
                                        Toast.makeText(view.getContext(), "그동안 해피투게독을 사용해 주셔서 감사합니다. ", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ExitResponse> call, Throwable t) {
                                        Log.e("회원 탈퇴 에러발생", t.getMessage());
                                        Toast.makeText(view.getContext(), "회원탈퇴에러발생 ", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(view.getContext(), "계속해서 해피투게독을 사용해보세요", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.show();

            }
        });
    }
}