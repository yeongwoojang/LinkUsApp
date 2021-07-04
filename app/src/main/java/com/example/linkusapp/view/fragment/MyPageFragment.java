package com.example.linkusapp.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.linkusapp.R;
import com.example.linkusapp.databinding.FragmentMyPageBinding;
import com.example.linkusapp.util.TimerService;
import com.example.linkusapp.view.activity.HomeActivity;
import com.example.linkusapp.view.activity.ManageJoinReqActivity;
import com.example.linkusapp.view.activity.MyStudyGroupActivity;
import com.example.linkusapp.view.activity.SetAddressActivity;
import com.example.linkusapp.view.activity.UpdateUserActivity;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.sdk.user.UserApiClient;

import java.io.IOException;

public class MyPageFragment extends Fragment {

    private FragmentMyPageBinding binding;
    private LoginViewModel viewModel;
    private String loginMethod,userNickname;
    private GoogleSignInClient mSignInClient;
    private String userId;
    private Bitmap profile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        Log.d("MyPageFragment", "onResume: ");
        binding.addressTv.setText(viewModel.getUserInfoFromShared().getAddress());
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getProfile(userNickname);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mSignInClient =  GoogleSignIn.getClient(getActivity(), gso);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*유저정보*/
        userId = viewModel.getUserInfoFromShared().getUserId();
        loginMethod = viewModel.getUserInfoFromShared().getLoginMethod();
        userNickname = viewModel.getUserInfoFromShared().getUserNickname();
        binding.nicknameTv.setText(userNickname);
        binding.addressTv.setText(viewModel.getUserInfoFromShared().getAddress());
        binding.methodTv.setText(loginMethod);
        /*회원정보 수정*/
        binding.updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateUserActivity.class));
            }
        });
        /*내지역설정*/
        binding.myAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SetAddressActivity.class);
                intent.putExtra("nickname",userNickname);
                startActivity(intent);
            }
        });
        /*내 스터디 그룹*/
        binding.myStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyStudyGroupActivity.class);
                intent.putExtra("nickname",userNickname);
                startActivity(intent);
            }
        });
        binding.signAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageJoinReqActivity.class);
                startActivity(intent);
            }
        });
        /*앱 공유하기 버튼*/
        binding.shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.example.linkusapp");
                msg.putExtra(Intent.EXTRA_TITLE, "앱 공유하기");
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "앱을 선택해 주세요"));
            }
        });
        Log.d("test", "onViewCreated: "+userNickname);
//        viewModel.getProfile(userNickname);
//        viewModel.getProfileLiveData.observe(getActivity(),profile1 -> {
//            if(profile1.getCode().equals("200")){
//                Snackbar.make(view.findViewById(R.id.my), "프로필 사진 불러왔습니다.", Snackbar.LENGTH_SHORT).show();
//                Log.d("TAG", "onViewCreated: "+profile1.getProfileUri());
//                if(profile1.getProfileUri().equals(null)){
//                    Drawable drawable = getResources().getDrawable(R.drawable.baseline_profile_picture);
//                    binding.profilePicture.setImageDrawable(drawable);
//                }else{
//                    Bitmap bitmap = profile1.getProfileUri();
//                    binding.profilePicture.setImageBitmap(bitmap);
//                    /*try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),image);
//                        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//                        binding.profilePicture.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }*/
//                }
//            }else if(profile1.getCode().equals("204")){
//                Snackbar.make(view.findViewById(R.id.my), "프로필 사진이 없습니다.", Snackbar.LENGTH_SHORT).show();
//            }else{
//                Snackbar.make(view.findViewById(R.id.my), "오류", Snackbar.LENGTH_SHORT).show();
//            }
//        });
        /*탈퇴하기 버튼*/
        binding.withdrawApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.withDraw(userId,loginMethod);
            }
        });
        viewModel.withDrawREDLD.observe(getActivity(),code -> {
            if (code.equals("200")) {
                Snackbar.make(view.findViewById(R.id.my), "탈퇴 성공", Snackbar.LENGTH_SHORT).show();
                logout();
            }else {
                Snackbar.make(view.findViewById(R.id.my), "탈퇴 실패", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*로그아웃 버튼*/
        binding.mypageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().stopService(new Intent(getActivity(), TimerService.class));
                logout();
                getActivity().finish();
            }
        });
    }

    private void logout(){
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
                        Log.e("kakao", "로그아웃 실패. SDK에서 토큰 삭제됨", error);
                    }else{
                        Log.i("kakao", "로그아웃 성공. SDK에서 토큰 삭제됨");
                    }
                    return null;
                });
                break;
            }
        }
        viewModel.removeUserIdPref();
        viewModel.removeUserInfo();
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
    //로그아웃하기
    private void googleSignOut() {
        mSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

}