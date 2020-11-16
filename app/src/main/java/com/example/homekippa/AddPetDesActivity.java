package com.example.homekippa;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.homekippa.ui.group.CreateGroupActivity;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.HashMap;

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
    private Button button_gallery;
    private Button button_camera;

    private String name;
    private String gender;
    private String species;
    private String neutralization;
    private String regNum;

    private static final String TAG = "addPetDes";
    private File tempFile;
    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    CreateGroupActivity groupActivity = new CreateGroupActivity();

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
        button_gallery = findViewById(R.id.button_gallery);
        button_camera = findViewById(R.id.button_camera);

        // 권한 요청
        groupActivity.tedPermission();

        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission) groupActivity.goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission)  groupActivity.takePhoto();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

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
                    addPetDes(groupId, birth);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        } else {
            if (requestCode == PICK_FROM_ALBUM) {

                Uri photoUri = intent.getData();
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

                Cursor cursor = null;

                try {

                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = { MediaStore.Images.Media.DATA };

                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index));

                    Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

//                setImage();

            } else if (requestCode == PICK_FROM_CAMERA) {

//                setImage();

            }
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

    private void addPetDes(int groupId, String birth) {
        Log.i("create2", "create");

        if (tempFile != null) {
            String str_groupId = String.valueOf(groupId);

            HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

            RequestBody reg_Num = RequestBody.create(MediaType.parse("text/plain"), regNum);
            data.put("regNum", reg_Num);
            RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), name);
            data.put("name", Name);
            RequestBody Species = RequestBody.create(MediaType.parse("text/plain"), species);
            data.put("species", Species);
            RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), gender);
            data.put("gender", Gender);
            RequestBody Neutralization = RequestBody.create(MediaType.parse("text/plain"), neutralization);
            data.put("neutralization", Neutralization);
            RequestBody group_Id = RequestBody.create(MediaType.parse("text/plain"), str_groupId);
            data.put("groupId", group_Id);
            RequestBody Birth = RequestBody.create(MediaType.parse("text/plain"), birth);
            data.put("birth", Birth);

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
