package com.example.homekippa.ui.group;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.homekippa.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopupSeleteGroupImageModify extends Activity {
    private static final String TAG = "PopupSeleteGroupImage";
    private Button button_image_default;
    private Button button_image_album;
    private Button button_image_camera;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_selete_image);
        Intent intent = getIntent();
        Boolean isPermission = intent.getBooleanExtra("isPermission", true);

        button_image_default = (Button) findViewById(R.id.button_image_default);
        button_image_album = (Button) findViewById(R.id.button_image_album);
        button_image_camera = (Button) findViewById(R.id.button_image_camera);

        button_image_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile = null;
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_image_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        button_image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermission) takePhoto();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            ((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.homekippa.provider", ((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {
                Uri photoUri = Uri.fromFile(((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile != null) {
                if (((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile.exists()) {
                    if (((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile.delete()) {
                        Log.e(TAG, ((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile.getAbsolutePath() + " 삭제 성공");
                        ((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile = null;
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

                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    ((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile = new File(cursor.getString(column_index));

                    Log.d(TAG, "tempFile Uri : " + Uri.fromFile(((ModifyGroupActivity)ModifyGroupActivity.context_ModifyGroupActivity).tempFile));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                setResult(RESULT_OK, intent);

                finish();

            } else if (requestCode == PICK_FROM_CAMERA) {

                setResult(RESULT_OK, intent);

                finish();
            }
        }
    }
}
