package com.example.linkusapp.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.linkusapp.R;

public class TimerDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 없애는 것
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_timer_dialog);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥 레이아웃 클릭 시 안닫히게 해버림.
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
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