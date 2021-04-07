package com.example.linkusapp.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityUpdateUserBinding;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class UpdateUserActivity extends AppCompatActivity {

    private ActivityUpdateUserBinding binding;
    private String checkNickname,checkPW;
    private LoginViewModel viewModel;
    private String pwd;

    /*엘범에서 사진 가져오기*/
    private static final int REQUEST_IMAGE_CODE = 1001;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;

    boolean isCertify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        chkPermission();
        /*유저 정보*/
        checkNickname =viewModel.getUserInfoFromShared().getUserNickname();
        checkPW = viewModel.getUserInfoFromShared().getPassword();
        binding.idTv.setText(viewModel.getUserInfoFromShared().getUserId());
        binding.updateMethodTv.setText(viewModel.getUserInfoFromShared().getLoginMethod());
        binding.nicknameEt.setText(viewModel.getUserInfoFromShared().getUserNickname());

        if(!binding.updateMethodTv.getText().equals("일반")){
            binding.originPasswordEt.setHint("소셜로그인은 변경할 수 없습니다.");
            binding.passwordEt.setHint("소셜로그인은 변경할 수 없습니다.");
            binding.passwordConfirmEt.setHint("소셜로그인은 변경할 수 없습니다.");
            binding.passwordEt.setEnabled(false);
            binding.passwordConfirmEt.setEnabled(false);
            binding.originPasswordEt.setEnabled(false);
        }
        binding.ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,REQUEST_IMAGE_CODE);
        }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
        binding.nicknameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNickname = binding.nicknameEt.getText().toString();
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

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = binding.nicknameEt.getText().toString();
                String password = binding.originPasswordEt.getText().toString();
                String newPwd = binding.passwordEt.getText().toString();
                String loginmethod= viewModel.getLoginMethod();
                /*닉네임 중복 검사*/
                if(nick.equals(checkNickname)){
                    isCertify = true;
                }
                if(!isCertify){
                    Snackbar.make(findViewById(R.id.update_user_layout), "닉네임 중복 검사 실시 해주세요.", Snackbar.LENGTH_SHORT).show();
                }/*비밀번호 조건 검사*/
                else if(binding.updateMethodTv.equals("일반")&&!newPwd.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")){
                    Snackbar.make(findViewById(R.id.update_user_layout), "비밀번호는 영문,특수문자,숫자 조합입니다.", Snackbar.LENGTH_SHORT).show();
                }else if(newPwd.equals(password)){/*현재비밀번호와 변경할 비밀번호가 다른지 검사*/
                    Snackbar.make(findViewById(R.id.update_user_layout), "변경하려는 비밀번호가 기존가 동일합니다.", Snackbar.LENGTH_SHORT).show();
                }/*비밀번호 확인과 동일 검사*/
                else if(newPwd.equals(binding.passwordConfirmEt.getText())){
                    Snackbar.make(findViewById(R.id.update_user_layout), "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                }
                else if(BCrypt.checkpw(password,checkPW)){
                    if(isCertify&&binding.updateMethodTv.getText().equals("일반")){
                        pwd = BCrypt.hashpw(newPwd,BCrypt.gensalt());
                        viewModel.updateUserInfo(nick,pwd,loginmethod);
                        Snackbar.make(findViewById(R.id.update_user_layout), "회원정보가 수정되었습니다.", Snackbar.LENGTH_SHORT).show();
                    }else if(isCertify&&!binding.updateMethodTv.getText().equals("일반")){
                        viewModel.updateUserInfo(nick,checkPW,loginmethod);
                        Snackbar.make(findViewById(R.id.update_user_layout), "회원정보가 수정되었습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                }
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
        viewModel.insertProfileLiveData.observe(this,code -> {
            if(code.equals("200")){
                Snackbar.make(findViewById(R.id.update_user_layout), "프로필 사진 저장 완료", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.update_user_layout), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
        viewModel.getProfileLiveData.observe(this,profile -> {
            if(profile.getCode().equals("200")){
                Snackbar.make(findViewById(R.id.update_user_layout), "프로필을 불러왔습니다.", Snackbar.LENGTH_SHORT).show();
                Uri uri = Uri.parse(profile.getProfileUri());
                Log.d("gd", "onCreate: "+uri);
            }else if(profile.getCode().equals("204")){
                Snackbar.make(findViewById(R.id.update_user_layout), "불러올 프로필이 없습니다.", Snackbar.LENGTH_SHORT).show();
            }else {
                Snackbar.make(findViewById(R.id.update_user_layout), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE){
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(binding.ivUser);
            viewModel.insertProfile(checkNickname,uri);
        }
    }
    /*접근 허용 체크*/
    private void chkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        }
    }
    /*private Bitmap resize(Bitmap src){
        Configuration config = getResources().getConfiguration();
        if(config.smallestScreenWidthDp>=800)
            src = Bitmap.createScaledBitmap(src, 400, 400, true);
        else if(config.smallestScreenWidthDp>=600)
            src = Bitmap.createScaledBitmap(src, 300, 300, true);
        else if(config.smallestScreenWidthDp>=400)
            src = Bitmap.createScaledBitmap(src, 200, 200, true);
        else if(config.smallestScreenWidthDp>=360)
            src = Bitmap.createScaledBitmap(src, 180, 180, true);
        else
            src = Bitmap.createScaledBitmap(src, 160, 160, true);
        return src;
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}