package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.example.homekippa.data.AddPetDesData;
import com.example.homekippa.data.AddpetDesResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.datepicker.DatePetPickerFragment;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPetDesActivity extends AppCompatActivity {

    private EditText editText_petName;
    private EditText editText_petGender;
    private EditText editText_petSpecies;
    private EditText editText_petNeutralization;
    private EditText editText_petRegNum;
    private Button button_petDesSave;
    private ServiceApi service;
    private GroupData groupData;
    private TextView textView_birthDay;

    private String name;
    private String gender;
    private String species;
    private String neutralization;
    private String regNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_pet_des);

        editText_petRegNum = findViewById(R.id.editText_pet_regNum);
        editText_petName = findViewById(R.id.editText_pet_name);
        editText_petGender = findViewById(R.id.editText_pet_gender);
        editText_petSpecies = findViewById(R.id.editText_pet_species);
        editText_petNeutralization = findViewById(R.id.editText_pet_neutralization);
        button_petDesSave = findViewById(R.id.button_petDesSave);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        groupData =(GroupData) getIntent().getExtras().get("groupData");
        textView_birthDay = findViewById(R.id.textView_birthday);


        Intent getintent = getIntent();

        regNum = getintent.getExtras().getString("petRegNum");
        if (regNum != "") {
            name = getintent.getExtras().getString("petName");
            gender = getintent.getExtras().getString("petGender");
            species = getintent.getExtras().getString("petSpecies");
            neutralization = getintent.getExtras().getString("petNeutralization");

            editText_petRegNum.setText(regNum);
            editText_petName.setText(name);
            editText_petGender.setText(gender);
            editText_petSpecies.setText(species);
            editText_petNeutralization.setText(neutralization);
        } else {
            regNum = editText_petRegNum.getText().toString();
            name = editText_petName.getText().toString();
            gender = editText_petGender.getText().toString();
            species = editText_petSpecies.getText().toString();
            neutralization = editText_petNeutralization.getText().toString();
        }

        textView_birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(view);
            }
        });

        button_petDesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int groupId = groupData.getId();
                final String birth = textView_birthDay.getText().toString().trim();

                Log.d("groupId", String.valueOf(groupId));

                if (name.isEmpty()) {
                    editText_petName.setError("반려동물 이름을 입력해주세요");
                } else if (gender.isEmpty()) {
                    editText_petGender.setError("반려동물성별을 입력해주세요");
                } else if (species.isEmpty()) {
                    editText_petSpecies.setError("반려동물 종을 입력해주세요");
                } else if (regNum.isEmpty()) {
                    editText_petRegNum.setError("반려동물 등록번호를 입려해주세요");
                } else if (neutralization.isEmpty()) {
                    editText_petNeutralization.setError("반려동물 중성화 여부를 입력해주세요.");
                }else if (birth.isEmpty()){
                    textView_birthDay.setError("생년월일을 입력하세요");
                }else {
                    addPetDes(new AddPetDesData(regNum, name, species, gender, neutralization, groupId, birth));
                }
            }
        });
    }

    private void datePicker(View view) {
        DialogFragment newFragment = new DatePetPickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "-" + month_string + "-" + day_string);

        textView_birthDay.setText(dateMessage);
    }

    private void addPetDes(AddPetDesData data) {
        Log.i("create2","create");
        service.addPetDes(data).enqueue(new Callback<AddpetDesResponse>() {
            @Override
            public void onResponse(Call<AddpetDesResponse> call, Response<AddpetDesResponse> response) {
                AddpetDesResponse result = response.body();


                Toast.makeText(AddPetDesActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();
                if(result.getCode() == 200){
                    setResult(RESULT_OK);
                    finish();

                }else{
                    setResult(RESULT_OK);
                    finish();

                }
            }
            @Override
            public void onFailure(Call<AddpetDesResponse> call, Throwable t) {

                Toast.makeText(AddPetDesActivity.this, "반려회동물 등록번호회 등록 에러 발생", Toast.LENGTH_SHORT).show();
                t.printStackTrace();


            }
        });

    }
}
