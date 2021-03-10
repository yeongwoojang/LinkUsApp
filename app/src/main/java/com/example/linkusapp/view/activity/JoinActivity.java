package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityJoinBinding;
import com.example.linkusapp.util.GMailSender;
import com.example.linkusapp.viewModel.JoinViewModel;
import com.google.android.material.snackbar.Snackbar;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding binding;
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
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork()
                .build()
        );
        viewModel = new ViewModelProvider(this).get(JoinViewModel.class);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.joinIdCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = binding.joinId.getText().toString().trim();
                if (!userId.equals("") && userId.matches("^[a-zA-Z0-9]+$") && userId.length() >= 6) {
                    viewModel.idChk(userId);
                } else {
                    binding.joinId.setText(" ");
                    Snackbar.make(binding.joinLayout, "올바른 아이디를 입력해주세요", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        binding.joinCertificationCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(binding.joinCertification.getWindowToken(),0);
                String code = binding.joinCertification.getText().toString();
                if (code.equals(emailCode) && !code.equals("")&&!isCertify){
                    isCertify = true;
                    viewModel.countDownTimer.cancel();
                    binding.timeText.setText("");
                    Snackbar.make(binding.joinLayout, "이메일인증이 완료되었습니다.", Snackbar.LENGTH_SHORT).show();
                }else if(isCertify){
                    Snackbar.make(binding.joinLayout, "이미 인증완료 했습니다.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(binding.joinLayout, "인증번호가 틀렸습니다.", Snackbar.LENGTH_SHORT).show();
                    binding.joinCertification.setText("");
                }

            }
        });

        viewModel.idChkResLD.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(binding.joinLayout, "사용가능한 아이디입니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(binding.joinLayout, "이미 사용중인 아이디입니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        GMailSender gMailSender = new GMailSender("sbtmxhs@gmail.com", "jang7856");


        binding.joinCertificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSendMail){
                    emailCode = gMailSender.getEmailCode();
                    Log.d("isSendMail", "onClick: "+binding.joinEmail.getText().toString());
                    viewModel.sendMail(gMailSender,binding.joinEmail.getText().toString(),emailCode);
                }else{
                    Snackbar.make(binding.joinLayout, "이미 인증코드를 전송했습니다.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        viewModel.sendMailRes.observe(this,sendMailCode -> {
            if(sendMailCode==1000){
                Snackbar.make(binding.joinLayout, "인증코드를 전송했습니다.", Snackbar.LENGTH_SHORT).show();
                isSendMail = true;
                viewModel.countDown();
            }else if(sendMailCode==1001){
                Snackbar.make(binding.joinLayout, "이메일 형식이 잘못되었습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(binding.joinLayout, "인터넷 연결을 확인해주십시오", Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.count.observe(this,count ->{
            if(!count.equals("")){
                binding.timeText.setText(count);
            }else{
                isCertify = false;
                emailCode = "";
            }
        });


        binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = binding.joinId.getText().toString().trim();
                String userPw = binding.joinPassword.getText().toString().trim();
                String userPw2 = binding.joinPasswordCheck.getText().toString().trim();
                String userName = binding.joinName.getText().toString().trim();
                String userEmail = binding.joinEmail.getText().toString().trim();
                String certification = binding.joinCertification.getText().toString().trim();

                if (viewModel.isInputAll(userId,userPw,userPw2,userName,userEmail,certification)) {
                    Snackbar.make(binding.joinLayout, "정보를 정확히 입력하세요.", Snackbar.LENGTH_SHORT).show();
                } else if (userId.length() < 6) {
                    Snackbar.make(binding.joinLayout, "아이디는 6자리 이상입니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userId.matches("^[a-zA-Z0-9]+$")) {
                    Snackbar.make(binding.joinLayout, "아이디는 영문과 숫자만 가능합니다.", Snackbar.LENGTH_SHORT).show();
                } else if (userPw.length() <8) {
                    Snackbar.make(binding.joinLayout, "비밀번호는 8자리 이상입니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userPw.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")) {
                    Snackbar.make(binding.joinLayout, "비밀번호는 영문,특수문자,숫자 조합입니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userPw.equals(userPw2)) {
                    Snackbar.make(binding.joinLayout, "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                } else if (!userName.matches("^[가-힣]+$")) {
                    Snackbar.make(binding.joinLayout, "부정확한 이름입니다.", Snackbar.LENGTH_SHORT).show();
                }else if(!isCertify){
                    Snackbar.make(binding.joinLayout, "이메일 인증을 해주세요.", Snackbar.LENGTH_SHORT).show();
                }else {
                    viewModel.join(userName, userId, userPw,userEmail);
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });

        viewModel.joinRsLD.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(binding.joinLayout, "회원가입 성공", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            } else {
                Snackbar.make(binding.joinLayout, "회원가입 실패", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}