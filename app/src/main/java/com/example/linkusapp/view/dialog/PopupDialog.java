package com.example.linkusapp.view.dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Timer;
import com.example.linkusapp.viewModel.TimerViewModel;

public class PopupDialog extends Activity {

    private TextView date,studyTime;
    private Timer studyTimeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_dialog);
        date = (TextView)findViewById(R.id.txt_date);
        studyTime = (TextView)findViewById(R.id.txt_study_time);
        Intent intent = getIntent();
        studyTimeInfo = (Timer) intent.getSerializableExtra("studyTime");
        date.setText(studyTimeInfo.getStudyDate());
        studyTime.setText("총 공부시간 : "+ studyTimeInfo.getStudyTime());


    }
}