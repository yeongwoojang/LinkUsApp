package com.example.linkusapp.util;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.stopService(intent.getParcelableExtra("intent")); //매일 밤12시가 되면 돌아가고 있던 타이머가 종료되도록 한 리시버
    }
}
