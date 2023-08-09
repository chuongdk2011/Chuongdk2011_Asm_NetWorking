package com.example.chuongdkph26546_assignment.Interface;

import com.example.chuongdkph26546_assignment.DTO.Noidung;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TruyenInterface {


    @GET("truyen")
    Call<List<TruyenDTO>> getTruyen();

    @GET("truyen?")
    Call<List<TruyenDTO>> getTruyenByName(@Query("tentruyen")String name);

    @POST("truyen/addTruyen")
    Call<TruyenDTO> addTruyen (@Body TruyenDTO objU);

    @PUT("truyen/updateTruyen/{id}")
    Call<TruyenDTO> updateTruyen(@Path("id")String id, @Body TruyenDTO objU);

    @DELETE("truyen/delTruyen/{id}")
    Call<TruyenDTO> deleteTruyen(@Path("id") String id);

    @GET("truyen/chitiet/{id}")
    Call<List<Noidung>> getCTT(@Path("id")String id);
}
