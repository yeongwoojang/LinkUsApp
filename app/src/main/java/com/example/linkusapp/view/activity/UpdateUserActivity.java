package com.example.linkusapp.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityUpdateUserBinding;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;

public class UpdateUserActivity extends AppCompatActivity {

    private ActivityUpdateUserBinding binding;
    private String checkNickname,checkPW,loginMethod;
    private LoginViewModel viewModel;
    private Uri uri;
    private static final int GET_FROM_ALBUM = 1;

    boolean isCertify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        /*유저 정보*/
        checkNickname =viewModel.getUserInfoFromShared().getUserNickname();
        checkPW = viewModel.getUserInfoFromShared().getPassword();
        loginMethod = viewModel.getUserInfoFromShared().getLoginMethod();
        binding.idTv.setText(viewModel.getUserInfoFromShared().getUserId());
        binding.updateMethodTv.setText(viewModel.getUserInfoFromShared().getLoginMethod());
        binding.nicknameEt.setText(viewModel.getUserInfoFromShared().getUserNickname());
        if(binding.updateMethodTv.equals("일반")){
            binding.passwordEt.setText(checkPW);
        }else {
            binding.passwordEt.setText("소셜로그인은 변경할 수 없습니다.");
            binding.password2Et.setText("소셜로그인은 변경할 수 없습니다.");
            binding.passwordEt.setEnabled(false);
            binding.password2Et.setEnabled(false);
        }
        /*프로필 가져오기*/
        /*viewModel.getProfile(checkNickname);
        viewModel.getProfileLiveData.observe(this,profile -> {
            if(profile.getCode()==200){
            }
        });*/
        binding.setProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tedPermission();
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
                String passwd = binding.passwordEt.getText().toString();
                String loginmethod= viewModel.getLoginMethod();
                /*닉네임 중복 검사*/
                if(!isCertify){
                    Snackbar.make(findViewById(R.id.update_user_layout), "닉네임 중복 검사 실시 해주세요.", Snackbar.LENGTH_SHORT).show();
                }/*비밀번호 조건 검사*/
                else if(binding.updateMethodTv.equals("일반")&&!passwd.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")){
                    Snackbar.make(findViewById(R.id.update_user_layout), "비밀번호는 영문,특수문자,숫자 조합입니다.", Snackbar.LENGTH_SHORT).show();
                }/*비밀번호 확인과 동일 검사*/
                else if(passwd.equals(binding.password2Et.getText())){
                    Snackbar.make(findViewById(R.id.update_user_layout), "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                }
                else if(isCertify&&binding.updateMethodTv.equals("일반")){
                    viewModel.updateUserInfo(nick,passwd,loginmethod);
                    finish();
                }else if(isCertify&&!binding.updateMethodTv.equals("일반")){
                    viewModel.updateUserInfo(nick,checkPW,loginmethod);
                    finish();
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GET_FROM_ALBUM){
            uri = data.getData();
            Cursor cursor = null;
            try{
                String[] proj = {MediaStore.Images.Media._ID};
            }
        }
    }
    /*권한 요청*/
    private void tedPermission(){
        ermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
    private void takePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,GET_FROM_ALBUM);
    }
}