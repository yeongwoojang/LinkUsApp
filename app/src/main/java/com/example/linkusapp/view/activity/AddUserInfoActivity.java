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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityAddUserInfoBinding;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.sdk.user.UserApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class AddUserInfoActivity extends AppCompatActivity {

    private ActivityAddUserInfoBinding binding;

    private String age;
    private String gender = "M";
    private String userNickname;
    private String currentId;
    private String loginMethod;

    private GoogleSignInClient mSignInClient;
    private LoginViewModel viewModel;
    private static final  int SEARCH_ADDRESS_ACTIVITY = 10000;

    /*닉네임중복확인 유무*/
    boolean isCertify = false;
    private static final String TAG = AddUserInfoActivity.class.getSimpleName();

    /*프로필 */
    private static final int REQUEST_IMAGE_CODE = 1001;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        /*퍼미션 체크*/
        chkPermission();


        binding.saveBtn.setPaintFlags(binding.saveBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        currentId = viewModel.getLoginSession();

        //어떤 방시으로 로그인 된 계정인지 체크
        loginMethod = viewModel.getLoginMethod();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mSignInClient =  GoogleSignIn.getClient(this, gso);

       /* binding.profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_IMAGE_CODE);*//*

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_IMAGE_CODE);
            }
        });*/
        /*나이*/
        binding.spinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                age = parent.getItemAtPosition(position).toString();
                Snackbar.make(binding.addUserInfo, age, Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Snackbar.make(binding.addUserInfo, "나이를 선택해주세요.", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*닉네임 체크*/
        binding.nicknameCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNickname = binding.setNickname.getText().toString().trim();
                if(userNickname.equals("")||!userNickname.matches("^[a-zA-Z0-9가-힣]+$")) {
                    Snackbar.make(binding.addUserInfo,"조건에 맞는 닉네임을 입력해주세요.",Snackbar.LENGTH_SHORT).show();
                    isCertify = false;
                }else if(userNickname.length() >= 10){
                    Snackbar.make(binding.addUserInfo,"닉네임 길이 10자 이하로 작성해주세요.",Snackbar.LENGTH_SHORT).show();
                    isCertify = false;
                }else{
                    viewModel.nickNameChk(userNickname);
                    isCertify = true;
                }
            }
        });
        viewModel.nickChkResLD.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(binding.addUserInfo, "사용가능한 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(binding.addUserInfo, "이미 사용중인 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*내지역 설정*/
        binding.searchAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
            }
        });
        /*내 지역 설정*/
        binding.searchAddressEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
            }
        });
        /*2차 회원 정보 저장버튼*/
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = binding.searchAddressEt.getText().toString();
                if(!isCertify){
                    Snackbar.make(binding.addUserInfo, "닉네임 중복검사 실시해주세요.", Snackbar.LENGTH_SHORT).show();
                }
                else if(age.equals("선택")){
                    Snackbar.make(binding.addUserInfo, "나이를 선택해주세요.", Snackbar.LENGTH_SHORT).show();
                }
                else if (binding.searchAddressEt.getText().toString().trim().equals("")){
                    Snackbar.make(binding.addUserInfo, "주소 검색 실시해주세요.", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    viewModel.saveInfo(currentId,userNickname,age,gender,address,loginMethod);
                    Snackbar.make(binding.addUserInfo, "회원님의 정보가 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
        viewModel.addUserInfoResLD.observe(this, code -> {
            if(code.equals("200")){
                Snackbar.make(binding.addUserInfo, "정보 저장 성공", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
            else{
                Snackbar.make(binding.addUserInfo, "정보저장 실패", Snackbar.LENGTH_SHORT).show();
            }
        });
        binding.joinRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_man) {
                    gender = "M";
                } else {
                    gender = "W";
                }
            }
        });
        /*이전 버튼*/
        binding.previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (loginMethod){
                    case  "일반" :{
                        //일반 로그아웃
                        viewModel.cancelAutoLogin();
                        break;
                    }
                    case "Google" :{
                        //구글 로그아웃
                        googleSignOut();
                        break;
                    }
                    case "Facebook" :{
                        //facebook 로그아웃
                        LoginManager.getInstance().logOut();
                        break;
                    }
                    case "Kakao" :{
                        UserApiClient.getInstance().logout(error ->{
                            if(error !=null){
                                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error);
                            }else{
                                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨");
                            }
                            return null;
                        });
                        break;
                    }
                }
                viewModel.removeUserIdPref();
//                viewModel.removeLoginMethod();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        binding.searchAddressEt.setText(data);
                    }
                }
                break;
            /*case REQUEST_IMAGE_CODE:
                if(resultCode==RESULT_OK){
                    try {
                        InputStream in = getContentResolver().openInputStream(intent.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        img=rotateBitmap(img,2);
                        in.close();
                        img = Bitmap.createScaledBitmap(img, 300, 300, true);
                        binding.profileIv.setImageBitmap(img);
                        String postImage = bitMapToString(img);
                        viewModel.insertProfile(currentId,loginMethod,img);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
        }
        viewModel.insertProfileLiveData.observe(this,code -> {
            if(code.equals("200")){
                Snackbar.make(findViewById(R.id.add_user_info), "아이콘 변경", Snackbar.LENGTH_SHORT).show();
            }else {
                Snackbar.make(findViewById(R.id.add_user_info), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
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
    /*image를 Blob타입으로 변환하는 메소드*/
    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte []arr = baos.toByteArray();
        String image = Base64.encodeToString(arr,Base64.DEFAULT);
        String temp = "";
        try {
            temp = "&imagedevice="+ URLEncoder.encode(image,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return temp;
    }
    /*비트맵 회전*/
    public Bitmap rotateBitmap(Bitmap bitmap, int rotate){
        Matrix rotateMatrix = new Matrix();
        if(rotate == 0 )
            rotateMatrix.postRotate(0);
        else if(rotate == 1)
            rotateMatrix.postRotate(45);
        else if(rotate == 2)
            rotateMatrix.postRotate(90);
        else if(rotate == 3)
            rotateMatrix.postRotate(135);
        else if(rotate == 4)
            rotateMatrix.postRotate(180);
        else if(rotate == 5)
            rotateMatrix.postRotate(225);
        else if(rotate == 6)
            rotateMatrix.postRotate(270);
        else
            rotateMatrix.postRotate(315);
        Bitmap sideInversionImg = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);

        return sideInversionImg;
    }
    //로그아웃하기
    private void googleSignOut() {
        mSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

}