package com.example.linkusapp.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.util.TimerService;
import com.example.linkusapp.viewModel.LoginViewModel;

public class TimerDialog extends AppCompatActivity {

    private TextView timer;
    private Button startBt, pauseBt, cancelBt;
    private ImageButton closeBt;
    private LinearLayout containerPause;
    private Thread timerThread = null;
    boolean isRunning = true;
    private String serviceStringData;
    private int serviceIntData;
    private LoginViewModel viewModel;

    private String fromNotiTime = "";
    private int fromNotiMin = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 없애는 것
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_timer_dialog);

        timer = (TextView) findViewById(R.id.timer);
        startBt = (Button) findViewById(R.id.btn_timer_start);
        pauseBt = (Button) findViewById(R.id.btn_timer_pause);
        cancelBt = (Button) findViewById(R.id.btn_cancel_timer);
        closeBt = (ImageButton) findViewById(R.id.btn_dialog_close);
        containerPause = (LinearLayout) findViewById(R.id.container_pause);

        //장영우 추가부분
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        User mUser = viewModel.getUserInfoFromShared();
        if (mUser != null) {
            Intent intent = getIntent();
            fromNotiTime = intent.getStringExtra("noti_time");
            fromNotiMin = intent.getIntExtra("noti_min", 0);
            if(fromNotiTime!=null && fromNotiMin!=0){
                Log.d("asdasdasd", fromNotiTime + ":" + fromNotiMin);
                timer.setText(fromNotiTime);
                timerThread = new Thread(new TimerThread());
                timerThread.start(); //스톱워치 시작
            }

        }

        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerThread = new Thread(new TimerThread());
                timerThread.start(); //스톱워치 시작
                startBt.setVisibility(View.INVISIBLE);
                containerPause.setVisibility(View.VISIBLE);
            }

        });

        pauseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pauseBt.getText().toString().equals("pause")) {
                    pauseBt.setText("start");
                } else {
                    pauseBt.setText("pause");
                }
                isRunning = !isRunning;
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerThread.interrupt(); //스톱워치 종료
                containerPause.setVisibility(View.INVISIBLE);
                startBt.setVisibility(View.VISIBLE);
                timer.setText("00:00:00:00");
                isRunning = true;
            }
        });

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"default");
//                builder.setSmallIcon(R.mipmap.app_icon);
//                builder.setContentTitle("스톱워치");
//                builder.setContentText(timer.getText().toString());
//                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//                    notificationManager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
//                }
//
//                notificationManager.notify(1,builder.build());
                Intent intent = new Intent(getApplicationContext(), TimerService.class);
                intent.putExtra("time", serviceStringData);
                intent.putExtra("min", serviceIntData);
                startService(intent);
                if (timerThread != null) {
                    timerThread.interrupt(); //스톱워치 종료
                }
                finish();
                Intent goToMainIntent = new Intent(TimerDialog.this,MainActivity.class);
                startActivity(goToMainIntent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥 레이아웃 클릭 시 안닫히게 해버림.
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //백버튼 막아버림
        return;
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            serviceStringData = result;
            Log.d("timeThread : ", result);
            timer.setText(result);
        }
    };

    public class TimerThread implements Runnable {
        @Override
        public void run() {
            int i = 0;
            if(fromNotiMin!=-1){
                i = fromNotiMin;
            }
            while (true) {
                while (isRunning) {
                    Message msg = new Message();
                    msg.arg1 = i++;
                    serviceIntData = msg.arg1; //service에 넘길 값
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //UI작업 하는 곳
                                timer.setText("00:00:00:00");
                            }
                        });
                        //인터럽트 발생 시 return
                        return;
                    }
                }
            }
        }
    }
}