package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox checkbox_female;
    private CheckBox checkbox_male;

    private EditText editText_petSpecies;
    private CheckBox checkbox_netralizationYes;
    private CheckBox checkbox_netralizationNo;
    private EditText editText_petRegNum;
    private Button button_petDesSave;
    private ServiceApi service;
    private GroupData groupData;
    private TextView textView_birthDay;
    private TextView textView_gender;
    private TextView textView_neutalization;

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
        checkbox_female = findViewById(R.id.checkbox_female);
        checkbox_male = findViewById(R.id.checkbox_male);
        checkbox_netralizationYes = findViewById(R.id.checkbox_netralizationYes);
        checkbox_netralizationNo = findViewById(R.id.checkbox_neutralizationNo);
        editText_petSpecies = findViewById(R.id.editText_pet_species);
        textView_gender = findViewById(R.id.textView_pet_gender);
        textView_neutalization = findViewById(R.id.textview_pet_neutralization);



        button_petDesSave = findViewById(R.id.button_petDesSave);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        groupData =(GroupData) getIntent().getExtras().get("groupData");
        textView_birthDay = findViewById(R.id.textView_birthday);


        Intent getintent = getIntent();

        regNum = getintent.getExtras().getString("petRegNum");


        if (!regNum.equals("")) {
            Log.d("nothere","not");
            name = getintent.getExtras().getString("petName");
            gender = getintent.getExtras().getString("petGender");
            species = getintent.getExtras().getString("petSpecies");
            neutralization = getintent.getExtras().getString("petNeutralization");

            editText_petRegNum.setText(regNum);
            editText_petName.setText(name);

            if(gender.equals("수컷")){
                checkbox_male.setChecked(true);
            }else{
                checkbox_female.setChecked(true);
            }

            editText_petSpecies.setText(species);

            if(neutralization.equals("중성")){
                checkbox_netralizationYes.setChecked(true);
            }else{
                checkbox_netralizationNo.setChecked(true);
            }
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

                if(regNum.equals("")){
                    name = editText_petName.getText().toString();

                    species = editText_petSpecies.getText().toString();
                    regNum = editText_petRegNum.getText().toString();

                    if(checkbox_male.isChecked()){
                        gender = "수컷";
                    }
                    else if(checkbox_female.isChecked()){
                        gender = "암컷";
                    }

                    if(checkbox_netralizationYes.isChecked()){
                        neutralization = "중성";
                    }
                    else if(checkbox_netralizationNo.isChecked()){
                        neutralization = "중성 안함";
                    }
                }

                Log.d("groupId", String.valueOf(groupId));

                if (name.isEmpty()) {
                    editText_petName.setError("반려동물 이름을 입력해주세요");
                } else if (gender.isEmpty()) {
                    textView_gender.setError("반려동물성별을 입력해주세요");
                } else if (species.isEmpty()) {
                    editText_petSpecies.setError("반려동물 종을 입력해주세요");
                } else if (regNum.isEmpty()) {
                    editText_petRegNum.setError("반려동물 등록번호를 입려해주세요");
                } else if (neutralization.isEmpty()) {
                    textView_neutalization.setError("반려동물 중성화 여부를 입력해주세요.");
                }else if (birth.isEmpty()){
                    textView_birthDay.setError("생년월일을 입력하세요");
                }else {
                    addPetDes(new AddPetDesData(regNum, name, species, gender, neutralization, groupId, birth));
                }
            }
        });
    }
    public void onCheckboxClicked_petGender(View v) {
        switch (v.getId()){
            case R.id.checkbox_male:
                checkbox_male.setChecked(true);
                checkbox_female.setChecked(false);
                break;


            case R.id.checkbox_female:
                checkbox_male.setChecked(false);
                checkbox_female.setChecked(true);
                break;

        }

    }
    public void onCheckboxClicked_petNeutralization(View v) {
        switch (v.getId()){
            case R.id.checkbox_netralizationYes:
                checkbox_netralizationYes.setChecked(true);
                checkbox_netralizationNo.setChecked(false);
                break;


            case R.id.checkbox_neutralizationNo:
                checkbox_netralizationYes.setChecked(false);
                checkbox_netralizationNo.setChecked(true);
                break;

        }

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
