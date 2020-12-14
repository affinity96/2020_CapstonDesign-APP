package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPetNeuteringActivity extends AppCompatActivity {

    public ModifyPetNeuteringActivity() {
    }

    private static final String TAG = " ModifyPetNeutering";

    private Button button_petneutering_next;
    private CheckBox checkbox_modify_no;
    private CheckBox checkbox_modify_yes;
    private ServiceApi service;
    private String petNeutrality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet_neutrality);
        checkbox_modify_yes = findViewById(R.id.checkbox_modify_yes);
        checkbox_modify_no = findViewById(R.id.checkbox_modify_no);
        button_petneutering_next = findViewById(R.id.button_petneutering_next);
        int neutrality = getIntent().getIntExtra("neutrality", 0);

        if(neutrality == 1){
            checkbox_modify_yes.setChecked(true);
            petNeutrality = "중성";
        } else {
            checkbox_modify_no.setChecked(true);
            petNeutrality = "중성화 안함";
        }

        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_petneutering_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkbox_modify_yes.isChecked()){
                    petNeutrality = "중성";
                }
                else if(checkbox_modify_no.isChecked()){
                    petNeutrality = "중성화 안함";
                }

                int id = getIntent().getIntExtra("id", 0);
                Next(id, petNeutrality);
            }
        });
    }

    public void onCheckboxClicked_petNeutralization(View v) {
        switch (v.getId()){
            case R.id.checkbox_modify_yes:
                checkbox_modify_yes.setChecked(true);
                checkbox_modify_no.setChecked(false);
                break;

            case R.id.checkbox_modify_no:
                checkbox_modify_yes.setChecked(false);
                checkbox_modify_no.setChecked(true);
                break;

        }

    }

    private void Next(int id, String neutrality) {
        service.modifyPetNeutering(id, neutrality).enqueue(new Callback<ModifyPetResponse>() {
            @Override
            public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                ModifyPetResponse result = response.body();

                if (result.getCode() == 200) {

                        finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                Toast.makeText(ModifyPetNeuteringActivity.this, "이름 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        if (petNeutrality == "중성") {
            intent.putExtra("petNeutrality", 1);
        } else {
            intent.putExtra("petNeutrality", 0);
        }
        setResult(RESULT_OK, intent);
        super.finish();
    }
}