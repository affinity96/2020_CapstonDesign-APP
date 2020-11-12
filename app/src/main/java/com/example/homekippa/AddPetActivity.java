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
import com.example.homekippa.data.GroupData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPetActivity extends AppCompatActivity {

    private EditText editText_petReg_num;
    private Button button_petReg;
    private Button button_petDes;
    private ServiceApi service;
    private String petName;
    private String petGender;
    private String petSpecies;
    private String petNeutralization;
    private String petReg;
    private GroupData groupData;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_add_pet);

        editText_petReg_num = findViewById(R.id.editText_petReg_num);
        button_petReg = findViewById(R.id.button_petReg);
        button_petDes = findViewById(R.id.button_petDes);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        petName = "";
        petGender = "";
        petNeutralization = "";
        petReg = "";
        petSpecies = "";

        groupData =(GroupData) getIntent().getExtras().get("groupData");





        editText_petReg_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_petReg_num.setText("");
            }
        });

        button_petReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                petReg = editText_petReg_num.getText().toString().trim();

                if(petReg.isEmpty()){
                    editText_petReg_num.setText("숫자를 입력하세요.");

                }
                else{
                    addPet(new AddPetData(petReg));

                }
            }
        });
        button_petDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPetDesActivity.class);
                intent.putExtra("petRegNum", petReg);
                intent.putExtra("petName", petName);
                intent.putExtra("petGender", petGender);
                intent.putExtra("petSpecies", petSpecies);
                intent.putExtra("petNeutralization", petNeutralization);
                intent.putExtra("groupData",groupData);
                startActivityForResult(intent, 0 );

            }
        });

    }

    private void addPet(AddPetData data) {
        Log.i("create2","create");
        service.addPetReg(data).enqueue(new Callback<AddPetResponse>() {
            @Override
            public void onResponse(Call<AddPetResponse> call, Response<AddPetResponse> response) {
                AddPetResponse result = response.body();
                petName = result.getPetName();
                petGender = result.getPetGender();
                petSpecies = result.getPetSpecies();
                petNeutralization = result.getPetNeutralization();

                Toast.makeText(AddPetActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddPetDesActivity.class);
                intent.putExtra("petRegNum", petReg);
                intent.putExtra("petName", petName);
                intent.putExtra("petGender", petGender);
                intent.putExtra("petSpecies", petSpecies);
                intent.putExtra("petNeutralization", petNeutralization);
                intent.putExtra("groupData",groupData);
                startActivityForResult(intent,0);





                if(result.getCode() == 200){

                    finish();
                }else{
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddPetResponse> call, Throwable t) {
                editText_petReg_num.setText("");
                Toast.makeText(AddPetActivity.this, "반려회동물 등록번호 조회 에러 발생", Toast.LENGTH_SHORT).show();
                t.printStackTrace();


            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if (resultCode==RESULT_OK) {
                finish();;
                Toast.makeText(AddPetActivity.this, "result ok!", Toast.LENGTH_SHORT).show();
            }else{
                finish();
                Toast.makeText(AddPetActivity.this, "result cancle!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
