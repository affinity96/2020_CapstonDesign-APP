package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.homekippa.chat.ChatProfile;
import com.example.homekippa.chat.ListChatAdapter;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.GroupInviteActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class ChatActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private RecyclerView listView_chat_users;
    private UserData userData;
    private GroupData groupData;
    private Button button;
    private ArrayList<ChatProfile> chatList;
    private ListChatAdapter mAdapter;
    private LinearLayoutManager pLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userData = (UserData)getIntent().getExtras().get("userData");
        groupData = (GroupData)getIntent().getExtras().get("groupData");
        listView_chat_users = findViewById(R.id.listView_chat_users);

        listView_chat_users.setLayoutManager(new LinearLayoutManager(this));
        chatList = new ArrayList<>();

        mAdapter = new ListChatAdapter(chatList, ChatActivity.this, userData.getUserId(), userData);
        pLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView_chat_users.setLayoutManager(pLayoutManager);
        listView_chat_users.setItemAnimator(new DefaultItemAnimator());
        listView_chat_users.setAdapter(mAdapter);


        button = findViewById(R.id.button_newchat);

        // 임시 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupInviteActivity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("groupData", groupData);
                intent.putExtra("fromChat", TRUE);
                startActivity(intent);
            }
        });
        Log.d("chat","here");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message").child(userData.getUserId());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("chat", snapshot.getKey());
                Log.d("chat name", snapshot.child("userName").getValue(String.class));
                Log.d("chat message", snapshot.child("message").getValue(String.class));
                Log.d("chat image", snapshot.child("image").getValue(String.class));
                ChatProfile chat = new ChatProfile(snapshot.getKey(), snapshot.child("userName").getValue(String.class), snapshot.child("message").getValue(String.class), snapshot.child("image").getValue(String.class));
                mAdapter.addChat(chat);
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