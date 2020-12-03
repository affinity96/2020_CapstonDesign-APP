package com.example.homekippa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.homekippa.ui.group.PopupSeleteGroupImage;

import java.io.File;
import java.util.HashMap;

public class AddPetDesActivity extends AppCompatActivity {

    public static Context context_AddPetDesActivity;

    private EditText editText_petName;
    private CheckBox checkbox_female;
    private CheckBox checkbox_male;

    private EditText editText_petSpecies;
    private CheckBox checkbox_netralizationYes;
    private CheckBox checkbox_netralizationNo;

    private Button button_petDesSave;
    private ServiceApi service;
    private GroupData groupData;
    private TextView textView_birthDay;
    private ImageView imageView_profileImage;
    private ImageButton image_button_camera;
    private TextView textView_gender;
    private TextView textView_neutalization;

    private String name;
    private String gender;
    private String species;
    private String neutralization;
    private String regNum;

    private static final String TAG = "addPetDes";
    public File tempFile;
    private Boolean isPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_pet_des);
        context_AddPetDesActivity = this;

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
        image_button_camera = findViewById(R.id.image_button_camera);
        imageView_profileImage = findViewById(R.id.imageView_profileImage);

        imageView_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSeletePetImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSeletePetImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        Intent getintent = getIntent();

        regNum = getintent.getExtras().getString("petRegNum");

        if (!regNum.equals("")) {
            Log.d("nothere","not");
            name = getintent.getExtras().getString("petName");
            gender = getintent.getExtras().getString("petGender");
            species = getintent.getExtras().getString("petSpecies");
            neutralization = getintent.getExtras().getString("petNeutralization");
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
                name = editText_petName.getText().toString();

                species = editText_petSpecies.getText().toString();


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
                Log.d("groupId", String.valueOf(groupId));
                if (name.isEmpty()) {
                    editText_petName.setError("반려동물 이름을 입력해주세요");
                } else if (gender.isEmpty()) {
                    textView_gender.setError("반려동물성별을 입력해주세요");
                } else if (species.isEmpty()) {
                    editText_petSpecies.setError("반려동물 종을 입력해주세요");
                } else if (neutralization.isEmpty()) {
                    textView_neutalization.setError("반려동물 중성화 여부를 입력해주세요.");
                }else if (birth.isEmpty()){
                    textView_birthDay.setError("생년월일을 입력하세요");
                }else {
                    addPetDes(regNum, name, species, gender, neutralization, groupId, birth);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        } else {
            if (requestCode == 1) {

                if (tempFile != null) {
                    setImage();
                } else {
                    imageView_profileImage.setImageResource(R.drawable.pet_profile_default);
                }

            }
        }
    }

    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView_profileImage.setImageBitmap(originalBm);

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

    private void addPetDes(String regNum, String name, String species, String gender, String neutralization, int groupId, String birth) {
        Log.i("create2", "create");
        if (tempFile != null) {
            String str_groupId = String.valueOf(groupId);

            HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

            RequestBody reg_Num = RequestBody.create(MediaType.parse("text/plain"), regNum);
            data.put("petRegNum", reg_Num);
            RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), name);
            data.put("petName", Name);
            RequestBody Species = RequestBody.create(MediaType.parse("text/plain"), species);
            data.put("petSpecies", Species);
            RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), gender);
            data.put("petGender", Gender);
            RequestBody Neutralization = RequestBody.create(MediaType.parse("text/plain"), neutralization);
            data.put("petNeutralization", Neutralization);
            RequestBody group_Id = RequestBody.create(MediaType.parse("text/plain"), str_groupId);
            data.put("GroupId", group_Id);
            RequestBody Birth = RequestBody.create(MediaType.parse("text/plain"), birth);
            data.put("petBirth", Birth);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            service.addPetDesWithPhoto(data, uploadFile).enqueue(new Callback<AddpetDesResponse>() {
                @Override
                public void onResponse(Call<AddpetDesResponse> call, Response<AddpetDesResponse> response) {
                    AddpetDesResponse result = response.body();


                    Toast.makeText(AddPetDesActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        setResult(RESULT_OK);
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<AddpetDesResponse> call, Throwable t) {

                    Toast.makeText(AddPetDesActivity.this, "반려동물 등록번호 등록 에러 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();


                }
            });

        } else {
            AddPetDesData data = new AddPetDesData(regNum, name, species, gender, neutralization, groupId, birth);

            service.addPetDes(data).enqueue(new Callback<AddpetDesResponse>() {
                @Override
                public void onResponse(Call<AddpetDesResponse> call, Response<AddpetDesResponse> response) {
                    AddpetDesResponse result = response.body();


                    Toast.makeText(AddPetDesActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<AddpetDesResponse> call, Throwable t) {

                    Toast.makeText(AddPetDesActivity.this, "반려동물 등록번호 등록 에러 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        }
    }
}