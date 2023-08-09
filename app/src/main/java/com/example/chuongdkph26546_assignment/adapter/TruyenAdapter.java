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
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.R;

import java.io.Serializable;
import java.util.ArrayList;

public class TruyenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    ArrayList<TruyenDTO> list;


    public TruyenAdapter(Context context, ArrayList<TruyenDTO> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_truyen,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TruyenDTO truyenDTO = list.get(position);

        TruyenAdapter.ItemViewHolder viewHolder = (TruyenAdapter.ItemViewHolder) holder;

        Glide.with(context).load(truyenDTO.getAnhbia()).centerCrop().into(viewHolder.img_anhBia);
        viewHolder.tv_tenTruyen.setText(truyenDTO.getTentruyen());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CTTruyenActivity.class);
                intent.putExtra("ctt",  truyenDTO);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenTruyen;

        ImageView img_anhBia;

        public ItemViewHolder(View view) {
            super(view);

            tv_tenTruyen = view.findViewById(R.id.tv_tentruyen);
            img_anhBia = view.findViewById(R.id.img_anhbia);


        }

    }

}
