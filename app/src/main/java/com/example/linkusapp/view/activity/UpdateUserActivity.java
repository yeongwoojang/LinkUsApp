package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class UpdateUserActivity extends AppCompatActivity {
    private TextView id,loginMethod;
    private EditText nickname, password,password2;
    private Button save;
    private ImageButton exit;
    private LoginViewModel viewModel;
    private String checkNickname,checkPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        id = (TextView) findViewById(R.id.id_tv);
        loginMethod = (TextView) findViewById(R.id.update_method_tv);
        nickname = (EditText) findViewById(R.id.nickname_et);
        password = (EditText) findViewById(R.id.password_et);
        password2 = (EditText) findViewById(R.id.password2_et);
        save = (Button)findViewById(R.id.save_btn);
        exit = (ImageButton) findViewById(R.id.back_btn);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getUserInfo();
        viewModel.getUserInfoRsLD.observe(this,userInfo -> {
            id.setText(userInfo.getUser().getUserId());
            loginMethod.setText(userInfo.getUser().getLoginMethod());
            nickname.setText(checkNickname);
            password.setText(checkPW);
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*닉네임만 변경*/
                if(password2.getText().equals("")){

                }
                /*비밀번호 or 둘다 변경*/
                else if(password.getText().equals(password2.getText())){

                }
                Snackbar.make(findViewById(R.id.update_user_layout), "변경 사항 없습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}