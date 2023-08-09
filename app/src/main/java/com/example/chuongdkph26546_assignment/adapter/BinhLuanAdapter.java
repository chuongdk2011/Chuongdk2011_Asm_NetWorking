package com.example.chuongdkph26546_assignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chuongdkph26546_assignment.CTTruyenActivity;
import com.example.chuongdkph26546_assignment.DTO.BinhLuanDTO;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.R;

import java.util.ArrayList;

public class BinhLuanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    ArrayList<BinhLuanDTO> list;


    public BinhLuanAdapter(Context context, ArrayList<BinhLuanDTO> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_binhluan,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BinhLuanDTO binhLuanDTO = list.get(position);

        BinhLuanAdapter.ItemViewHolder viewHolder = (BinhLuanAdapter.ItemViewHolder) holder;


        viewHolder.tv_nameBL.setText(binhLuanDTO.getUserDTO().getUsername());
        viewHolder.tv_bl.setText(binhLuanDTO.getBinhluan());
        viewHolder.tv_date.setText(binhLuanDTO.getNgay());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bl,tv_nameBL,tv_date;



        public ItemViewHolder(View view) {
            super(view);

            tv_nameBL = view.findViewById(R.id.tv_nameBL);
            tv_bl = view.findViewById(R.id.tv_bl);
            tv_date = view.findViewById(R.id.tv_date);
        }

    }
}
