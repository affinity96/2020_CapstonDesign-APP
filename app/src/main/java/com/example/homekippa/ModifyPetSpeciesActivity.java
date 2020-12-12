package com.example.homekippa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPetSpeciesActivity extends AppCompatActivity {

    public ModifyPetSpeciesActivity() {
    }

    private static final String TAG = " ModifyPetSpecies";

    private EditText editText_petmodify_Species;
    private Button button_petspecies_next;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet_species);
        editText_petmodify_Species = findViewById(R.id.editText_petmodify_Species);
        button_petspecies_next = findViewById(R.id.button_petspecies_next);
        String species = getIntent().getStringExtra("species");
        editText_petmodify_Species.setText(species);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        button_petspecies_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petSpecies = editText_petmodify_Species.getText().toString();
                int id = getIntent().getIntExtra("id", 0);
                Next(id, petSpecies);
            }
        });
    }

    private void Next(int id, String species) {
        service.modifyPetSpecies(id, species).enqueue(new Callback<ModifyPetResponse>() {
            @Override
            public void onResponse(Call<ModifyPetResponse> call, Response<ModifyPetResponse> response) {
                ModifyPetResponse result = response.body();

                if (result.getCode() == 200) {

                        finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyPetResponse> call, Throwable t) {
                Toast.makeText(ModifyPetSpeciesActivity.this, "종 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}