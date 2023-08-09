package com.example.chuongdkph26546_assignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chuongdkph26546_assignment.DTO.BinhLuanDTO;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.DTO.UserDTO;
import com.example.chuongdkph26546_assignment.DTO.YeuThichDTO;
import com.example.chuongdkph26546_assignment.Interface.BinhLuanInterface;
import com.example.chuongdkph26546_assignment.Interface.YeuThichInterface;
import com.example.chuongdkph26546_assignment.adapter.BinhLuanAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CTTYeuthichActivity extends AppCompatActivity {

    static final String BASE_URL = "http://192.168.0.108:3000/api/";
    TextView tv_tg, tv_tenTruyen, tv_nxb, tv_ct;
    ImageView img_anhbia, img_anhct, img_backCTT,img_favorite;

    ArrayList<BinhLuanDTO> list;
    YeuThichDTO truyenDTO;
    BinhLuanAdapter binhLuanAdapter;
    RecyclerView rcv_bl;
    Button btn_addBl, btn_doctruyen;

    YeuThichDTO yeuThichDTO;
    ArrayList<YeuThichDTO> listYT;
    String idtruyen;
    String idU;
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
        setContentView(R.layout.activity_cttyeuthich);

        btn_doctruyen = findViewById(R.id.btn_doctruyen);
        tv_ct = findViewById(R.id.tv_motaCT);
        tv_nxb = findViewById(R.id.tv_nxb);
        tv_tenTruyen = findViewById(R.id.tv_ten);
        tv_tg = findViewById(R.id.tv_tg);
        img_anhct = findViewById(R.id.img_anhCT2);
        img_anhbia = findViewById(R.id.img_anhbia);
        img_backCTT = findViewById(R.id.img_backCTT);
        rcv_bl = findViewById(R.id.rcv_bl);
        btn_addBl = findViewById(R.id.btn_addBl);


        truyenDTO = (YeuThichDTO) getIntent().getSerializableExtra("cttyt");


        img_backCTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Log.d("chuongdk", "onCreate: " + truyenDTO.getAnhchitiet());

        tv_tenTruyen.setText(truyenDTO.getTentruyen());
        tv_tg.setText(truyenDTO.getTacgia());
        tv_nxb.setText(truyenDTO.getNamxuatban());
        tv_ct.setText(" -" + truyenDTO.getMota());

        Glide.with(this).load(truyenDTO.getAnhbia()).centerCrop().into(img_anhbia);
        Glide.with(this).load(truyenDTO.getAnhchitiet()).centerCrop().into(img_anhct);

        listYT = new ArrayList<>();

        list = new ArrayList<>();
        binhLuanAdapter = new BinhLuanAdapter(CTTYeuthichActivity.this, list);
        rcv_bl.setAdapter(binhLuanAdapter);
        getListBl();

        btn_addBl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBl();
            }
        });
        btn_doctruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CTTYeuthichActivity.this, DocTruyenActivity.class);
                intent.putExtra("idtruyen", truyenDTO.getIdTruyen());

                intent.putExtra("nametruyen", truyenDTO.getTentruyen());
                startActivity(intent);
            }
        });

        getListYT();
        SharedPreferences mypref = getSharedPreferences("Mypref", MODE_PRIVATE);
        idU = mypref.getString("id","");

        idtruyen = truyenDTO.getId();

        img_favorite =  findViewById(R.id.img_favorite);





        yeuThichDTO = new YeuThichDTO();

        Log.d("chuongdz", "onCreate: "+idU);

