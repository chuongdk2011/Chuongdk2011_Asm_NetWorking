package com.example.chuongdkph26546_assignment.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.R;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences mypref = getContext().getSharedPreferences("Mypref", MODE_PRIVATE);
        String user = mypref.getString("user", "");
        String email = mypref.getString("email", "");
        String fullname = mypref.getString("fullname", "");
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_username = view.findViewById(R.id.tv_tentaikhoan);
        TextView tv_email = view.findViewById(R.id.tv_email);

        tv_name.setText(fullname);
        tv_username.setText(user);
        tv_email.setText(email);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}