package com.example.homekippa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.ModifyGroupProfileImageResponse;
import com.example.homekippa.data.SetGroupProfileImageDefaultResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.ModifyGroupAddressActivity;
import com.example.homekippa.ui.group.ModifyGroupIntroActivity;
import com.example.homekippa.ui.group.ModifyGroupNameActivity;
import com.example.homekippa.ui.group.PopupSeleteGroupImageModify;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPetActivity extends AppCompatActivity {

    public ModifyPetActivity() {
    }

    public static Context context_ModifyGroupActivity;
    private static final String TAG = " ModifyGroup";

    private GroupData groupData;
    private CircleImageView imageView_modify_profile_Image;
    private ImageButton image_modify_button_camera;
    private LinearLayout LinearLayout_groupName_title;
    private LinearLayout LinearLayout_groupIntro_title;
    private LinearLayout LinearLayout_groupAddress_title;
    private TextView textView_groupName;
    private TextView textView_groupIntro;
    private TextView textView_groupAddress;
    public File tempFile;
    private Boolean isPermission = true;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group);
        context_ModifyGroupActivity = this;
        imageView_modify_profile_Image = findViewById(R.id.imageView_modify_profile_Image);
        image_modify_button_camera = findViewById(R.id.image_modify_button_camera);
        LinearLayout_groupName_title = findViewById(R.id.LinearLayout_groupName_title);
        LinearLayout_groupIntro_title = findViewById(R.id.LinearLayout_groupIntro_title);
        LinearLayout_groupAddress_title = findViewById(R.id.LinearLayout_groupAddress_title);
        textView_groupName = findViewById(R.id.textView_groupName);
        textView_groupIntro = findViewById(R.id.textView_groupIntro);
        textView_groupAddress = findViewById(R.id.textView_groupAddress);
        groupData = (GroupData)getIntent().getExtras().get("groupData");
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        getGroupProfileImage(groupData.getImage(), imageView_modify_profile_Image);
        textView_groupName.setText(groupData.getName());
        textView_groupIntro.setText(groupData.getIntroduction());
        textView_groupAddress.setText(groupData.getAddress());

        imageView_modify_profile_Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSeleteGroupImageModify.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_modify_button_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSeleteGroupImageModify.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        LinearLayout_groupName_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyGroupNameActivity.class);
                intent.putExtra("id", groupData.getId());
                intent.putExtra("name", groupData.getName());
                startActivityForResult(intent, 2);
            }
        });

        LinearLayout_groupIntro_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyGroupIntroActivity.class);
                intent.putExtra("id", groupData.getId());
                intent.putExtra("introduction", groupData.getIntroduction());
                startActivityForResult(intent, 3);
            }
        });

        LinearLayout_groupAddress_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyGroupAddressActivity.class);
                intent.putExtra("id", groupData.getId());
                startActivityForResult(intent, 4);
            }
        });
    }

    private void getGroupProfileImage(String url, CircleImageView imageView) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "ModifyGroupActivity";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(ModifyPetActivity.this).load(bitmap).circleCrop().into(imageView);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void setImage() {
        if (tempFile != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

            String str_groupId = String.valueOf(groupData.getId());

            imageView_modify_profile_Image.setImageBitmap(originalBm);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), str_groupId);

            service.modifyGroupProfileImage(id, uploadFile).enqueue(new Callback<ModifyGroupProfileImageResponse>() {
                @Override
                public void onResponse(Call<ModifyGroupProfileImageResponse> call, Response<ModifyGroupProfileImageResponse> response) {
                    ModifyGroupProfileImageResponse result = response.body();

                    Toast.makeText(ModifyPetActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyGroupProfileImageResponse> call, Throwable t) {
                    Toast.makeText(ModifyPetActivity.this, "그룹 프로필 이미지 수정 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            imageView_modify_profile_Image.setImageResource(R.drawable.group_profile_default);

            service.setGroupProfileImageDefault(groupData.getId()).enqueue(new Callback<SetGroupProfileImageDefaultResponse>() {
                @Override
                public void onResponse(Call<SetGroupProfileImageDefaultResponse> call, Response<SetGroupProfileImageDefaultResponse> response) {
                    SetGroupProfileImageDefaultResponse result = response.body();

                    Toast.makeText(ModifyPetActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<SetGroupProfileImageDefaultResponse> call, Throwable t) {
                    Toast.makeText(ModifyPetActivity.this, "그룹 프로필 이미지 수정 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {

            return;
        } else {
            if (requestCode == 1) {
                    setImage();
            } else {

            }
        }
    }
}