package com.example.chuongdkph26546_assignment.Interface;

import com.example.chuongdkph26546_assignment.DTO.BinhLuanDTO;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.DTO.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BinhLuanInterface {


    @Headers("Content-Type: application/json")
    @GET("binhluan/{id}")
    Call<List<BinhLuanDTO>> getBinhLuan(@Path("id") String id);

    @POST("binhluan/addBL")
    Call<BinhLuanDTO> addBL (@Body BinhLuanDTO objU);

}
