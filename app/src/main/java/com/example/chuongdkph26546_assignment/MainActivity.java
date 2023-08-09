package com.example.chuongdkph26546_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chuongdkph26546_assignment.DTO.UserDTO;
import com.example.chuongdkph26546_assignment.fragment.FavoriteFragment;
import com.example.chuongdkph26546_assignment.fragment.HomeFragment;
import com.example.chuongdkph26546_assignment.fragment.ProfileFragment;
import com.example.chuongdkph26546_assignment.fragment.RepassFragment;
import com.google.android.material.navigation.NavigationView;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;

    private static  final int FRAGMENT_HOME = 0;
    private static  final int FRAGMENT_FAVORITE = 1;

    private static  final int FRAGMENT_PROFILE = 2;
    private static  final int FRAGMENT_REPASS = 3;
    private  int currentFragment = FRAGMENT_HOME;
    Toolbar toolbar;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://172.20.10.14:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.layout_drawer);

        getSupportActionBar().setTitle("Home");


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        UserDTO userDTO = (UserDTO) getIntent().getSerializableExtra("user");

        View headerView = navigationView.getHeaderView(0);
        TextView navFullname = (TextView) headerView.findViewById(R.id.tv_nav_fullname);
        TextView navEmail = (TextView) headerView.findViewById(R.id.tv_nav_email);
        navFullname.setText(userDTO.getFullname());
        navEmail.setText(userDTO.getEmail());

        mSocket.connect();
        // lắng nghe su kien
        mSocket.on("new tt", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String data_sv_send = (String) args[0];
                        postNotify("Thông Báo", data_sv_send);

                    }
                });
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            if (currentFragment != FRAGMENT_HOME){
                toolbar.setTitle("Home");
                replaceFragment(new HomeFragment());
                currentFragment = FRAGMENT_HOME;
            }
        }else if (id == R.id.nav_favorite){
            if (currentFragment != FRAGMENT_FAVORITE){
                toolbar.setTitle("Favorite");
                replaceFragment(new FavoriteFragment());
                currentFragment = FRAGMENT_FAVORITE;
            }
        }
        else if (id == R.id.nav_profile){
            if (currentFragment != FRAGMENT_PROFILE){
                toolbar.setTitle("Profile");
                replaceFragment(new ProfileFragment());
                currentFragment = FRAGMENT_PROFILE;
            }
        }
        else if (id == R.id.nav_repass){
            if (currentFragment != FRAGMENT_REPASS){
                toolbar.setTitle("Repass");
                replaceFragment(new RepassFragment());
                currentFragment = FRAGMENT_REPASS;
            }
        }else if (id == R.id.nav_logout){
            SharedPreferences preferences = getSharedPreferences("Mypref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
            editor.clear();
            editor.commit();
            finish();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //nếu mở navigation thì ấn nút back sẽ tắt
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void  replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_frame,fragment);
        transaction.commit();
    }
    void postNotify(String title, String content) {


        // Khởi tạo layout cho Notify
        Notification customNotification = new NotificationCompat.Builder(MainActivity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)


                .build();
        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(MainActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy, customNotification);


    }
}