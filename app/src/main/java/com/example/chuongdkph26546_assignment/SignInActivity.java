package com.example.chuongdkph26546_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chuongdkph26546_assignment.DTO.UserDTO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignInActivity extends AppCompatActivity {

    String TAG = "chuongdk";
    static final String API = "http://172.20.10.14:3000/api/users";
    ArrayList<UserDTO> list = new ArrayList<>();
    UserDTO mUser = new UserDTO();
    EditText ed_username, ed_passwd;
    TextView tv_chuadangky;
    Button btn_dangnhap;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        tv_chuadangky = findViewById(R.id.tv_chuadangky);
        ed_username = findViewById(R.id.ed_username);
        ed_passwd = findViewById(R.id.ed_matkhau);

        saveLoginCheckBox = findViewById(R.id.cbo_saveLogin);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            ed_username.setText(loginPreferences.getString("username", ""));
            ed_passwd.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        checkLogin(API);
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLogin();
                onClickLogin();
            }
        });
        tv_chuadangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkLogin(String link_api) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        service.execute(new Runnable() {
            @Override
            public void run() {
                String dia_chi = link_api;
                String noidung = "";
                //1. Tạo đối tượng url
                try {
                    URL url = new URL(dia_chi);
                    //2. Mở kết nối
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //3. Tạo đối tượng đọc luồng dữ liệu
                    InputStream inputStream = conn.getInputStream();
                    //4. Tạo biến đê đọc dữ liệu
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    //5. Tạo biến ghép nối dữ liệu
                    StringBuilder builder = new StringBuilder();
                    String dong; // dòng dữ liệu đọc được
                    // đọc dữ liệu
                    while ((dong = reader.readLine()) != null) {
                        builder.append(dong).append("\n");
                    }
                    // kết thúc quá trình đọc:
                    reader.close();
                    inputStream.close();
                    conn.disconnect();
                    noidung = builder.toString();

                    Log.d("chuongdk", "run: noi dung = " + noidung);
                    try {
                        JSONArray array = new JSONArray(noidung);
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);

                            Gson gson = new Gson();
                            UserDTO user = gson.fromJson(String.valueOf(object), UserDTO.class);
                            Log.d(TAG, "onCreate: username: " + user.getUsername());

                            list.add(user);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                //----- lấy xong dữ liệu, xử lý hiển thị lên giao diện

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // cập nhật giao diện

                    }
                });

            }
        });
    }


    private void onClickLogin() {
        String user = ed_username.getText().toString();
        String pass = ed_passwd.getText().toString();

        if (user.length() == 0 || pass.length() == 0) {
            Toast.makeText(this, "Không Được Để Trống", Toast.LENGTH_SHORT).show();
        } else {
            if (list == null || list.isEmpty()) {
                return;
            }
            boolean isHasUser = false;
            for (UserDTO userDTO : list) {
                if (user.equals(userDTO.getUsername()) && pass.equals(userDTO.getPasswd())) {
                    isHasUser = true;
                    mUser = userDTO;

                    SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("Mypref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user",userDTO.getUsername());
                    editor.putString("email",userDTO.getEmail());
                    editor.putString("fullname",userDTO.getFullname());
                    editor.putString("id",userDTO.getId());
                    editor.putString("passwd",userDTO.getPasswd());
                    editor.commit();

                    break;
                }
            }
            if (isHasUser) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra("user", mUser);
                startActivity(intent);
            } else {
                Toast.makeText(SignInActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  void saveLogin(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_username.getWindowToken(), 0);

        username = ed_username.getText().toString();
        password = ed_passwd.getText().toString();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

    }

}