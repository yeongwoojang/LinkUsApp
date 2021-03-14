package com.example.linkusapp.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpdateUserActivity extends AppCompatActivity {

    private ActivityUpdateUserBinding binding;
    private String checkNickname,checkPW,loginMethod;
    private LoginViewModel viewModel;

    /*엘범에서 사진 가져오기*/
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;
    private Boolean isPermission = true;

    boolean isCertify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        tedPermission();
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
        binding.setProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isPermission)  takePhoto();
                        else Snackbar.make(findViewById(R.id.update_user_layout),getResources().getString(R.string.permission_2),Snackbar.LENGTH_SHORT).show();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isPermission) getPhoto();
                        else Snackbar.make(findViewById(R.id.update_user_layout),getResources().getString(R.string.permission_2),Snackbar.LENGTH_SHORT).show();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(UpdateUserActivity.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
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
        super.onActivityResult(requestCode, resultCode, data);
        /*예외처리 : 사진 선택하는 앨범화면에서 선택없이 뒤로 갔을 경우*/
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("tempFIle", tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Log.d("photoUri", "PICK_FROM_ALBUM photoUri : " + photoUri);
            Cursor cursor = null;
            try {
                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
                Log.d("uri", "tempFile Uri : " + Uri.fromFile(tempFile));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            setImage();
        }
    }
    /*권한 요청*/
    private void tedPermission(){
        PermissionListener permissionListener = new PermissionListener() {
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
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            Uri photoUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }
    private void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }
    private void setImage(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(),options);
        binding.setProfilePicture.setImageBitmap(originalBm);
        tempFile = null;
    }
    /*사진 촬영 후 폴더 만들기*/
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";
        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();
        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d("파일 생성", "createImageFile : " + image.getAbsolutePath());
        return image;
    }
}