package com.example.homekippa;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.AddPetData;
import com.example.homekippa.data.AddPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPetActivity extends AppCompatActivity {

    private EditText editText_petReg_num;
    private Button button_petReg;
    private ServiceApi service;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_add_pet);

        editText_petReg_num = findViewById(R.id.editText_petReg_num);
        button_petReg = findViewById(R.id.button_petReg);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        editText_petReg_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_petReg_num.setText("");
            }
        });

        button_petReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String petReg = editText_petReg_num.getText().toString().trim();

                if(petReg.isEmpty()){
                    editText_petReg_num.setText("숫자를 입력하세요.");

                }
                else{
                    addPet(new AddPetData(petReg));
//                    Intent intent =
                }
            }
        });
    }

    private void addPet(AddPetData data) {
        Log.i("create2","create");
        service.addPetReg(data).enqueue(new Callback<AddPetResponse>() {
            @Override
            public void onResponse(Call<AddPetResponse> call, Response<AddPetResponse> response) {
                AddPetResponse result = response.body();
//                Log.d("haha", "1");
//                Log.d("happy", result.getMessage());
                Toast.makeText(AddPetActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();

                if(result.getCode() == 200){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddPetResponse> call, Throwable t) {
                Toast.makeText(AddPetActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//                Log.e("createGroup error",t.getMessage());
                t.printStackTrace();

            }
        });
    }


}
