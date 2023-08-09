package com.example.chuongdkph26546_assignment.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chuongdkph26546_assignment.DTO.YeuThichDTO;
import com.example.chuongdkph26546_assignment.Interface.BinhLuanInterface;
import com.example.chuongdkph26546_assignment.R;
import com.example.chuongdkph26546_assignment.Interface.YeuThichInterface;
import com.example.chuongdkph26546_assignment.adapter.YeuThichAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FavoriteFragment extends Fragment implements YeuThichAdapter.OnLongClickDel {


    static final String BASE_URL = "http://172.20.10.14:3000/api/";

    String idU;
    ArrayList<YeuThichDTO> list;
    YeuThichAdapter adapter;
    RecyclerView rcv_yt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        SharedPreferences mypref = getContext().getSharedPreferences("Mypref", MODE_PRIVATE);
        idU = mypref.getString("id", "");
        rcv_yt = view.findViewById(R.id.rcv_YT);
        list = new ArrayList<>();
        adapter = new YeuThichAdapter(getContext(), list,this);
        rcv_yt.setAdapter(adapter);
        GetListYT();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    void GetListYT() {
        // tao gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        YeuThichInterface yeuThichInterface = retrofit.create(YeuThichInterface.class);

        //tạo đối tượng
        Call<List<YeuThichDTO>> objCall = yeuThichInterface.getYeuThich(idU);

        objCall.enqueue(new Callback<List<YeuThichDTO>>() {
            @Override
            public void onResponse(Call<List<YeuThichDTO>> call, Response<List<YeuThichDTO>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<YeuThichDTO>> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void xoa(YeuThichDTO yeuThichDTO) {
        new AlertDialog.Builder(getContext())
                .setTitle("Thông Báo")
                .setMessage("Bạn có muốn xóa truyện '" + yeuThichDTO.getTentruyen()+ "' khỏi danh sách yêu thích không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Gson gson = new GsonBuilder().setLenient().create();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        // sử dụng interface
                        YeuThichInterface yeuThichInterface = retrofit.create(YeuThichInterface.class);

                        //tạo đối tượng
                        Call<YeuThichDTO> objCall = yeuThichInterface.deleteYT(yeuThichDTO.getId());

                        objCall.enqueue(new Callback<YeuThichDTO>() {
                            @Override
                            public void onResponse(Call<YeuThichDTO> call, Response<YeuThichDTO> response) {
                                if (response.isSuccessful()) {


                                } else {
                                    Log.e("chuongdk", response.message());

                                }
                            }

                            @Override
                            public void onFailure(Call<YeuThichDTO> call, Throwable t) {
                                Log.e("chuongdk", t.getLocalizedMessage());
                            }
                        });
                        GetListYT();
                        Toast.makeText(getContext(), "Đã Xóa Khỏi Danh Sách Yêu Thích",
                                Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}