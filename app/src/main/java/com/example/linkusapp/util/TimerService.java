package com.example.linkusapp.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.linkusapp.R;
import com.example.linkusapp.view.activity.HomeActivity;
import com.example.linkusapp.view.activity.MainActivity;
import com.example.linkusapp.view.activity.TimerDialog;

public class TimerService extends Service {


    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Thread timerThread = null;
    private boolean isRunning = true;
    private String time;
    private int min;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("comm", "onStartCommand: ");
        time = intent.getStringExtra("time");
        min = intent.getIntExtra("min",0);
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_LOW));
        }



        builder = new NotificationCompat.Builder(getApplicationContext(),"default");
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setContentTitle("스톱워치");
        builder.setContentText(time);


        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_LOW));
        }
        timerThread = new Thread(new TimerThread());
        timerThread.start(); //스톱워치 시작


//        notificationManager.notify(1,builder.build());
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "onDestroy: ");
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            Intent notiIntent = new Intent(getApplicationContext(), TimerDialog.class);
            notiIntent.putExtra("noti_time",time);
            notiIntent.putExtra("noti_min",min);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notiIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentText(result);
            builder.setContentIntent(pendingIntent);
            time = result;
            startForeground(1,builder.build());
            Log.d("serviceTh : ", result);
        }
    };

    public class TimerThread implements Runnable {
        @Override
        public void run() {
            int i = min;

            while (true) {
                while (isRunning) {
                    Message msg = new Message();
                    msg.arg1 = i++;
                    min = msg.arg1;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                        //인터럽트 발생 시 return
                        return;
                    }
                }
            }
        }
    }
}
