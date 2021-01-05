package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.LoadingViewModel;

public class LoadingActivity extends AppCompatActivity {

    LoadingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        viewModel = new ViewModelProvider(this).get(LoadingViewModel.class);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.loading);
        findViewById(R.id.loading_text).setAnimation(animation);

        viewModel.getLoginSession();
        String loginMethod = viewModel.getLoginMethod();

        viewModel.userIdLiveData.observe(this, userId -> {
            Log.d("userId", "onCreate: " + userId);
            if (!userId.equals(" ")) {
                viewModel.chkScdUserInfo(userId, loginMethod);
            }else{
                viewModel.getLoginSession();
            }
        });

        viewModel.resultCode.observe(this, resultCode -> {
            if (resultCode.equals("200")) {
                startActivity(new Intent(getApplicationContext(), AddUserInfoActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();

            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });
    }
}