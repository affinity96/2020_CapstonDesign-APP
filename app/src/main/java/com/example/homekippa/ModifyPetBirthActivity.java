package com.example.homekippa;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.datepicker.DatePetModifyPickerFragment;
import com.example.homekippa.ui.datepicker.DatePetPickerFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPetBirthActivity extends AppCompatActivity {

    public ModifyPetBirthActivity() {
    }

    private static final String TAG = " ModifyPetBirth";

    private TextView textView_modify_birthday;
    private Button button_petbirth_next;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet_birth);
        textView_modify_birthday = findViewById(R.id.textView_modify_birthday);
        button_petbirth_next = findViewById(R.id.button_petbirth_next);
        String birth = getIntent().getStringExtra("birth");
        textView_modify_birthday.setText(birth.substring(0, 10));
        service = RetrofitClient.getClient().create(ServiceApi.class);

        textView_modify_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(view);
            }
        });

        button_petbirth_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petBirth = textView_modify_birthday.getText().toString();
                int id = getIntent().getIntExtra("id", 0);
                Next(id, petBirth);
            }
        });
    }

    private void datePicker(View view) {
        DialogFragment newFragment = new DatePetModifyPickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "-" + month_string + "-" + day_string);

        textView_modify_birthday.setText(dateMessage);
    }

    private void Next(int id, String birth) {
        service.modifyPetBirth(id, birth).enqueue(new Callback<ModifyPetResponse>() {
            @Override
            public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                ModifyPetResponse result = response.body();

                if (result.getCode() == 200) {

                        finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                Toast.makeText(ModifyPetBirthActivity.this, "생일 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}