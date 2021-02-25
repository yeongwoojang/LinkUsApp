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
import com.example.linkusapp.viewModel.TimerViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;

public class TimerDialog extends AppCompatActivity {

    private TextView timer;
    private Button startBt, pauseBt, cancelBt, recordBt;
    private ImageButton closeBt;
    private LinearLayout containerPause;
    private TimerViewModel viewModel;

    private Intent intent;
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
        recordBt = (Button)findViewById(R.id.btn_record_timer);
        closeBt = (ImageButton) findViewById(R.id.btn_dialog_close);
        containerPause = (LinearLayout) findViewById(R.id.container_pause);
        String userNick = getIntent().getStringExtra("userNick");
        viewModel = new ViewModelProvider(this).get(TimerViewModel.class);

        intent = new Intent(getApplicationContext(), TimerService.class);

        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBt.setEnabled(false); //스레드 두번연속 실행을 막기위함
                startService(intent);
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
                TimerService.isRunning.setValue(!TimerService.isRunning.getValue());
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timerThread.interrupt(); //스톱워치 종료
                stopService(intent);
                containerPause.setVisibility(View.INVISIBLE);
                startBt.setEnabled(true);
                startBt.setVisibility(View.VISIBLE);
            }
        });

        recordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.insertTimer(userNick,timer.getText().toString());
            }
        });

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Service에 있는 time을 관찰하며 UI업데이트
        TimerService.time2.observe(this,time -> {
            Log.d("respoo", time);
            if(!time.equals("00:00:00")){
                startBt.setVisibility(View.INVISIBLE);
                containerPause.setVisibility(View.VISIBLE);

            }else{
                startBt.setVisibility(View.VISIBLE);
                containerPause.setVisibility(View.INVISIBLE);

            }
            timer.setText(time);
        });

        viewModel.insertTimerLD.observe(this,response ->{
            if(response.equals("200")){
                Snackbar.make(findViewById(R.id.timerview),"시간이 기록되었습니다.",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.timerview),"시간이 기록을 실패했습니다..",Snackbar.LENGTH_SHORT).show();
            }
        } );

        TimerService.isRunning.observe(this,isRunning -> {
            if(isRunning){
                pauseBt.setText("pause");
            }else{
                pauseBt.setText("start");
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
}