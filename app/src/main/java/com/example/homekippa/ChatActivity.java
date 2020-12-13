package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.homekippa.chat.ChatProfile;
import com.example.homekippa.chat.ListChatAdapter;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private RecyclerView listView_chat_users;
    private UserData userData;
    private Button button;
    private ArrayList<ChatProfile> chatList;
    private ListChatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userData = (UserData)getIntent().getExtras().get("userData");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message").child(userData.getUserId());
        listView_chat_users = findViewById(R.id.listView_chat_users);

        listView_chat_users.setLayoutManager(new LinearLayoutManager(this));
        chatList = new ArrayList<>();

        mAdapter = new ListChatAdapter(chatList, ChatActivity.this, userData.getUserId());



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
        Log.d("chat","here");

        setChatListView(listView_chat_users);
        // FirebaseDB 예시  https://mailmail.tistory.com/44
/*        ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
        databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); //databaseReference를 이용해 데이터 푸쉬
        chat_edit.setText(""); //입력창 초기화*/

    }

    private void setChatListView(RecyclerView listView){
        databaseReference.child("with").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d("chat", snapshot.get);
                Log.d("chat", String.valueOf(snapshot.getChildrenCount()));
                Log.d("chat", snapshot.child("userName").getValue(String.class));
                ChatProfile chat = new ChatProfile(snapshot.child("userName").getValue(String.class), snapshot.child("message").getValue(String.class));
                mAdapter.addChat(chat);
                Log.d("chat", "들어옴");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}