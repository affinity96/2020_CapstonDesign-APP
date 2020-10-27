package com.example.homekippa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser curUser;
    private FirebaseAuth mAuth;
    private Toolbar tb;
    private DrawerLayout leftDrawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton, chatButton;

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
                if(id == R.id.menu_item1){
                    Toast.makeText(getApplicationContext(), title + " 선택됨", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.menu_item2){
                    Toast.makeText(getApplicationContext(), title + " 선택됨", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.menu_logout){
                    Toast.makeText(getApplicationContext(), title + " 선택됨", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        //bottom navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
