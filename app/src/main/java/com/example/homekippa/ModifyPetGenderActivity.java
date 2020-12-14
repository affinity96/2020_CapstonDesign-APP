package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPetGenderActivity extends AppCompatActivity {

    public ModifyPetGenderActivity() {
    }

    private static final String TAG = " ModifyPetGender";

    private Button button_petgender_next;
    private CheckBox checkbox_modify_female;
    private CheckBox checkbox_modify_male;
    private ServiceApi service;
    private String petGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet_gender);
        checkbox_modify_female = findViewById(R.id.checkbox_modify_female);
        checkbox_modify_male = findViewById(R.id.checkbox_modify_male);
        button_petgender_next = findViewById(R.id.button_petgender_next);
        int gender = getIntent().getIntExtra("gender", 0);

        if(gender == 1){
            checkbox_modify_male.setChecked(true);
            petGender = "수컷";
        } else {
            checkbox_modify_female.setChecked(true);
            petGender = "암컷";
        }

        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_petgender_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox_modify_male.isChecked()){
                    petGender = "수컷";
                }
                else if(checkbox_modify_female.isChecked()){
                    petGender = "암컷";
                }

                int id = getIntent().getIntExtra("id", 0);
                Next(id, petGender);
            }
        });
    }

    public void onCheckboxClicked_petGender(View v) {
        switch (v.getId()){
            case R.id.checkbox_modify_male:
                checkbox_modify_male.setChecked(true);
                checkbox_modify_female.setChecked(false);
                break;

            case R.id.checkbox_modify_female:
                checkbox_modify_male.setChecked(false);
                checkbox_modify_female.setChecked(true);
                break;

        }

    }

    private void Next(int id, String gender) {
        service.modifyPetGender(id, gender).enqueue(new Callback<ModifyPetResponse>() {
            @Override
            public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                ModifyPetResponse result = response.body();

                if (result.getCode() == 200) {

                        finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                Toast.makeText(ModifyPetGenderActivity.this, "성별 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        if (petGender == "수컷") {
            intent.putExtra("petGender", 1);
        } else {
            intent.putExtra("petGender", 0);
        }
        setResult(RESULT_OK, intent);
        super.finish();
    }
}