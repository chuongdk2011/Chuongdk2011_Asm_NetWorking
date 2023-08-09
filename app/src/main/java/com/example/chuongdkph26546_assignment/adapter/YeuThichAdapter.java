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
import com.example.chuongdkph26546_assignment.CTTYeuthichActivity;
import com.example.chuongdkph26546_assignment.DTO.BinhLuanDTO;
import com.example.chuongdkph26546_assignment.DTO.YeuThichDTO;
import com.example.chuongdkph26546_assignment.R;

import java.util.ArrayList;

public class YeuThichAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    ArrayList<YeuThichDTO> list;
    OnLongClickDel onLongClickDel;

    public YeuThichAdapter(Context context, ArrayList<YeuThichDTO> list,OnLongClickDel onLongClickDel) {

        this.context = context;
        this.list = list;
        this.onLongClickDel = onLongClickDel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yt, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        YeuThichDTO yeuThichDTO = list.get(position);

        YeuThichAdapter.ItemViewHolder viewHolder = (YeuThichAdapter.ItemViewHolder) holder;

        Glide.with(context).load(yeuThichDTO.getAnhbia()).centerCrop().into(viewHolder.img_anhbia);
        viewHolder.tv_name.setText(yeuThichDTO.getTentruyen());
        viewHolder.tv_tg.setText(yeuThichDTO.getTacgia());
        viewHolder.tv_nxb.setText(yeuThichDTO.getNamxuatban());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLongClickDel.xoa(yeuThichDTO);
                return false;
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CTTYeuthichActivity.class);
                intent.putExtra("cttyt",yeuThichDTO);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_tg, tv_nxb;
        ImageView img_anhbia;

        public ItemViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.tv_tenyt);
            tv_tg = view.findViewById(R.id.tv_tgyt);
            tv_nxb = view.findViewById(R.id.tv_nxbyt);
            img_anhbia = view.findViewById(R.id.img_anhbiayt);
        }

    }

    public interface OnLongClickDel {
        void xoa(YeuThichDTO yeuThichDTO);
    }
}
