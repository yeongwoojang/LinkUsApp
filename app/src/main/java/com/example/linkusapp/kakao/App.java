package com.example.linkusapp.kakao;

import android.app.Application;
import android.content.Context;

import com.example.linkusapp.R;
import com.google.firebase.FirebaseApp;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.sdk.common.KakaoSdk;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        KakaoSdk.init(this ,this.getResources().getString(R.string.kakao_app_key));
    }
}