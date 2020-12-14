package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homekippa.data.EditUserData;
import com.example.homekippa.data.EditUserResponse;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.PopupSelectGroupImage;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    public static Context context_EditUserActivity;
    private ServiceApi service;
    private MainActivity main;

    private EditText email_login;
    private EditText birth_login;
    private EditText phone_login;

    private EditText name_login;

    private Button button_gotoEditUser;

    private Editable userEmail;
    private Editable userName;
    private Editable userPhone;
    private UserData userData;
    private ImageButton image_button_camera_edituser;
    private ImageView imageView_image_edituser;

    public File tempFile;
    private int checkDefault = 0;

    private Boolean isPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        context_EditUserActivity = this;
        userData = ((MainActivity) MainActivity.context_main).getUserData();
        service = RetrofitClient.getClient().create(ServiceApi.class);

        email_login = (EditText) this.findViewById(R.id.editText_email);
        birth_login = (EditText) this.findViewById(R.id.editText_birth);
        phone_login = (EditText) this.findViewById(R.id.editText_Phone);

        name_login = (EditText) this.findViewById(R.id.editText_Name);
        button_gotoEditUser = (Button) this.findViewById(R.id.button_gotoEditUser);
        image_button_camera_edituser = findViewById(R.id.image_button_camera_edituser);
        imageView_image_edituser = findViewById(R.id.imageView_image_edituser);

        ImageTask imageTask = new ImageTask(userData.getUserImage(), imageView_image_edituser, this, false);
        imageTask.getImage();

        String id = userData.getUserId();
        String name = userData.getUserName();
        String phone = userData.getUserPhone();

        String email = userData.getUserEmail();
        String birth = userData.getUserBirth().substring(0, 10);

        email_login.setText(email);
        phone_login.setText(phone);
        birth_login.setText(birth);
        name_login.setText(name);

        userEmail = email_login.getText();
        userName = name_login.getText();
        userPhone = phone_login.getText();

        imageView_image_edituser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectEditUserImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_button_camera_edituser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectEditUserImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });


        button_gotoEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser(id, userName.toString(), userPhone.toString());
            }
        });


    }


    private void editUser(String id, String name, String phone) {

        if (tempFile != null) {
            HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

            RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), id);
            data.put("id", Id);
            RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), name);
            data.put("name", Name);
            RequestBody Phone = RequestBody.create(MediaType.parse("text/plain"), phone);
            data.put("phone", Phone);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            service.userEditWithPhoto(data, uploadFile).enqueue(new Callback<EditUserResponse>() {

                @Override
                public void onResponse(Call<EditUserResponse> call, Response<EditUserResponse> response) {
                    EditUserResponse result = response.body();
                    if (result.getCode() == 200) {
                        Toast.makeText(EditUserActivity.this, "사용자 정보가 성공적으로 수정되었습니다!", Toast.LENGTH_LONG).show();
                        service.getUserData(userData.getUserId(), userData.getUserToken()).enqueue(new Callback<UserData>() {
                            @Override
                            public void onResponse(Call<UserData> call, Response<UserData> response) {
                                if (response.isSuccessful()) {
                                    UserData userData = response.body();
                                    ((MainActivity) MainActivity.context_main).setUserData(userData);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserData> call, Throwable t) {

                            }
                        });
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<EditUserResponse> call, Throwable t) {
                    Toast.makeText(EditUserActivity.this, "사용자 정보 수정 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("사용자 정보 수정 에러 발생", t.getMessage());
                    t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌

                }
            });
        } else {

            EditUserData data = new EditUserData(id, name, phone, checkDefault);
            service.userEdit(data).enqueue(new Callback<EditUserResponse>() {

                @Override
                public void onResponse(Call<EditUserResponse> call, Response<EditUserResponse> response) {
                    EditUserResponse result = response.body();
                    if (result.getCode() == 200) {
                        Toast.makeText(EditUserActivity.this, "사용자 정보가 성공적으로 수정되었습니다!", Toast.LENGTH_LONG).show();
                        service.getUserData(userData.getUserId(), userData.getUserToken()).enqueue(new Callback<UserData>() {
                            @Override
                            public void onResponse(Call<UserData> call, Response<UserData> response) {
                                if (response.isSuccessful()) {
                                    UserData userData = response.body();
                                    ((MainActivity) MainActivity.context_main).setUserData(userData);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserData> call, Throwable t) {

                            }
                        });
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<EditUserResponse> call, Throwable t) {
                    Toast.makeText(EditUserActivity.this, "사용자 정보 수정 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("사용자 정보 수정 에러 발생", t.getMessage());
                    t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌

                }
            });
        }
    }
    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView_image_edituser.setImageBitmap(originalBm);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {

            return;
        } else {
            if (requestCode == 1) {

                if (tempFile != null) {
                    setImage();
                } else {
                    checkDefault = 1;
                    imageView_image_edituser.setImageResource(R.drawable.group_profile_default);
                }

            }
        }
    }
}