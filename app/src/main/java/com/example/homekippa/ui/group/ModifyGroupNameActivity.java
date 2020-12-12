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

public class ModifyGroupNameActivity extends AppCompatActivity {

    public ModifyGroupNameActivity() {
    }

    private static final String TAG = " ModifyGroupName";

    private EditText editText_groupmodify_Name;
    private Button button_name_next;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group_name);
        editText_groupmodify_Name = findViewById(R.id.editText_groupmodify_Name);
        button_name_next = findViewById(R.id.button_name_next);
        String name = getIntent().getStringExtra("name");
        editText_groupmodify_Name.setText(name);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_name_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = editText_groupmodify_Name.getText().toString();
                int id = getIntent().getIntExtra("id", 0);
                Next(id, groupName);
            }
        });
    }

    private void Next(int id, String name) {
        service.modifyGroupName(id, name).enqueue(new Callback<ModifyGroupResponse>() {
            @Override
            public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                ModifyGroupResponse result = response.body();

                if (result.getCode() == 200) {

                        finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                Toast.makeText(ModifyGroupNameActivity.this, "그룹이름 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}