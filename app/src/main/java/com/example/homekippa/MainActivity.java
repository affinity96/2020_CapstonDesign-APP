package com.example.homekippa;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.homekippa.ui.group.GroupFragment;
import com.example.homekippa.ui.group.SingleItemPet;
import com.example.homekippa.ui.home.HomeFragment;
import com.example.homekippa.ui.notifications.NotificationsFragment;
import com.example.homekippa.ui.search.SearchFragment;
import com.example.homekippa.ui.walk.WalkFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();
        Toast.makeText(getApplicationContext(), curUser.getEmail() + "님 로그인", Toast.LENGTH_LONG).show();

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
                leftDrawerLayout.openDrawer(navigationView);
            }
        });
        //채팅 버튼 - To Do
        chatButton = findViewById(R.id.top_btn_chat);
        //좌측 메뉴
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commitAllowingStateLoss();
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new GroupFragment()).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });
    }
}
