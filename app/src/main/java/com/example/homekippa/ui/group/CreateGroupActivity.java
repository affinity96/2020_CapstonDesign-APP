package com.example.homekippa.ui.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.AddPetDesActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.CreateGroupData;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.searchAddress.searchAddress;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = "createGroup";

    private EditText editText_groupName;
    private EditText editText_introduce;
    private Button button_createGroup;
    private Button button_gallery;
    private Button button_camera;
    private TextView moveToSearchAddress;
    private EditText editText_detailAddress;
    private FirebaseAuth mAuth;
    private ServiceApi service;

    private File tempFile;

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        editText_groupName = findViewById(R.id.editText_groupName);
        editText_introduce = findViewById(R.id.editText_introduce);
        button_createGroup = findViewById(R.id.button_createGroup);
        moveToSearchAddress = findViewById(R.id.moveToSearchAddress);
        mAuth = FirebaseAuth.getInstance();
        service = RetrofitClient.getClient().create(ServiceApi.class);
        button_gallery = findViewById(R.id.button_gallery);
        button_camera = findViewById(R.id.button_camera);
        editText_detailAddress = findViewById(R.id.editText_detailAddress);

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

        service = RetrofitClient.getClient().create(ServiceApi.class);

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
                Map<String, String> data = new HashMap<>();
                String groupName = editText_groupName.getText().toString();
//                Log.d("creategroup", "here");
                String userId = mAuth.getCurrentUser().getUid();
                String groupIntroduction = editText_introduce.getText().toString();

                final String groupAddress = moveToSearchAddress.getText().toString() + editText_detailAddress.getText().toString();

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

                    createGroup(userId, groupName, groupAddress, groupIntroduction);
                }
            }
        });
    }

    /**
     *  앨범에서 이미지 가져오기
     */
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     *  카메라에서 이미지 가져오기
     */
    private void takePhoto() {

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
    private File createImageFile() throws IOException {

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
     *  tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
//    private void setImage() {
//
////        ImageView imageView = findViewById(R.id.imageView);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
//
////        imageView.setImageBitmap(originalBm);
//
//        /**
//         *  tempFile 사용 후 null 처리를 해줘야 합니다.
//         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
//         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
//         */
//        tempFile = null;
//
//    }

    /**
     *  권한 설정
     */
    private void tedPermission() {
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

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
//            Log.d("주소", "error");
            // 사진 업로드 버튼들 눌러서 들어갔다가 취소하는 경우 (이부분 주소랑 겹쳐서 잘 모르겠음)
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
            if (requestCode == 0) {
                //수신성공 출력
                String result = intent.getStringExtra("address");
                moveToSearchAddress.setText(result);
            } else if (requestCode == PICK_FROM_ALBUM) {

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

    private void createGroup(String userId, String groupName, String groupAddress, String groupIntroduction) {
        Log.i("create", "create");

        HashMap<String, RequestBody> data = new HashMap<String, RequestBody>();

        RequestBody user_Id = RequestBody.create(MediaType.parse("text/plain"), userId);
        data.put("userId", user_Id);
        RequestBody group_Name = RequestBody.create(MediaType.parse("text/plain"), groupName);
        data.put("groupName", group_Name);
        RequestBody group_Address = RequestBody.create(MediaType.parse("text/plain"), groupAddress);
        data.put("groupAddress", group_Address);
        RequestBody group_Introduction = RequestBody.create(MediaType.parse("text/plain"), groupIntroduction);
        data.put("groupIntroduction", group_Introduction);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

//        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        service.groupCreate(data, uploadFile).enqueue(new Callback<CreateGroupResponse>() {

            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                CreateGroupResponse result = response.body();
//              Toast.makeText(CreateGroupActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), CreateGroupUploadActivity.class);
//                startActivity(intent);
                if (result.getCode() == 200) {

                    finish();
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