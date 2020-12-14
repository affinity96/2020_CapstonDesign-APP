package com.example.homekippa.ui.group;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.CreateGroupData;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.searchAddress.searchAddress;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class CreateGroupActivity extends AppCompatActivity {

    public CreateGroupActivity() {
    }

    public static Context context_CreateGroupActivity;
    private static final String TAG = "createGroup";

    private EditText editText_groupName;
    private EditText editText_introduce;
    private Button button_createGroup;
    private ImageButton image_button_camera;
    private TextView moveToSearchAddress;
    private ImageView imageView_profileImage;
    private EditText editText_detailAddress;
    private FirebaseAuth mAuth;
    private ServiceApi service;
    private GroupData groupData;
    private UserData userData;

    public File tempFile;

    private Boolean isPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        context_CreateGroupActivity = this;
        editText_groupName = findViewById(R.id.editText_groupName);
        editText_introduce = findViewById(R.id.editText_introduce);
        button_createGroup = findViewById(R.id.button_createGroup);
        image_button_camera = findViewById(R.id.image_button_camera);
        moveToSearchAddress = findViewById(R.id.moveToSearchAddress);
        mAuth = FirebaseAuth.getInstance();
        service = RetrofitClient.getClient().create(ServiceApi.class);
        editText_detailAddress = findViewById(R.id.editText_detailAddress);
        imageView_profileImage = findViewById(R.id.imageView_profileImage);
        userData = ((MainActivity) MainActivity.context_main).getUserData();

        service = RetrofitClient.getClient().create(ServiceApi.class);

        tedPermission();
        imageView_profileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectGroupImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_button_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectGroupImage.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        editText_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_groupName.setText("");
            }
        });

        editText_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_introduce.setText("");
            }
        });

        moveToSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), searchAddress.class);
                startActivityForResult(intent, 0);
            }
        });

        button_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = editText_groupName.getText().toString();
                String userId = mAuth.getCurrentUser().getUid();
                String groupIntroduction = editText_introduce.getText().toString();

                final String tempAddress = moveToSearchAddress.getText().toString();
                final String groupArea = tempAddress.substring(tempAddress.lastIndexOf("/")+1);
                final String groupAddress = tempAddress.substring(0, tempAddress.lastIndexOf("/")) + editText_detailAddress.getText().toString();

                if (groupName.isEmpty()) {
                    editText_groupName.setHint("GroupName을 입력하세요!");
                } else if (groupAddress.isEmpty()) {
                    moveToSearchAddress.setHint("주소를 입력하세요!");
                } else if (groupIntroduction.isEmpty()) {
                    editText_introduce.setHint("그룹 소개글을 써주세요!");
                    editText_introduce.setText("소개글");

                } else if (editText_detailAddress.getText().toString().isEmpty()){
                    editText_detailAddress.setHint("상세주소 입력해주세요!");
                } else {
                    createGroup(userId, groupName, groupAddress, groupIntroduction, groupArea);
                }
            }
        });
    }

    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView_profileImage.setImageBitmap(originalBm);

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

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode != RESULT_OK) {

                return;
            } else {
                if (requestCode == 0) {
                    String result = intent.getStringExtra("address");
                    moveToSearchAddress.setText(result);
                } else if (requestCode == 1) {

                    if (tempFile != null) {
                        setImage();
                    } else {
                        imageView_profileImage.setImageResource(R.drawable.group_profile_default);
                    }

                }
            }
        }

    private void createGroup(String userId, String groupName, String groupAddress, String groupIntroduction, String area) {
        Log.i("create", "create");

        if (tempFile != null) {
            HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

            RequestBody user_Id = RequestBody.create(MediaType.parse("text/plain"), userId);
            data.put("userId", user_Id);
            RequestBody group_Name = RequestBody.create(MediaType.parse("text/plain"), groupName);
            data.put("groupName", group_Name);
            RequestBody group_Address = RequestBody.create(MediaType.parse("text/plain"), groupAddress);
            data.put("groupAddress", group_Address);
            RequestBody group_Introduction = RequestBody.create(MediaType.parse("text/plain"), groupIntroduction);
            data.put("groupIntroduction", group_Introduction);
            RequestBody group_Area = RequestBody.create(MediaType.parse("text/plain"), area);
            data.put("area", group_Area);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);
            service.groupCreateWithPhoto(data, uploadFile).enqueue(new Callback<CreateGroupResponse>() {

                @Override
                public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                    CreateGroupResponse result = response.body();
                    if (result.getCode() == 200) {
                        requestUserData(userData.getUserId(), userData.getUserToken());
                    }
                }

                @Override
                public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                    Toast.makeText(CreateGroupActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            CreateGroupData data = new CreateGroupData(userId, groupName, groupAddress, groupIntroduction, area);

            service.groupCreate(data).enqueue(new Callback<CreateGroupResponse>() {

                @Override
                public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                    CreateGroupResponse result = response.body();

                    if (result.getCode() == 200) {
                        requestUserData(userData.getUserId(), userData.getUserToken());
                    }
                }

                @Override
                public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                    Toast.makeText(CreateGroupActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void requestUserData(String ID, String token) {
        service.getUserData(ID, token).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    userData = response.body();
                    ((MainActivity) MainActivity.context_main).setUserData(userData);
                    getGroupData(userData.getGroupId());
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("로그인", t.getMessage());
                Toast.makeText(getApplicationContext(), "유저 정보 로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getGroupData(int ID) {

        service.getGroupData(ID).enqueue(new Callback<GroupData>() {
            @Override
            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                if (response.isSuccessful()) {
                    groupData = response.body();
                    ((MainActivity) MainActivity.context_main).setGroupData(groupData);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("user", userData);
                    intent.putExtra("group", groupData);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<GroupData> call, Throwable t) {
                Log.e("그룹 확인", t.getMessage());
            }
        });
    }




}