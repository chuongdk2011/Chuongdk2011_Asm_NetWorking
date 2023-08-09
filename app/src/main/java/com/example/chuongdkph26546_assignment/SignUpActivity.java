package com.example.chuongdkph26546_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chuongdkph26546_assignment.DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SignUpActivity extends AppCompatActivity {

    static final String API = "http://172.20.10.14:3000/api/users";

    ImageView img_back;

    EditText ed_username, ed_fullname, ed_email, ed_passwd, ed_repasswd;
    Button btn_dangky;

    ArrayList<UserDTO> list;

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
        setContentView(R.layout.activity_sign_up);
        Unit();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        list = new ArrayList<>();
        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickReg(API + "/reg");
            }
        });
        checkLogin(API);

        mSocket.connect();
        // lắng nghe su kien
        mSocket.on("new msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SignUpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String data_sv_send = (String) args[0];

                        postNotify("Thông Báo", data_sv_send);

                    }
                });


            }
        });

    }

    private void Unit() {
        img_back = findViewById(R.id.img_back);
        ed_email = findViewById(R.id.ed_email);
        ed_fullname = findViewById(R.id.ed_fullname);
        ed_username = findViewById(R.id.ed_username);
        ed_passwd = findViewById(R.id.ed_passwd);
        ed_repasswd = findViewById(R.id.ed_repasswd);
        btn_dangky = findViewById(R.id.btn_dangky);


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
                            Log.d("chuongdk", "onCreate: username: " + user.getUsername());

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

    private void onClickReg(String link_api) {
        String user = ed_username.getText().toString();
        String pass = ed_passwd.getText().toString();

        if (user.length() == 0 || pass.length() == 0) {

        } else {
            if (list == null || list.isEmpty()) {
                return;
            }
            boolean isHasUser = false;
            for (UserDTO userDTO : list) {
                if (user.equals(userDTO.getUsername())) {
                    isHasUser = true;
                    Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (isHasUser == false) {
                String mk = ed_passwd.getText().toString();
                String nlmk = ed_repasswd.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = ed_email.getText().toString();


                if (ed_username.getText().length() == 0) {
                    ed_username.setError("Không được để trống!");
                } else if (ed_fullname.getText().length() == 0) {
                    ed_fullname.setError("Không được để trống!");
                } else if (email.length() == 0) {
                    ed_email.setError("Không được để trống!");
                } else if (mk.length() == 0) {
                    ed_passwd.setError("Không được để trống!");
                } else if (nlmk.length() == 0) {
                    ed_repasswd.setError("Không được để trống!");
                } else if (!mk.equalsIgnoreCase(nlmk)) {
                    ed_repasswd.setError("Mật khẩu không trùng khớp!!");
                } else if(!email.matches(emailPattern)) {
                    ed_email.setError("Email không đúng định dạng!!");
                }else{

                    ExecutorService service = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());
                    final String diachi = link_api;
                    service.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                URL url = new URL(diachi);
                                // mở kết nối
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                //Thiết lập phương thức POST, mặc định là GET
                                conn.setRequestMethod("POST");
                                //tạo đối tượng dữ liệu để gửi lên server
                                JSONObject postData = new JSONObject();

                                postData.put("username", ed_username.getText().toString());
                                postData.put("passwd", ed_passwd.getText().toString());
                                postData.put("fullname", ed_fullname.getText().toString());
                                postData.put("email", ed_email.getText().toString());

                                //thiết lập kiểu dữ liệu sẽ gửi lên server
                                conn.setRequestProperty("Content-Type", "application/json");

                                // Tạo đối tượng out dữ liệu ra khỏi ứng đụng để gửi lên server

                                OutputStream outputStream = conn.getOutputStream();

                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                                //ghi dữ liệu vào outPut
                                writer.append(postData.toString());

                                //xóa bộ đệm
                                writer.flush();
                                writer.close();
                                outputStream.close();
                                //chưa cần đóng connection: hãy thu nhận dữ liệu mà server phản hồi
                                InputStream inputStream = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                StringBuilder builder = new StringBuilder();
                                String dong;
                                while ((dong = reader.readLine()) != null) {
                                    builder.append(dong).append("/n");
                                }
                                reader.close();
                                inputStream.close();
                                conn.disconnect();


                                String phanhoi = builder.toString();


                                JSONObject jsonObject = new JSONObject(phanhoi);
                                Log.d("chuongdk", "run: " + jsonObject);

                                Gson gson = new Gson();
                                UserDTO user = gson.fromJson(String.valueOf(jsonObject), UserDTO.class);
                                Log.d("chuongdk", "onCreate: username: " + user.getId());

                                // gửi dữ liệu phản hồi lên webview để xem
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                    }
                                });

                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        }
    }

    void postNotify(String title, String content) {


        // Khởi tạo layout cho Notify
        Notification customNotification = new NotificationCompat.Builder(SignUpActivity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(R.drawable.baseline_done_24)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)


                .build();
        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(SignUpActivity.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(SignUpActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(SignUpActivity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(SignUpActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy, customNotification);

    }
}