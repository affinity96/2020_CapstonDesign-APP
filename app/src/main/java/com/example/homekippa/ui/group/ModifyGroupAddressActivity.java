package com.example.homekippa.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homekippa.R;
import com.example.homekippa.data.ModifyGroupResponse;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.searchAddress.searchAddress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyGroupAddressActivity extends AppCompatActivity {

    public ModifyGroupAddressActivity() {
    }

    private static final String TAG = " ModifyGroupName";

    private EditText editText_groupmodify_address;
    private EditText editText_groupmodify_detail;
    private Button button_address_next;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group_address);
        editText_groupmodify_address = findViewById(R.id.editText_groupmodify_address);
        editText_groupmodify_detail = findViewById(R.id.editText_groupmodify_detail);
        button_address_next = findViewById(R.id.button_address_next);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        editText_groupmodify_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), searchAddress.class);
                startActivityForResult(intent, 0);
            }
        });

        button_address_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getIntent().getIntExtra("id", 0);
                final String tempAddress = editText_groupmodify_address.getText().toString();
                final String groupArea = tempAddress.substring(tempAddress.lastIndexOf("/")+1);
                final String groupAddress = tempAddress.substring(0, tempAddress.lastIndexOf("/")) + editText_groupmodify_detail.getText().toString();

                Next(id, groupAddress, groupArea);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {

            return;
        } else {
            if (requestCode == 0) {
                //수신성공 출력
                String result = intent.getStringExtra("address");
                editText_groupmodify_address.setText(result);
            }
        }
    }

    private void Next(int id, String address, String area) {
        service.modifyGroupAddress(id, address, area).enqueue(new Callback<ModifyGroupResponse>() {
            @Override
            public void onResponse(Call<ModifyGroupResponse> call, Response<ModifyGroupResponse> response) {
                ModifyGroupResponse result = response.body();

                if (result.getCode() == 200) {

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyGroupResponse> call, Throwable t) {
                Toast.makeText(ModifyGroupAddressActivity.this, "그룹주소 수정 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}