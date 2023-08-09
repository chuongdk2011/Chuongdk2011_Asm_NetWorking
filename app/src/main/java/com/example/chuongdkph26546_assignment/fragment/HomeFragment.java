package com.example.chuongdkph26546_assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.R;
import com.example.chuongdkph26546_assignment.Interface.TruyenInterface;
import com.example.chuongdkph26546_assignment.adapter.TruyenAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {


    ImageSlider image_slide;
    static final String BASE_URL = "http://172.20.10.14:3000/api/";
    TruyenAdapter adapter;
    ArrayList<TruyenDTO> list;
    RecyclerView rcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        image_slide = view.findViewById(R.id.image_slider);

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.anhbia_demon_salyer,"Demon Slayer",null));
        images.add(new SlideModel(R.drawable.anhbia_fairytale,"Fairy Tail",null));
        images.add(new SlideModel(R.drawable.anhbia_onepiece,"One Piece",null));
        images.add(new SlideModel(R.drawable.anhbia_bokunohero,"My Hero Academia",null));
        images.add(new SlideModel(R.drawable.anhbia_doraemon,"Doraemon",null));

        image_slide.setImageList(images, ScaleTypes.CENTER_CROP);

        image_slide.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Toast.makeText(view.getContext(), "ảnh số "+i, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void doubleClick(int i) {

            }
        });

        ImageView img_search = view.findViewById(R.id.img_search);
        EditText ed_search = view.findViewById(R.id.ed_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetListTruyenByName(ed_search.getText().toString());
            }
        });

        rcv = view.findViewById(R.id.rcv_truyen);

        list = new ArrayList<>();
        adapter = new TruyenAdapter(getContext(), list);
        rcv.setAdapter(adapter);
        GetListTruyen();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        Call<List<TruyenDTO>> objCall = truyenInterface.getTruyen();

        objCall.enqueue(new Callback<List<TruyenDTO>>() {
            @Override
            public void onResponse(Call<List<TruyenDTO>> call, Response<List<TruyenDTO>> response) {
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
            public void onFailure(Call<List<TruyenDTO>> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());

            }
        });
    }

    void GetListTruyenByName(String name) {
        // tao gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        TruyenInterface truyenInterface = retrofit.create(TruyenInterface.class);

        //tạo đối tượng
        Call<List<TruyenDTO>> objCall = truyenInterface.getTruyenByName(name);

        objCall.enqueue(new Callback<List<TruyenDTO>>() {
            @Override
            public void onResponse(Call<List<TruyenDTO>> call, Response<List<TruyenDTO>> response) {
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
            public void onFailure(Call<List<TruyenDTO>> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());

            }
        });
    }

}