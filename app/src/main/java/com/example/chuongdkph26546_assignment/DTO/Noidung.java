package com.example.chuongdkph26546_assignment.DTO;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Noidung implements Serializable {
    @Expose
    String anhnd;


    public String getAnhnd() {
        return anhnd;
    }

    public void setAnhnd(String anhnd) {
        this.anhnd = anhnd;
    }

}
