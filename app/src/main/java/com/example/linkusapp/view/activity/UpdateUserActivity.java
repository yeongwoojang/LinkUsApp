package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private Button save,nicknameChk;
    private ImageButton exit;
    private LoginViewModel viewModel;
    private String checkNickname,checkPW;

    boolean isCertify = false;

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
        nicknameChk = (Button) findViewById(R.id.nickname_check);
        exit = (ImageButton) findViewById(R.id.back_btn);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        viewModel.getUserInfo();
//        viewModel.getUserInfoRsLD.observe(this,userInfo -> {
//            checkNickname =userInfo.getUser().getUserNickname();
//            checkPW = userInfo.getUser().getPassword();
//            String Method = userInfo.getUser().getLoginMethod();
//            id.setText(userInfo.getUser().getUserId());
//            loginMethod.setText(userInfo.getUser().getLoginMethod());
//            nickname.setText(checkNickname);
//            if(loginMethod.equals("일반")){
//                password.setText(checkPW);
//            }else {
//                password.setText("소셜로그인은 변경할 수 없습니다.");
//                password2.setText("소셜로그인은 변경할 수 없습니다.");
//                password.setEnabled(false);
//                password2.setEnabled(false);
//            }
//        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
        nicknameChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNickname = nickname.getText().toString();
                if(userNickname.equals("")||!userNickname.matches("^[a-zA-Z0-9가-힣]+$")) {
                    Snackbar.make(findViewById(R.id.update_user_layout),"조건에 맞는 닉네임을 입력해주세요.",Snackbar.LENGTH_SHORT).show();
                    isCertify = false;
                }else if(userNickname.length() >= 10){
                    Snackbar.make(findViewById(R.id.update_user_layout),"닉네임 길이 10자 이하로 작성해주세요.",Snackbar.LENGTH_SHORT).show();
                    isCertify = false;
                }else{
                    viewModel.nickNameChk(userNickname);
                    isCertify = true;
                }
            }
        });
        viewModel.nickChkResLD.observe(this,code -> {
            if (code.equals("200")) {
                Snackbar.make(findViewById(R.id.update_user_layout), "사용가능한 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.update_user_layout), "이미 사용중인 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = nickname.getText().toString();
                String passwd = password.getText().toString();
                /*닉네임 중복 검사*/
                if(!isCertify){
                    Snackbar.make(findViewById(R.id.update_user_layout), "닉네임 중복 검사 실시 해주세요.", Snackbar.LENGTH_SHORT).show();
                }/*비밀번호 조건 검사*/
                else if(loginMethod.equals("일반")&&!passwd.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")){
                    Snackbar.make(findViewById(R.id.update_user_layout), "비밀번호는 영문,특수문자,숫자 조합입니다.", Snackbar.LENGTH_SHORT).show();
                }/*비밀번호 확인과 동일 검사*/
                else if(passwd.equals(password2.getText())){
                    Snackbar.make(findViewById(R.id.update_user_layout), "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                }
                else if(loginMethod.equals("일반")){
                    viewModel.updateUserInfo(nick,passwd);
                }
                viewModel.updateUserInfo(nick,checkPW);
            }
        });
        viewModel.updateUserInfoRsLD.observe(this,code ->{
            if(code.equals("200")){
                Log.d("updateUSerInfo", "회원 정보 수정");
                Snackbar.make(findViewById(R.id.update_user_layout), "회원정보 수정 완료.", Snackbar.LENGTH_SHORT).show();
            }else if(code.equals("404")){
                Log.d("updateUSerInfo", "회원 정보 수정 오류 ");
                Snackbar.make(findViewById(R.id.update_user_layout), "회원 정보 수정 오류", Snackbar.LENGTH_SHORT).show();
            }
        } );
    }
}