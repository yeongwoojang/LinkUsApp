package com.example.linkusapp.view.dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityPopupDialogBinding;
import com.example.linkusapp.model.vo.Timer;
import com.example.linkusapp.viewModel.TimerViewModel;

public class PopupDialog extends Activity {

    private ActivityPopupDialogBinding binding;
    private Timer studyTimeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopupDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        studyTimeInfo = (Timer) intent.getSerializableExtra("studyTime");
        binding.txtDate.setText(studyTimeInfo.getStudyDate());
        binding.txtStudyTime.setText("총 공부시간 : "+ studyTimeInfo.getStudyTime());
    }
}