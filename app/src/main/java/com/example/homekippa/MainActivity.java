package com.example.homekippa;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

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
        BottomNavigationView navView = findViewById(R.id.nav_view);
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
                    Toast.makeText(getApplicationContext(), title + " 선택됨", Toast.LENGTH_SHORT).show();
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
                ft.replace(R.id.nav_host_fragment, homeFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.nav_host_fragment, searchFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.nav_host_fragment, manageFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.nav_host_fragment, notificationFragment);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.nav_host_fragment, groupFragment);
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

}
