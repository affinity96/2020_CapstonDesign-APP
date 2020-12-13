package com.example.homekippa.ui.group;

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
import com.example.homekippa.Cache;
import com.example.homekippa.LoginActivity;
import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.GetFollowData;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.ModifyGroupResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

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

public class ModifyGroupActivity extends AppCompatActivity {

    public ModifyGroupActivity() {
    }

    public static Context context_ModifyGroupActivity;
    private static final String TAG = " ModifyGroup";

    private Cache cache;
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
    private int groupId;
    private Boolean isPermission = true;
    private ServiceApi service;

    private MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group);

        main = new MainActivity();
        service = RetrofitClient.getClient().create(ServiceApi.class);

        cache = new Cache(getApplicationContext());
        context_ModifyGroupActivity = this;
        imageView_modify_profile_Image = findViewById(R.id.imageView_modify_profile_Image);
        image_modify_button_camera = findViewById(R.id.image_modify_button_camera);
        LinearLayout_groupName_title = findViewById(R.id.LinearLayout_groupName_title);
        LinearLayout_groupIntro_title = findViewById(R.id.LinearLayout_groupIntro_title);
        LinearLayout_groupAddress_title = findViewById(R.id.LinearLayout_groupAddress_title);
        textView_groupName = findViewById(R.id.textView_groupName);
        textView_groupIntro = findViewById(R.id.textView_groupIntro);
        textView_groupAddress = findViewById(R.id.textView_groupAddress);
        groupData = MainActivity.getGroupData();
        Log.d("yes modify onstart", groupData.getImage());
        getGroupProfileImage(groupData.getImage(), imageView_modify_profile_Image);

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("BBACK", "들어오는건가?");
        getGroupData(groupData.getId());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SSTART", "시작한건가?");
        textView_groupName.setText(groupData.getName());
        textView_groupIntro.setText(groupData.getIntroduction());
        textView_groupAddress.setText(groupData.getAddress());

        imageView_modify_profile_Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectGroupImageModify.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_modify_button_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectGroupImageModify.class);
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
                startActivityForResult(intent, 2);
            }
        });

        LinearLayout_groupAddress_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyGroupAddressActivity.class);
                intent.putExtra("id", groupData.getId());
                startActivityForResult(intent, 2);
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
                    if (bitmap != null) {
                        Glide.with(ModifyGroupActivity.this).load(bitmap).circleCrop().into(imageView);
                    }
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

            service.modifyGroupProfileImage(id, uploadFile).enqueue(new Callback<ModifyGroupResponse>() {
                @Override
                public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                    ModifyGroupResponse result = response.body();

                    Toast.makeText(ModifyGroupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {
                        Log.d("yes modify", "?");
                        service.getGroupData(MainActivity.getGroupData().getId()).enqueue(new Callback<GroupData>() {
                            @Override
                            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                                GroupData group = response.body();
                                main.setGroupData(group);
                                Log.d("yes modify", group.getImage());
                                cache.saveBitmapToJpeg(originalBm, group.getImage());
                                imageView_modify_profile_Image.setImageBitmap(originalBm);
                            }

                            @Override
                            public void onFailure(Call<GroupData> call, Throwable t) {

                            }
                        });


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                    Toast.makeText(ModifyGroupActivity.this, "그룹 프로필 이미지 수정 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {

            service.setGroupProfileImageDefault(groupData.getId()).enqueue(new Callback<ModifyGroupResponse>() {
                @Override
                public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                    ModifyGroupResponse result = response.body();

                    Toast.makeText(ModifyGroupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {
                        service.getGroupData(MainActivity.getGroupData().getId()).enqueue(new Callback<GroupData>() {
                            @Override
                            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                                GroupData group = response.body();
                                main.setGroupData(group);
                                Log.d("yes modify", group.getImage());

                                imageView_modify_profile_Image.setImageResource(R.drawable.group_profile_default);

                            }

                            @Override
                            public void onFailure(Call<GroupData> call, Throwable t) {

                            }
                        });
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                    Toast.makeText(ModifyGroupActivity.this, "그룹 프로필 이미지 수정 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        }

    }

    public void getGroupData(int ID) {

        service.getGroupData(ID).enqueue(new Callback<GroupData>() {
            @Override
            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                if (response.isSuccessful()) {
                    groupData = response.body();
                    Log.d("GGROUP", "받아온건가?");
                    textView_groupName.setText(groupData.getName());
                    textView_groupIntro.setText(groupData.getIntroduction());
                    textView_groupAddress.setText(groupData.getAddress());
                }
            }

            @Override
            public void onFailure(Call<GroupData> call, Throwable t) {
                Log.e("그룹 확인", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {

                setImage();
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("groupData", groupData);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}