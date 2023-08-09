package com.example.chuongdkph26546_assignment.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class YeuThichDTO implements Serializable {

    @SerializedName("_id")
    String id;

    @SerializedName("id_user")
    String idUser;
    @SerializedName("id_truyen")
    String idTruyen;
    String tentruyen;
    String mota;
    String tacgia;
    String namxuatban;
    String anhbia;

    @SerializedName("anhnoidung")
    ArrayList<Noidung> noidung;

    String anhchitiet;

    public String getIdTruyen() {
        return idTruyen;
    }

    public void setIdTruyen(String idTruyen) {
        this.idTruyen = idTruyen;
    }

    public ArrayList<Noidung> getNoidung() {
        return noidung;
    }

    public void setNoidung(ArrayList<Noidung> noidung) {
        this.noidung = noidung;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public String getNamxuatban() {
        return namxuatban;
    }

    public void setNamxuatban(String namxuatban) {
        this.namxuatban = namxuatban;
    }

    public String getAnhbia() {
        return anhbia;
    }

    public void setAnhbia(String anhbia) {
        this.anhbia = anhbia;
    }

    public String getAnhchitiet() {
        return anhchitiet;
    }

    public void setAnhchitiet(String anhchitiet) {
        this.anhchitiet = anhchitiet;
    }


}
