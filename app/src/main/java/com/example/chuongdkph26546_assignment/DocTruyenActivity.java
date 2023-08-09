package com.example.chuongdkph26546_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chuongdkph26546_assignment.DTO.Noidung;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.Interface.TruyenInterface;
import com.example.chuongdkph26546_assignment.adapter.DocTruyenAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DocTruyenActivity extends AppCompatActivity {

    static final String BASE_URL = "http://172.20.10.14:3000/api/";


    ArrayList<Noidung> list;

    DocTruyenAdapter adapter;

    ListView lv_doctruyen;
    String idtruyen;
    ImageView img_backDT;
    TextView tv_tilte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);

        lv_doctruyen = findViewById(R.id.lv_doctruyen);
        img_backDT = findViewById(R.id.img_backDT);
        tv_tilte = findViewById(R.id.tv_titleDT);
        img_backDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        idtruyen = getIntent().getStringExtra("idtruyen");
        tv_tilte.setText(getIntent().getStringExtra("nametruyen"));
        Log.d("chuongdk", "onCreate: idtruyen="+idtruyen);
        GetListTruyen();
        list = new ArrayList<>();
        adapter = new DocTruyenAdapter(this, list);
        lv_doctruyen.setAdapter(adapter);

    }

    void GetListTruyen() {
        // tao gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        TruyenInterface truyenInterface = retrofit.create(TruyenInterface.class);

        //tạo đối tượng
        Call<List<Noidung>> objCall = truyenInterface.getCTT(idtruyen);

        objCall.enqueue(new Callback<List<Noidung>>() {
            @Override
            public void onResponse(Call<List<Noidung>> call, Response<List<Noidung>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.d("chuongdk", "onResponse: doctruyen list = "+list.size());


                } else {
                    Toast.makeText(DocTruyenActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Noidung>> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());

            }
        });
    }
}