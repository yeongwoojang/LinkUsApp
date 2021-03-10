package com.example.linkusapp.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
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
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.R;
import com.example.linkusapp.view.activity.HomeActivity;
import com.example.linkusapp.view.activity.MainActivity;
import com.example.linkusapp.view.activity.TimerDialog;

import java.util.Calendar;

public class TimerService extends Service {

    //타이머를 관리하는 변수
    public static MutableLiveData<Boolean> isRunning = new MutableLiveData<>();
    public static MutableLiveData<String> time2 = new MutableLiveData<>();

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Thread timerThread = null;

    private AlarmManager alarmManager;
    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onStartCommand", "onStartCommand: ");
        registAlarm(intent);
        isRunning.setValue(true);
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_LOW));
        }

        builder = new NotificationCompat.Builder(getApplicationContext(),"default");
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setContentTitle("스톱워치");
        timerThread = new Thread(new TimerThread());
        timerThread.start(); //스톱워치 시작

//        notificationManager.notify(1,builder.build());
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        isRunning.setValue(true);
        timerThread.interrupt();
        Log.d("onStartCommand", "onDestroy: ");
        super.onDestroy();
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            Intent notiIntent = new Intent(getApplicationContext(), HomeActivity.class);
            notiIntent.setAction(Intent.ACTION_MAIN);
            notiIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notiIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentText(result);
            builder.setContentIntent(pendingIntent);
            startForeground(1,builder.build());
            time2.postValue(result);
        }
    };


    public void registAlarm(Intent serviceIntent){
        Intent intent = new Intent(getApplicationContext(),TimerReceiver.class);
        intent.putExtra("intent",serviceIntent);
        calendar.set(Calendar.HOUR_OF_DAY,24);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            alarmManager =  (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }else{
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }
    }

    public void unRegistAlarm(){
        Intent intent = new Intent(getApplicationContext(),TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public class TimerThread implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                while (isRunning.getValue()) {
                    Message msg = new Message();
                    Message msg2 = new Message();
                    msg.arg1 = i++;
//                    min = msg.arg1;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Log.d("onDestroy", "123");
                        msg2.arg1 = 0;
                        handler.sendMessage(msg2);
                        unRegistAlarm();
                        e.printStackTrace();
                        //인터럽트 발생 시 return
                        return;
                    }
                }
            }
        }
    }
}