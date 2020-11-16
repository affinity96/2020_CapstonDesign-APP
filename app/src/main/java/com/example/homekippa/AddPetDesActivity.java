package com.example.homekippa;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private Button button_gallery;
    private Button button_camera;
    private TextView textView_gender;
    private TextView textView_neutalization;

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
        button_gallery = findViewById(R.id.button_gallery);
        button_camera = findViewById(R.id.button_camera);

        // 권한 요청
        tedPermission();

        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission)  takePhoto();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
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
                    addPetDes(groupId, birth);
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

    /**
     *  앨범에서 이미지 가져오기
     */
    public void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     *  카메라에서 이미지 가져오기
     */
    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.homekippa.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {
                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    /**
     *  폴더 및 파일 만들기
     */
    public File createImageFile() throws IOException {

        // 이미지 파일 이름 ( Happytogedog_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "Happytogedog_" + timeStamp;

        // 이미지가 저장될 폴더 이름 ( Happytogedog )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Happytogedog/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     *  권한 설정
     */
    public void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
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
