package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.R;
import com.example.homekippa.data.ModifyGroupResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyGroupIntroActivity extends AppCompatActivity {

    public ModifyGroupIntroActivity() {
    }

    private static final String TAG = " ModifyGroupName";

    private EditText editText_groupmodify_info;
    private Button button_info_next;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group_info);
        editText_groupmodify_info = findViewById(R.id.editText_groupmodify_info);
        button_info_next = findViewById(R.id.button_info_next);
        String intro = getIntent().getStringExtra("introduction");
        editText_groupmodify_info.setText(intro);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_info_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intro = editText_groupmodify_info.getText().toString();
                int id = getIntent().getIntExtra("id", 0);
                Next(id, intro);
            }
        });
    }

    private void Next(int id, String intro) {
        service.modifyGroupIntro(id, intro).enqueue(new Callback<ModifyGroupResponse>() {
            @Override
            public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                ModifyGroupResponse result = response.body();

                if (result.getCode() == 200) {

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                Toast.makeText(ModifyGroupIntroActivity.this, "그룹설명 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}