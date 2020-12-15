package com.example.homekippa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.homekippa.chat.ChatData;
import com.example.homekippa.chat.ChatProfile;
import com.example.homekippa.chat.ListChattingRoomAdapter;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingRoomActivity extends AppCompatActivity {
    private RecyclerView listview;
    private EditText editText;
    private Button button;
    private UserData userData;
    private String chatUserId;
    private String chatUserName;
    private String chatUserImage;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ListChattingRoomAdapter mAdapter;
    private ArrayList<ChatData> chatList;
    private LinearLayoutManager pLayoutManager;
    private Bitmap bitmap;
    private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);

        Intent intent = getIntent();
        userData = (UserData) intent.getExtras().get("userData");
        chatUserId = (String)intent.getExtras().get("chatUserId");
        chatUserName = (String)intent.getExtras().get("chatUserName");
        chatUserImage = (String)intent.getExtras().get("chatUserImage");

        listview = findViewById(R.id.listView_chats);
        editText = findViewById(R.id.editText_chat_input);
        button = findViewById(R.id.button_Send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTime = simpleDate.format(new Date(System.currentTimeMillis()));

                ChatData chatfrom = new ChatData(editText.getText().toString(), "false");
                Map<String, Object> fromUpdates = chatfrom.toMap();
                Map<String, Object> chatFromUpdates = new HashMap<>();
                Map<String, Object> chatFromExtraUpdates = new HashMap<>();

                chatFromUpdates.put(getTime, fromUpdates);
                chatFromExtraUpdates.put("message", editText.getText().toString());
                chatFromExtraUpdates.put("image", chatUserImage);
                chatFromExtraUpdates.put("userName", chatUserName);


                ChatData chatto = new ChatData(editText.getText().toString(), "true");
                Map<String, Object> toUpdates = chatto.toMap();
                Map<String, Object> chatToUpdates = new HashMap<>();
                Map<String, Object> chatToExtraUpdates = new HashMap<>();

                chatToUpdates.put(getTime, toUpdates);
                chatToExtraUpdates.put("image", userData.getUserImage());
                chatToExtraUpdates.put("message", editText.getText().toString());
                chatToExtraUpdates.put("userName", userData.getUserName());

                database.getReference("message").child(userData.getUserId()).child(chatUserId).child("messages").updateChildren(chatFromUpdates);
                database.getReference("message").child(userData.getUserId()).child(chatUserId).updateChildren(chatFromExtraUpdates);
                database.getReference("message").child(chatUserId).child(userData.getUserId()).child("messages").updateChildren(chatToUpdates);
                database.getReference("message").child(chatUserId).child(userData.getUserId()).updateChildren(chatToExtraUpdates);
                editText.setText("");
            }
        });

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        service.getProfileImage(chatUserImage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    InputStream is = response.body().byteStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        chatList = new ArrayList<>();
        mAdapter = new ListChattingRoomAdapter(chatList, ChattingRoomActivity.this, bitmap);
        pLayoutManager = new LinearLayoutManager(getApplicationContext());
        listview.setLayoutManager(pLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message").child(userData.getUserId()).child(chatUserId).child("messages");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("chatroom", "here");
                Log.d("chatroom", String.valueOf(snapshot.getChildrenCount()));
                Log.d("chatroom", snapshot.getKey());
                Log.d("chatroom message", String.valueOf(snapshot.child(snapshot.getKey()).getChildrenCount()));
                Date time = null;
                try {
                    time = simpleDate.parse(snapshot.getKey());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ChatData chat = new ChatData(snapshot.child("message").getValue(String.class), snapshot.child("from").getValue(String.class), time);

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