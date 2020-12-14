package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.data.UidData;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.ui.datepicker.DatePickerFragment;
import com.example.homekippa.ui.group.PopupSelectGroupImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.homekippa.function.Loading;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class SignUpActivity extends AppCompatActivity {
    public static Context context_SignUpActivity;
    private FirebaseAuth mAuth;
    private EditText phone_login;
    private EditText email_login;
    private EditText pw_login;
    private TextView birth_login;
    private EditText name_login;
    private ServiceApi service;
    private CheckBox checkbox_Agree;
    private CheckBox checkbox_man;
    private CheckBox checkbox_woman;
    private ImageButton image_button_camera_signup;
    private ImageView imageView_image_signup;

    private int gender;

    public File tempFile;

    private Boolean isPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context_SignUpActivity = this;
        Button button_SignUp = findViewById(R.id.button_SignUp);
        imageView_image_signup = findViewById(R.id.imageView_image_signup);
        image_button_camera_signup = findViewById(R.id.image_button_camera_signup);
        phone_login = findViewById(R.id.editText_Phone);
        email_login = findViewById(R.id.editText_email);
        pw_login = findViewById(R.id.editText_PW);
        birth_login = findViewById(R.id.text_Birth);
        name_login = findViewById(R.id.editText_Name);
        mAuth = FirebaseAuth.getInstance();
        checkbox_Agree = findViewById(R.id.checkbox_Agree);
        checkbox_man = findViewById(R.id.checkbox_userGenderMan);
        checkbox_woman = findViewById(R.id.checkbox_usergenderWoman);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        tedPermission();

        imageView_image_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectUserImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_button_camera_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectUserImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        birth_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = phone_login.getText().toString().trim();
                final String email = email_login.getText().toString().trim();
                final String pw = pw_login.getText().toString().trim();
                final String birth = birth_login.getText().toString().trim();
                final String name = name_login.getText().toString().trim();


                if(checkbox_man.isChecked()){
                    gender = 1;
                }else if(checkbox_woman.isChecked()){
                    gender =0;
                }

                if (email.isEmpty()) {
                    email_login.setError("생년월일을 입력하세요");
                } else if (phone.isEmpty()) {
                    phone_login.setError("PW를 입력하세요");
                } else if (pw.isEmpty()) {
                    pw_login.setError("PW를 입력하세요");
                } else if (birth.isEmpty()) {
                    birth_login.setError("생년월일을 입력하세요");
                } else if (name.isEmpty()) {
                    name_login.setError("닉네임을 입력하세요");
                } else if (!checkbox_Agree.isChecked()) {
                    checkbox_Agree.setError("이용약관에 동의해주세요");
                } else if(!checkbox_man.isChecked() && !checkbox_woman.isChecked()){
                    checkbox_man.setError("성별을 선택하세요");
                    checkbox_woman.setError("성별을 선택하세요");
                } else {
                    final Loading loading = new Loading();
                    loading.loading(SignUpActivity.this);
                    mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("회원 가입", "createUserWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();
                                startSignUp(user.getUid(), phone, email, name, birth);
                                loading.loadingEnd();

                                user.sendEmailVerification().addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> verify_task) {
                                        if (verify_task.isSuccessful()) {
                                            Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Exception e = task.getException();
                                Log.w("회원 가입", "createUserWithEmail:failure", e);
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        checkbox_Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_Agree.isChecked()) {
                    Toast.makeText(getApplicationContext(), "이용 약관에 동의하셨습니다", Toast.LENGTH_SHORT).show(); // 토스트 : 팝업으로 송출
                }
            }
        });

    }

    public void onCheckboxClicked_UserGender(View v) {
        switch (v.getId()){
            case R.id.checkbox_userGenderMan:
                checkbox_man.setChecked(true);
                checkbox_woman.setChecked(false);
                break;
            case R.id.checkbox_usergenderWoman:
                checkbox_man.setChecked(false);
                checkbox_woman.setChecked(true);
                break;
        }

    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String year_string = Integer.toString(year);
        String day_string = null;
        String dateMessage = null;
        if (day < 10){
            day_string = Integer.toString(day);
            dateMessage = (year_string + "-" + month_string + "-0" + day_string);
        } else {
            day_string = Integer.toString(day);
            dateMessage = (year_string + "-" + month_string + "-" + day_string);
        }

        birth_login.setText(dateMessage);
    }

    private void startSignUp(String uid, String phone, String email, String name, String birth) {
        if (tempFile != null) {
            HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

            RequestBody uId = RequestBody.create(MediaType.parse("text/plain"), uid);
            data.put("userId", uId);
            RequestBody Phone = RequestBody.create(MediaType.parse("text/plain"), phone);
            data.put("userPhone", Phone);
            RequestBody Email = RequestBody.create(MediaType.parse("text/plain"), email);
            data.put("userEmail", Email);
            RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), name);
            data.put("userName", Name);
            RequestBody Birth = RequestBody.create(MediaType.parse("text/plain"), birth);
            data.put("userBirth", Birth);
            RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gender));
            data.put("userGender", Gender);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            service.userSignUpWithPhoto(data, uploadFile).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    SignUpResponse result = response.body();
//                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
//                        showProgress(false);

                    if (result.getCode() == 200) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "회원가입 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("회원가입 에러 발생", t.getMessage());
                    t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌
//                        showProgress(false);
                }
            });
        } else {
            SignUpData data = new SignUpData(uid, phone, email, name, birth, gender);
            service.userSignUp(data).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    SignUpResponse result = response.body();
//                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
//                        showProgress(false);

                    if (result.getCode() == 200) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "회원가입 에러 발생", Toast.LENGTH_LONG).show();
                    Log.e("회원가입 에러 발생", t.getMessage());
                    t.printStackTrace(); // 에러 발생시 에러 발생 원인 단계별로 출력해줌
//                        showProgress(false);
                }
            });
        }
    }

    public void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView_image_signup.setImageBitmap(originalBm);

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
                    imageView_image_signup.setImageResource(R.drawable.group_profile_default);
                }
            }
        }
    }
}
