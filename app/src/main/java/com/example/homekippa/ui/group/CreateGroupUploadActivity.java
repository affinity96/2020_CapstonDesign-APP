package com.example.homekippa.ui.group;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.AddPetDesActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.CreateGroupUploadResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  사진만 업로드 했을 때 성공하는지 여부를 테스트하기 위한 액티비티입니다.
 */

public class CreateGroupUploadActivity extends AppCompatActivity {

    private static final String TAG = "createGroupUpload";
    private Button button_album;
    private Button button_upload;
    private ServiceApi service;
    private File tempFile;
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_upload);
        button_album = findViewById(R.id.button_album);
        button_upload = findViewById(R.id.button_upload);

        // 권한 요청
        tedPermission();

        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if (isPermission) goToAlbum();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempFile != null) {
                    upload();
                } else {
                    Toast.makeText(getApplicationContext(),"이미지를 선택하거라", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 권한 설정
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
            // 사진 업로드 버튼들 눌러서 들어갔다가 취소하는 경우 (이부분 주소랑 겹쳐서 잘 모르겠음)
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
            if (requestCode == PICK_FROM_ALBUM) {

                Uri photoUri = intent.getData();
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

                Cursor cursor = null;

                try {

                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};

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

            }
        }
    }

    private void upload() {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        service.groupCreateUpload(body, name).enqueue(new Callback<CreateGroupUploadResponse>() {

            @Override
            public void onResponse(Call<CreateGroupUploadResponse> call, Response<CreateGroupUploadResponse> response) {
                CreateGroupUploadResponse result = response.body();
//              Toast.makeText(CreateGroupUploadActivity.this, result.getMessage(),Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {

                    finish();
                }
            }

            @Override
            public void onFailure(Call<CreateGroupUploadResponse> call, Throwable t) {
                Toast.makeText(CreateGroupUploadActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
