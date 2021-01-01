package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.util.GMailSender;
import com.example.linkusapp.viewModel.JoinViewModel;
import com.google.android.material.snackbar.Snackbar;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class JoinActivity extends AppCompatActivity {

    private EditText idEdt;
    private EditText pwEdt;
    private EditText pwEdt2;
    private EditText nameEdt;
    private EditText emailEdt;
    private EditText certificationEdt;
    private TextView timeText;

    private Button certificationBtn;
    private Button certificationCheckBtn;
    private Button joinBtn;
    private Button joinIdCheckBtn;

    private InputMethodManager imm;
    private JoinViewModel viewModel;
    //이메일 인증번호
    String emailCode = "";
    //이메일 인증번호확인 여부변수
    boolean isCertify = false;
    boolean isSendMail = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork()
                .build()
        );
        viewModel = new ViewModelProvider(this).get(JoinViewModel.class);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);



        idEdt = (EditText) findViewById(R.id.join_id);
        pwEdt = (EditText) findViewById(R.id.join_password);
        pwEdt2 = (EditText) findViewById(R.id.join_password_check);
        nameEdt = (EditText) findViewById(R.id.join_name);
        emailEdt = (EditText) findViewById(R.id.join_email);
        certificationEdt = (EditText) findViewById(R.id.join_certification);

        certificationBtn = (Button) findViewById(R.id.join_certification_btn);
        certificationCheckBtn = (Button) findViewById(R.id.join_certification_check_btn);
        joinIdCheckBtn = (Button) findViewById(R.id.join_id_check_btn);
        joinBtn = (Button) findViewById(R.id.join_btn);
        timeText = (TextView)findViewById(R.id.time_text);




        joinIdCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = idEdt.getText().toString().trim();
                if (!userId.equals("") && userId.matches("^[a-zA-Z0-9]+$") && userId.length() >= 6) {
                    viewModel.idChk(userId);
                } else {
                    idEdt.setText(" ");
                    Snackbar.make(findViewById(R.id.join_layout), "올바른 아이디를 입력해주세요", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        certificationCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(certificationEdt.getWindowToken(),0);
                String code = certificationEdt.getText().toString();
                if (code.equals(emailCode) && !code.equals("")&&!isCertify){
                    isCertify = true;
                    viewModel.countDownTimer.cancel();
                    timeText.setText("");
                    Snackbar.make(findViewById(R.id.join_layout), "이메일인증이 완료되었습니다.", Snackbar.LENGTH_SHORT).show();
                }else if(isCertify){
                    Snackbar.make(findViewById(R.id.join_layout), "이미 인증완료 했습니다.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(findViewById(R.id.join_layout), "인증번호가 틀렸습니다.", Snackbar.LENGTH_SHORT).show();
                    certificationEdt.setText("");
                }

            }
        });

        viewModel.idChkResLD.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(findViewById(R.id.join_layout), "사용가능한 아이디입니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.join_layout), "이미 사용중인 아이디입니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        GMailSender gMailSender = new GMailSender("sbtmxhs@gmail.com", "jang7856");


        certificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSendMail){
                    emailCode = gMailSender.getEmailCode();
                    viewModel.sendMail(gMailSender,emailEdt.getText().toString(),emailCode);
                }else{
                    Snackbar.make(findViewById(R.id.join_layout), "이미 인증코드를 전송했습니다.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        viewModel.sendMailRes.observe(this,sendMailCode -> {
            if(sendMailCode==1000){
                Snackbar.make(findViewById(R.id.join_layout), "인증코드를 전송했습니다.", Snackbar.LENGTH_SHORT).show();
                isSendMail = true;
                viewModel.countDown();
            }else if(sendMailCode==1001){
                Snackbar.make(findViewById(R.id.join_layout), "이메일 형식이 잘못되었습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.join_layout), "인터넷 연결을 확인해주십시오", Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.count.observe(this,count ->{
            if(!count.equals("")){
                timeText.setText(count);
            }else{
                isCertify = false;
                emailCode = "";
            }
        });


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = idEdt.getText().toString().trim();
                String userPw = pwEdt.getText().toString().trim();
                String userPw2 = pwEdt2.getText().toString().trim();
                String userName = nameEdt.getText().toString().trim();
                String userEmail = emailEdt.getText().toString().trim();
                String certification = certificationEdt.getText().toString().trim();

                if (viewModel.isInputAll(userId,userPw,userPw2,userName,userEmail,certification)) {
                    Snackbar.make(findViewById(R.id.join_layout), "정보를 정확히 입력하세요.", Snackbar.LENGTH_SHORT).show();
                } else if (userId.length() < 6) {
                    Snackbar.make(findViewById(R.id.join_layout), "아이디는 6자리 이상입니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userId.matches("^[a-zA-Z0-9]+$")) {
                    Snackbar.make(findViewById(R.id.join_layout), "아이디는 영문과 숫자만 가능합니다.", Snackbar.LENGTH_SHORT).show();
                } else if (userPw.length() <8) {
                    Snackbar.make(findViewById(R.id.join_layout), "비밀번호는 8자리 이상입니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userPw.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")) {
                    Snackbar.make(findViewById(R.id.join_layout), "비밀번호는 영문,특수문자,숫자 조합입니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userPw.equals(userPw2)) {
                    Snackbar.make(findViewById(R.id.join_layout), "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userName.matches("^[가-힣]+$")) {
                    Snackbar.make(findViewById(R.id.join_layout), "부정확한 이름입니다.", Snackbar.LENGTH_SHORT).show();
                }else if(!isCertify){
                    Snackbar.make(findViewById(R.id.join_layout), "이메일 인증을 해주세요.", Snackbar.LENGTH_SHORT).show();
                }else {
                    viewModel.join(userName, userId, userPw,userEmail);
                }
            }
        });

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });

        viewModel.joinRsLD.observe(this, code -> {
            if (code.equals("200")) {
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.join_layout), "회원가입 성공", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            } else {
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.join_layout), "회원가입 실패", Snackbar.LENGTH_SHORT).show();

            }
        });

    }
}