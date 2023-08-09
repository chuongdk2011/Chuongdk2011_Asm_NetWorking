package com.example.chuongdkph26546_assignment;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

public class NotifyConfig extends Application {
    public static final String CHANEL_ID = "CHUONGDK"; //tự đặt thành tên mới

    @Override
    public void onCreate() {
        super.onCreate();
        config();
    }

    void config() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tên chanel
            CharSequence name = "ten_chanel"; // tên hiển thị trong cài đặt notify của điện thoại
            // mo ta:
            String mota = "Mo ta";
            int do_uu_tien = NotificationManager.IMPORTANCE_DEFAULT;
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.thongbao2);
//            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            // đăng ký notify
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, do_uu_tien);

            channel.setDescription(mota);

            channel.setSound(sound, audioAttributes);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}