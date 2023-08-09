package com.example.chuongdkph26546_assignment.Interface;

import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.DTO.YeuThichDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface YeuThichInterface {

    @GET("yeuthich/{id}")
    Call<List<YeuThichDTO>> getYeuThich(@Path("id") String id);

    @POST("yeuthich/addYT")
    Call<YeuThichDTO> addYT(@Body YeuThichDTO objYT);

    @GET("yeuthich")
    Call<List<YeuThichDTO>> getlistYT();

    @DELETE("yeuthich/delYT/{id}")
    Call<YeuThichDTO> deleteYT(@Path("id") String id);
}
