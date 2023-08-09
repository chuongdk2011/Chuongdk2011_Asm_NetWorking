package com.example.chuongdkph26546_assignment.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chuongdkph26546_assignment.R;
import com.example.chuongdkph26546_assignment.SignInActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RepassFragment extends Fragment {

    EditText ed_mkcu,ed_mkmoi,ed_nlmkmoi;
    Button btn_doimk;
    private String api = "http://172.20.10.14:3000/api/users";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_repass, container, false);

//        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                requireActivity().onBackPressed();
//                // in here you can do logic when backPress is clicked
//            }
//        });

        ed_mkcu = view.findViewById(R.id.ed_mkcu);
        ed_mkmoi = view.findViewById(R.id.ed_matkhaumoi);
        ed_nlmkmoi = view.findViewById(R.id.ed_nlmatkhaumoi);
        btn_doimk = view.findViewById(R.id.btn_doimk);


        btn_doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDoimk();
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public  void  clickDoimk(){

        SharedPreferences mypref = getContext().getSharedPreferences("Mypref", MODE_PRIVATE);
        String mk_cu = mypref.getString("passwd","");
        String idU = mypref.getString("id","");
        String edmkcu = ed_mkcu.getText().toString().trim();
        String mkmoi = ed_mkmoi.getText().toString().trim();
        String nlmkmoi = ed_nlmkmoi.getText().toString().trim();

        if (edmkcu.length()==0){
            ed_mkcu.setError("Không được để trống");
        }else if(mkmoi.length()==0){
            ed_mkmoi.setError("Không được để trống");
        } else if(nlmkmoi.length()==0){
            ed_nlmkmoi.setError("Không được để trống");
        }else if(!mk_cu.equalsIgnoreCase(edmkcu)){
            ed_mkcu.setError("Mật khẩu cũ không chính xác");
        }else if(!mkmoi.equalsIgnoreCase(nlmkmoi)){
            ed_nlmkmoi.setError("Mật khẩu mới không trùng khớp");
        }else{
            ExecutorService service = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            final String diachi = api + "/update/" + idU;
            service.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        URL url = new URL(diachi);
                        // mở kết nối
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //Thiết lập phương thức POST, mặc định là GET
                        conn.setRequestMethod("PUT");
                        //tạo đối tượng dữ liệu để gửi lên server
                        JSONObject postData = new JSONObject();

                        postData.put("passwd", mkmoi);

                        //thiết lập kiểu dữ liệu sẽ gửi lên server
                        conn.setRequestProperty("Content-Type", "application/json");

                        // Tạo đối tượng out dữ liệu ra khỏi ứng đụng để gửi lên server

                        OutputStream outputStream = conn.getOutputStream();

                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                        //ghi dữ liệu vào outPut
                        writer.append(postData.toString());

                        //xóa bộ đệm
                        writer.flush();
                        writer.close();
                        outputStream.close();
                        //chưa cần đóng connection: hãy thu nhận dữ liệu mà server phản hồi
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder builder = new StringBuilder();
                        String dong;
                        while ((dong = reader.readLine()) != null) {
                            builder.append(dong).append("/n");
                        }
                        reader.close();
                        inputStream.close();
                        conn.disconnect();

                        String phanhoi = builder.toString();

                        // gửi dữ liệu phản hồi lên webview để xem
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getContext(), "Đã đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), SignInActivity.class);
                                startActivity(intent);
                            }
                        });


                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }


    }
}