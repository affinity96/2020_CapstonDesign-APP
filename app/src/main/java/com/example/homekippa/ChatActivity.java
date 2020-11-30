package com.example.homekippa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private RecyclerView listView_chat_users;
    private UserData userData;
    private Button button;
    private ArrayList<UserData> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message");
        listView_chat_users = findViewById(R.id.listView_chat_users);
        userData = (UserData)getIntent().getExtras().get("userData");
        button = findViewById(R.id.button_newchat);

        // 임시 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChattingRoomActivity.class);
                intent.putExtra("userData", userData);
                startActivity(intent);
            }
        });

        // FirebaseDB 예시  https://mailmail.tistory.com/44
/*        ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
        databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); //databaseReference를 이용해 데이터 푸쉬
        chat_edit.setText(""); //입력창 초기화*/

    }

    private void setChatListView(RecyclerView listView){

    }
}