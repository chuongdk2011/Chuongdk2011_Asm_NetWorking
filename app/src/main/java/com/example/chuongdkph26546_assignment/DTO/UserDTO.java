package com.example.chuongdkph26546_assignment.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDTO implements Serializable {
    @SerializedName("_id")
    public String id;
    public  String username;
    public  String email;
    public  String passwd;
    public  String fullname;


    public UserDTO() {
    }

    public UserDTO(String id, String username, String email, String passwd, String fullname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwd = passwd;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
