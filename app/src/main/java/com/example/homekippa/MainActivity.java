package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.GroupFragment;
import com.example.homekippa.ui.group.SingleItemPet;
import com.example.homekippa.ui.home.HomeFragment;
import com.example.homekippa.ui.manage.ManageFragment;
import com.example.homekippa.ui.notifications.NotificationsFragment;
import com.example.homekippa.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.security.acl.Group;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser curUser;
    private FirebaseAuth mAuth;
    private Toolbar tb;
    private DrawerLayout leftDrawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton, chatButton;
    private GroupFragment groupFragment;
    private HomeFragment homeFragment;
    private ManageFragment manageFragment;
    private NotificationsFragment notificationFragment;
    private SearchFragment searchFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
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
        userData = (UserData)intent.getExtras().get("user");
        Toast.makeText(getApplicationContext(), userData.getUserName() + "님 로그인", Toast.LENGTH_LONG).show();

        //tob navigation
        tb = findViewById(R.id.top_bar);
        setSupportActionBar(tb);
        //좌측메뉴 버튼
        menuButton = findViewById(R.id.top_btn_menu);
        leftDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.top_nav_view);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        getGroupData(userData.getGroupId());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //좌측메뉴 열기
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
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFrag(0);
                        break;
                    case R.id.navigation_search:
                        setFrag(1);
                        break;
                    case R.id.navigation_manage:
                        setFrag(2);
                        break;
                    case R.id.navigation_notifications:
                        setFrag(3);
                        break;
                    case R.id.navigation_group:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        setBottomNav();
        //bottom navigation
    }


    private void setBottomNav() {
        groupFragment = new GroupFragment();
        homeFragment = new HomeFragment();
        manageFragment = new ManageFragment();
        notificationFragment = new NotificationsFragment();
        searchFragment = new SearchFragment();
        setFrag(0);
    }

    private void setFrag(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (i) {
            case 0:
                ft.replace(R.id.nav_host_fragment, new HomeFragment());
                ft.commit();
               break;
            case 1:
                ft.replace(R.id.nav_host_fragment, new SearchFragment());
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.nav_host_fragment, new ManageFragment());
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.nav_host_fragment, new NotificationsFragment());
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.nav_host_fragment, new GroupFragment());
                ft.commit();
                break;
        }
    }


    //replacing the fragment in nav_host_fragment- activity main
    public void replaceGroupFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment).commit();
    }

    public UserData getUserData(){
        return this.userData;
    }


    public void getGroupData(int ID) {
        Log.d("그룹 확인", "들어옴");
        service.getGroupData(ID).enqueue(new Callback<GroupData>() {
            @Override
            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                if (response.isSuccessful()) {
                    Log.d("그룹 확인", "성공");
                    groupData = response.body();

                    main_naviheader = (ConstraintLayout) findViewById(R.id.naviheader_container);
                    main_naviheader.setBackgroundResource(R.drawable.base_cover);
                    TextView username = findViewById(R.id.user_name);
                    username.setText(userData.getUserName() + "님");
                    TextView usergroup = findViewById(R.id.user_group);
                    usergroup.setText(groupData.getGroupName());
                }
            }

            @Override
            public void onFailure(Call<GroupData> call, Throwable t) {
                Log.d("그룹 확인", "에러");
                Log.e("그룹 확인", t.getMessage());
            }
        });
    }

    public GroupData getGroupData(){
        return this.groupData;
    }
}
