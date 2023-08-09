package com.example.chuongdkph26546_assignment.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chuongdkph26546_assignment.DTO.Noidung;
import com.example.chuongdkph26546_assignment.DTO.TruyenDTO;
import com.example.chuongdkph26546_assignment.DocTruyenActivity;
import com.example.chuongdkph26546_assignment.R;

import java.util.ArrayList;

public class DocTruyenAdapter extends BaseAdapter {

    Context context;
    ArrayList<Noidung> list;

    public DocTruyenAdapter(Context context, ArrayList<Noidung> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        Noidung noidung = list.get(i);

        return noidung;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        if (view == null) {
            view1 = View.inflate(viewGroup.getContext(), R.layout.item_doctruyen, null);
        } else {
            view1 = view;
        }

        Noidung noidung = list.get(i);
        ImageView img_noidung = view1.findViewById(R.id.img_noidung);

        Glide.with(view1.getContext()).load(noidung.getAnhnd()).into(img_noidung);


        return  view1;
    }
}
