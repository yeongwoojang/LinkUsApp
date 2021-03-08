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
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TimerDialog extends AppCompatActivity {

    private TextView timer;
    private Button startBt, pauseBt, cancelBt, recordBt;
    private ImageButton closeBt;
    private LinearLayout containerPause;
    private TimerViewModel viewModel;

    private Intent intent;
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private String curTime;

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
        recordBt = (Button) findViewById(R.id.btn_record_timer);
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.MINUTE,30);
                calendar.set(Calendar.SECOND,0);
                String defaultTime = timeFormat.format(calendar.getTimeInMillis());
                try {
                    Date time = timeFormat.parse(timer.getText().toString());
                    Date t = timeFormat.parse(defaultTime);
                    if(time.before(t)){
                        Snackbar.make(findViewById(R.id.timerview), "공부시간 30분 전 까지는 기록할 수 없습니다..", Snackbar.LENGTH_SHORT).show();
                    }else{
                        viewModel.getTodayRecord(userNick); //사용자가 오늘 날짜에 지금까지 공부한 시간 조회
                        curTime = timer.getText().toString();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        viewModel.todayRecordLD.observe(this,timerInfo -> {
            if(timerInfo.getCode()==200){
                String recordTime = timerInfo.getTimer().get(0).getStudyTime();
                int hour = Integer.parseInt(recordTime.substring(0,2));
                int min = Integer.parseInt(recordTime.substring(3,5));
                int sec = Integer.parseInt(recordTime.substring(6));
                Log.d("recordTime", hour +" : "+ min+" : "+ sec );
                Calendar calendar = Calendar.getInstance();
                try {
                    Date date = timeFormat.parse(curTime);
                    calendar.setTime(date);

                    calendar.add(Calendar.HOUR,hour);
                    calendar.add(Calendar.MINUTE,min);
                    calendar.add(Calendar.SECOND,sec);

                    String returnDate = timeFormat.format(calendar.getTimeInMillis());
                    Log.d("returnDate", returnDate);
                    viewModel.updateTimer(userNick,returnDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                Log.d("not200", "오늘은 사용자가 공부 시간을 기록하지 않음.");

                viewModel.insertTimer(userNick, timer.getText().toString()); //오늘의 공부 시간을 최초로 기록
            }
            //공부 시간을 기록하면 타이머 초기화.
            stopService(intent);
            containerPause.setVisibility(View.INVISIBLE);
            startBt.setEnabled(true);
            startBt.setVisibility(View.VISIBLE);
        });

        viewModel.updateTimerLD.observe(this,response ->{
            if(response.equals("200")){
                Snackbar.make(findViewById(R.id.timerview), "오늘의 공부 시간을 업데이트 했습니다.", Snackbar.LENGTH_SHORT).show();
            }
        } );

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Service에 있는 time을 관찰하며 UI업데이트
        TimerService.time2.observe(this, time -> {
            Log.d("respoo", time);
            if (!time.equals("00:00:00")) {
                startBt.setVisibility(View.INVISIBLE);
                containerPause.setVisibility(View.VISIBLE);

            } else {
                startBt.setVisibility(View.VISIBLE);
                containerPause.setVisibility(View.INVISIBLE);

            }
            timer.setText(time);
        });

        viewModel.insertTimerLD.observe(this, response -> {
            if (response.equals("200")) {
                Snackbar.make(findViewById(R.id.timerview), "시간이 기록되었습니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.timerview), "시간이 기록을 실패했습니다..", Snackbar.LENGTH_SHORT).show();
            }
        });

        TimerService.isRunning.observe(this, isRunning -> {
            if (isRunning) {
                pauseBt.setText("pause");
            } else {
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