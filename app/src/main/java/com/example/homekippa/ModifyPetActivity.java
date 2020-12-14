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
import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.SingleItemPet;

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

    public static Context context_ModifyPetActivity;
    private static final String TAG = " ModifyPet";

    private Cache cache;
    private int petId;
    private String petName;
    private String petSpecies;
    private int petGender;
    private String petImage;
    private int petNeutrality;
    private String petBirth;
    private CircleImageView imageView_modifypet_Image;
    private ImageButton image_modifypet_button_camera;
    private LinearLayout LinearLayout_petName_title;
    private LinearLayout LinearLayout_petSpecies_title;
    private LinearLayout LinearLayout_petGender_title;
    private LinearLayout LinearLayout_petNeu_title;
    private LinearLayout LinearLayout_petBirth_title;
    private TextView textView_petName;
    private TextView textView_petSpecies;
    private TextView textView_petGender;
    private TextView textView_petNeu;
    private TextView textView_petBirth;
    public File tempFile;
    private Boolean isPermission = true;

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet);
        context_ModifyPetActivity = this;
        petId = getIntent().getIntExtra("petId",0);
        petName = getIntent().getStringExtra("petName");
        petSpecies = getIntent().getStringExtra("petSpecies");
        petGender = getIntent().getIntExtra("petGender",0);
        petImage = getIntent().getStringExtra("petImage");
        petNeutrality = getIntent().getIntExtra("petNeutrality",0);
        petBirth = getIntent().getStringExtra("petBirth");
        imageView_modifypet_Image = findViewById(R.id.imageView_modifypet_Image);
        image_modifypet_button_camera = findViewById(R.id.image_modifypet_button_camera);
        LinearLayout_petName_title = findViewById(R.id.LinearLayout_petName_title);
        LinearLayout_petSpecies_title = findViewById(R.id.LinearLayout_petSpecies_title);
        LinearLayout_petGender_title = findViewById(R.id.LinearLayout_petGender_title);
        LinearLayout_petNeu_title = findViewById(R.id.LinearLayout_petNeu_title);
        LinearLayout_petBirth_title = findViewById(R.id.LinearLayout_petBirth_title);
        textView_petName = findViewById(R.id.textView_petName);
        textView_petSpecies = findViewById(R.id.textView_petSpecies);
        textView_petGender = findViewById(R.id.textView_petGender);
        textView_petNeu = findViewById(R.id.textView_petNeu);
        textView_petBirth = findViewById(R.id.textView_petBirth);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        getPetProfileImage(petImage, imageView_modifypet_Image);
    }

    @Override
    public void onStart() {
        super.onStart();
        textView_petName.setText(petName);
        textView_petSpecies.setText(petSpecies);
        if(petGender == 1) {
            textView_petGender.setText("수컷");
        }
        else {
            textView_petGender.setText("암컷");
        }
        if(petNeutrality == 1) {
            textView_petNeu.setText("유");
        }
        else {
            textView_petNeu.setText("무");
        }

        textView_petBirth.setText(petBirth.substring(0, 10));

        imageView_modifypet_Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectPetImageModify.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        image_modifypet_button_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PopupSelectPetImageModify.class);
                intent.putExtra("isPermission", isPermission);
                startActivityForResult(intent, 1);

            }
        });

        LinearLayout_petName_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyPetNameActivity.class);
                intent.putExtra("id", petId);
                intent.putExtra("name", petName);
                startActivityForResult(intent, 2);
            }
        });

        LinearLayout_petSpecies_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyPetSpeciesActivity.class);
                intent.putExtra("id", petId);
                intent.putExtra("species", petSpecies);
                startActivityForResult(intent, 3);
            }
        });

        LinearLayout_petGender_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyPetGenderActivity.class);
                intent.putExtra("id", petId);
                intent.putExtra("gender", petGender);
                startActivityForResult(intent, 4);
            }
        });

        LinearLayout_petNeu_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyPetNeuteringActivity.class);
                intent.putExtra("id", petId);
                intent.putExtra("neutrality", petNeutrality);
                startActivityForResult(intent, 5);
            }
        });

        LinearLayout_petBirth_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyPetBirthActivity.class);
                intent.putExtra("id", petId);
                intent.putExtra("birth", petBirth);
                startActivityForResult(intent, 6);
            }
        });
    }

    private void getPetProfileImage(String url, CircleImageView imageView) {
        Log.d("url", url);
        service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "ModifyPetActivity";
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

            String str_petId = String.valueOf(petId);

            imageView_modifypet_Image.setImageBitmap(originalBm);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
            MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("upload", tempFile.getName(), reqFile);

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), str_petId);

            service.modifyPetImage(id, uploadFile).enqueue(new Callback<ModifyPetResponse>() {
                @Override
                public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                    ModifyPetResponse result = response.body();

                    Toast.makeText(ModifyPetActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                    Toast.makeText(ModifyPetActivity.this, "펫 프로필 이미지 수정 오류 발생", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            imageView_modifypet_Image.setImageResource(R.drawable.pet_profile_default);

            service.resetPetImage(petId).enqueue(new Callback<ModifyPetResponse>() {
                @Override
                public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                    ModifyPetResponse result = response.body();

                    Toast.makeText(ModifyPetActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.getCode() == 200) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                    Toast.makeText(ModifyPetActivity.this, "펫 프로필 이미지 수정 오류 발생", Toast.LENGTH_SHORT).show();
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
            } else if (requestCode == 2) {
                petName = intent.getStringExtra("petName");
                Log.d("2", petName);
                textView_petName.setText(petName);
            } else if (requestCode == 3) {
                petSpecies = intent.getStringExtra("petSpecies");
                Log.d("3", petSpecies);
                textView_petSpecies.setText(petSpecies);
            } else if (requestCode == 4) {
                petGender = intent.getIntExtra("petGender",0);
                Log.d("4", String.valueOf(petGender));
                if(petGender == 1) {
                    textView_petGender.setText("수컷");
                }
                else {
                    textView_petGender.setText("암컷");
                }
            } else if (requestCode == 5) {
                petNeutrality = intent.getIntExtra("petNeutrality",0);
                Log.d("5", String.valueOf(petNeutrality));
                if(petNeutrality == 1) {
                    textView_petNeu.setText("유");
                }
                else {
                    textView_petNeu.setText("무");
                }
            } else if (requestCode == 6) {
                petBirth = intent.getStringExtra("petBirth");
                Log.d("6", petBirth);
                textView_petBirth.setText(petBirth.substring(0, 10));
            }
        }
    }
}