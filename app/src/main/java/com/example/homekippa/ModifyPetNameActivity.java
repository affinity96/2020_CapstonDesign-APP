package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.ModifyGroupResponse;
import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPetNameActivity extends AppCompatActivity {

    public ModifyPetNameActivity() {
    }

    private static final String TAG = " ModifyPetName";

    private EditText editText_petmodify_Name;
    private Button button_petname_next;
    private ServiceApi service;
    private String petName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet_name);
        editText_petmodify_Name = findViewById(R.id.editText_petmodify_Name);
        button_petname_next = findViewById(R.id.button_petname_next);
        petName = getIntent().getStringExtra("name");
        editText_petmodify_Name.setText(petName);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_petname_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petName = editText_petmodify_Name.getText().toString();
                int id = getIntent().getIntExtra("id", 0);
                Next(id, petName);
            }
        });
    }

    private void Next(int id, String name) {
        service.modifyPetName(id, name).enqueue(new Callback<ModifyPetResponse>() {
            @Override
            public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                ModifyPetResponse result = response.body();

                if (result.getCode() == 200) {
                    Log.d("??", petName);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                Toast.makeText(ModifyPetNameActivity.this, "이름 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        Log.d("????", petName);
        intent.putExtra("petName", petName);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}