//        yeuThichDTO.setIdUser(idU);
//        yeuThichDTO.setAnhbia(truyenDTO.getAnhbia());
//        yeuThichDTO.setAnhchitiet(truyenDTO.getAnhchitiet());
//        yeuThichDTO.setMota(truyenDTO.getMota());
//        yeuThichDTO.setNamxuatban(truyenDTO.getNamxuatban());
//        yeuThichDTO.setTentruyen(truyenDTO.getTentruyen());
//        yeuThichDTO.setNoidung(truyenDTO.getNoidung());
//        yeuThichDTO.setTacgia(truyenDTO.getTacgia());
//        yeuThichDTO.setIdTruyen(truyenDTO.getId());



        mSocket.connect();
        // lắng nghe su kien
        mSocket.on("new bl", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                CTTYeuthichActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String data_sv_send = (String) args[0];

                        postNotify("Thông Báo", data_sv_send+"trong truyện "+truyenDTO.getTentruyen());

                    }
                });


            }
        });
        img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isHasUser = false;
                for (YeuThichDTO yeuThichDTO1 : listYT) {
                    if (idtruyen.equals(yeuThichDTO1.getIdTruyen()) && idU.equals(yeuThichDTO1.getIdUser())) {
                        isHasUser = true;

                        break;
                    }
                }
                if (isHasUser) {

                    Toast.makeText(CTTYeuthichActivity.this, truyenDTO.getTentruyen()+" already available in favorites", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(CTTYeuthichActivity.this)
                            .setTitle("Thông Báo")
                            .setMessage("Bạn có muốn thêm truyện "+truyenDTO.getTentruyen()+" vào danh sách yêu thích")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
//                                    addYT();
                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .show();


                }
            }
        });
    }


    public void getListBl() {
        // tao gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        BinhLuanInterface binhLuanInterface = retrofit.create(BinhLuanInterface.class);

        //tạo đối tượng
        Call<List<BinhLuanDTO>> objCall = binhLuanInterface.getBinhLuan(truyenDTO.getIdTruyen());

        objCall.enqueue(new Callback<List<BinhLuanDTO>>() {
            @Override
            public void onResponse(Call<List<BinhLuanDTO>> call, Response<List<BinhLuanDTO>> response) {
                if (response.isSuccessful()) {


                    list.clear();
                    list.addAll(response.body());
                    binhLuanAdapter.notifyDataSetChanged();

                    Log.d("chuongdk", "onResponse: " + list.size());
                    Log.d("chuongdk", "onResponse: " + response.body());

                } else {
                    Toast.makeText(CTTYeuthichActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BinhLuanDTO>> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());

            }
        });
    }

    public void addBl() {
        final Dialog dialog1 = new Dialog(CTTYeuthichActivity.this);
        dialog1.setContentView(R.layout.layout_dialog_bl);
        dialog1.setCancelable(false);

        Window window = dialog1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog1 != null && dialog1.getWindow() != null) {
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }


        EditText ed_bl = dialog1.findViewById(R.id.ed_bl);

        Button btn_them = dialog1.findViewById(R.id.btn_them);

        Button huy = dialog1.findViewById(R.id.btn_huy);
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences mypref = getSharedPreferences("Mypref", MODE_PRIVATE);
                String user = mypref.getString("user", "");
                String email = mypref.getString("email", "");
                String fullname = mypref.getString("fullname", "");
                String passwd = mypref.getString("passwd", "");
                String id = mypref.getString("id", "");

                UserDTO userDTO = new UserDTO();

                userDTO.setUsername(user);
                userDTO.setEmail(email);
                userDTO.setFullname(fullname);
                userDTO.setPasswd(passwd);
                userDTO.setId(id);

                BinhLuanDTO binhLuanDTO = new BinhLuanDTO();
                binhLuanDTO.setBinhluan(ed_bl.getText().toString());
                binhLuanDTO.setId_truyen(truyenDTO.getIdTruyen());
                Log.d("chuongdk", "onClick:id " + userDTO.getId());
                binhLuanDTO.setUserDTO(userDTO);

                //Tạo đối tượng chuyển đổi kiểu dữ liệu
                Gson gson = new GsonBuilder().setLenient().create();
                //Tạo Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                // Khởi  tạo interface

                BinhLuanInterface binhLuanInterface = retrofit.create(BinhLuanInterface.class);


                // Tạo Call
                Call<BinhLuanDTO> objCall = binhLuanInterface.addBL(binhLuanDTO);
                // Thực hiện gửi dữ liệu lên server
                objCall.enqueue(new Callback<BinhLuanDTO>() {
                    @Override
                    public void onResponse(Call<BinhLuanDTO> call, Response<BinhLuanDTO> response) {
                        // kết quả server trả về ở đây
                        if (response.isSuccessful()) {


                            // lấy kết quả trả về

                            Log.d("chuongdk", "onResponse: binh luan = "+response.body().getBinhluan());
                            getListBl();
                            Toast.makeText(CTTYeuthichActivity.this, "Bình luận thành công", Toast.LENGTH_SHORT).show();


                        } else {
                            Log.e("chuongdk", response.message());

                        }
                    }

                    @Override
                    public void onFailure(Call<BinhLuanDTO> call, Throwable t) {
                        // nếu xảy ra lỗi sẽ thông báo ở đây

                        Log.e("chuongdk", t.getLocalizedMessage());
                    }
                });
                dialog1.dismiss();

            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();

    }

    public  void addYT(){
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Khởi  tạo interface

        YeuThichInterface yeuThichInterface = retrofit.create(YeuThichInterface.class);



        // Tạo Call
        Call<YeuThichDTO> objCall = yeuThichInterface.addYT(yeuThichDTO);
        // Thực hiện gửi dữ liệu lên server
        objCall.enqueue(new Callback<YeuThichDTO>() {
            @Override
            public void onResponse(Call<YeuThichDTO> call, Response<YeuThichDTO> response) {
                // kết quả server trả về ở đây
                if (response.isSuccessful()) {
                    // lấy kết quả trả về

                    Log.d("chuongdz", "onResponse: id yeu thich = "+response.body().getId());
                    Toast.makeText(CTTYeuthichActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("chuongdk", response.message());

                }
            }

            @Override
            public void onFailure(Call<YeuThichDTO> call, Throwable t) {
                // nếu xảy ra lỗi sẽ thông báo ở đây

                Log.e("chuongdk", t.getLocalizedMessage());
            }
        });
    }

    void postNotify(String title, String content) {


        // Khởi tạo layout cho Notify
        Notification customNotification = new NotificationCompat.Builder(CTTYeuthichActivity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .build();

        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(CTTYeuthichActivity.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(CTTYeuthichActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(CTTYeuthichActivity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(CTTYeuthichActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy, customNotification);

    }

    public  void getListYT(){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        YeuThichInterface yeuThichInterface = retrofit.create(YeuThichInterface.class);

        //tạo đối tượng
        Call<List<YeuThichDTO>> objCall = yeuThichInterface.getlistYT();

        objCall.enqueue(new Callback<List<YeuThichDTO>>() {
            @Override
            public void onResponse(Call<List<YeuThichDTO>> call, Response<List<YeuThichDTO>> response) {
                if (response.isSuccessful()) {


                    listYT.clear();
                    listYT.addAll(response.body());


                    Log.d("chuongdz", "onResponse: listYT = " + listYT.size());
                    Log.d("chuongdz", "onResponse: " + response.body());

                } else {
                    Toast.makeText(CTTYeuthichActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<YeuThichDTO>> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());

            }
        });
    }
}