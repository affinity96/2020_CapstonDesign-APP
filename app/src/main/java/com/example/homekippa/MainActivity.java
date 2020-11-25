package com.example.homekippa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.GroupFragment;
import com.example.homekippa.ui.group.NoGroup;
import com.example.homekippa.ui.group.SingleItemPet;
import com.example.homekippa.ui.home.HomeFragment;
import com.example.homekippa.ui.notifications.NotificationsFragment;
import com.example.homekippa.ui.search.SearchFragment;
import com.example.homekippa.ui.walk.WalkFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser curUser;
    private FirebaseAuth mAuth;
    private Toolbar tb;
    private DrawerLayout leftDrawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView navView;
    private ImageButton menuButton, chatButton;

    private ArrayList<SingleItemPet> array_pets;
    private ListView listView_pets;
    private UserData userData;
    private GroupData groupData;
    private ServiceApi service;
    private ConstraintLayout main_naviheader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        service = RetrofitClient.getClient().create(ServiceApi.class);
        curUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        userData = (UserData) intent.getExtras().get("user");
        groupData = (GroupData) intent.getExtras().get("group");

        Toast.makeText(getApplicationContext(), userData.getUserName() + "님 로그인", Toast.LENGTH_LONG).show();

        //tob navigation
        tb = findViewById(R.id.top_bar);
        setSupportActionBar(tb);
        //좌측메뉴 버튼
        menuButton = findViewById(R.id.top_btn_menu);
        leftDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.top_nav_view);
        navView = findViewById(R.id.nav_view);


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //좌측메뉴 열기
                setNavGroupData();
                leftDrawerLayout.openDrawer(navigationView);
            }
        });
        //채팅 버튼 - To Do
        chatButton = findViewById(R.id.top_btn_chat);

        //좌측 메뉴
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                leftDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                //좌측 메뉴에서 item 선택시 - To Do
                if (id == R.id.menu_item1) {
                    Toast.makeText(getApplicationContext(), title + " 선택됨", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu_item2) {
                    Toast.makeText(getApplicationContext(), title + " 선택됨", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();
                }
                return true;
            }
        });

        //하단 메뉴
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commitAllowingStateLoss();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SearchFragment()).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_walk:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new WalkFragment()).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationsFragment()).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_group:
                        if (userData.getGroupId() != 0) {
                            GroupFragment groupFragment = new GroupFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("groupData", groupData);
                            groupFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, groupFragment).commitAllowingStateLoss();
                        } else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NoGroup()).commitAllowingStateLoss();
                        }


                        return true;
                }
                return false;
            }
        });

    }

    public void setNavGroupData() {
        TextView username = (TextView) findViewById(R.id.nav_user_name);
        ImageView userProfile = (ImageView) findViewById(R.id.nav_user_image);
        getUserProfileImage(userProfile);
        TextView usergroup = (TextView) findViewById(R.id.nav_user_group);

        username.setText(userData.getUserName() + "님");
        if (userData.getGroupId() != 0) {
            usergroup.setText(groupData.getName());

        }
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commitAllowingStateLoss();
    }

    public UserData getUserData() {
        return this.userData;
    }

    public void setUserData(UserData userData){
        this.userData = userData;
    }

    public GroupData getGroupData() {
        return this.groupData;
    }

    public void setGroupData(GroupData groupData){
        this.groupData = groupData;
    }

    public BottomNavigationView getNavView() {
        return this.navView;
    }

    @Override
    public void onBackPressed() {
//        if(!navView.getMenu().getItem(0).isChecked()){
            navView.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commitAllowingStateLoss();
//        }
//        else {
//            this.finishAffinity();
//        }
    }

    private void getUserProfileImage(ImageView userProfile) {
        Log.d("url", userData.getUserImage());
        service.getProfileImage(userData.getUserImage()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String TAG = "MainActivity";
                if (response.isSuccessful()) {

                    Log.d(TAG, "server contacted and has file");
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Glide.with(MainActivity.this).load(bitmap).circleCrop().into(userProfile);

                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
//              Log.e("createGroup error",t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
