package com.example.chuongdkph26546_assignment.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BinhLuanDTO {

    @SerializedName("_id")
    String id;
    String binhluan;
    String ngay;
    String id_truyen;
    @SerializedName("id_user")
    private UserDTO userDTO;


    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public void setBinhluan(String binhluan) {
        this.binhluan = binhluan;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getId_truyen() {
        return id_truyen;
    }

    public void setId_truyen(String id_truyen) {
        this.id_truyen = id_truyen;
    }

}
