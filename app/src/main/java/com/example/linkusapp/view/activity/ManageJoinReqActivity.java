package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityManageJoinReqBinding;
import com.example.linkusapp.model.vo.LeaderGroup;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UsersInfo;
import com.example.linkusapp.view.adapter.MyGroupAdapter;
import com.example.linkusapp.view.adapter.RequestAdapter;
import com.example.linkusapp.viewModel.ManageJoinViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class ManageJoinReqActivity extends AppCompatActivity {

    private ActivityManageJoinReqBinding binding;
    private MyGroupAdapter myGroupAdapter;
    private RequestAdapter requestAdapter;
    private ManageJoinViewModel viewModel;
    private List<LeaderGroup> leaderGroups = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageJoinReqBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(ManageJoinViewModel.class);

        //myGroup 리사이클러뷰 어댑터 생성
        myGroupAdapter = new MyGroupAdapter(leaderGroups,binding.subSlidingView,viewModel);
        binding.myGroupRecyclerview.setAdapter(myGroupAdapter);
        binding.myGroupRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        //requet 리사이클러뷰 어댑터 생성
        requestAdapter = new RequestAdapter(users,viewModel,this);
        binding.requestRecyclerview.setAdapter(requestAdapter);
        binding.requestRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        User user = viewModel.getUserInfoFromShared();
        viewModel.getLeaderGroup(user.getUserNickname());

        viewModel.leaderGroupRes.observe(this, leaderGroupInfo -> {
            if (leaderGroupInfo.getCode() == 200) {
                myGroupAdapter.updateItem(leaderGroupInfo.getLeaderGroupList());
            }else if(leaderGroupInfo.getCode()==204){
                myGroupAdapter.updateItem(new ArrayList<LeaderGroup>());
                binding.emptyTxt.setVisibility(View.VISIBLE);
            }
        });

        viewModel.reqUserListRes.observe(this, UsersInfo -> {
            Log.d("WhatCode", "onCreate: "+UsersInfo.getCode());
            if (UsersInfo.getCode() == 200) {
                requestAdapter.updateItem(UsersInfo.getUsers());
                binding.subSlidingView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }else if(UsersInfo.getCode() == 204){
                requestAdapter.updateItem(new ArrayList<User>());
            }
        });
        myGroupAdapter.setGnameListener(new MyGroupAdapter.GroupNameListener() {
            @Override
            public void returnGname(String gName) {
                requestAdapter.setgName(gName);
//                groupName.setText("가입 요청 유저목록");
//                viewModel.joinGroup(gName,user.getUserId(),user.getUserNickname());
            }
        });

        viewModel.joinGroupRes.observe(this, response -> {
            if(response.equals("200")){
                Snackbar.make(binding.subSlidingView, "가입되었습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(binding.subSlidingView, "가입이 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.deleteRes.observe(this, response ->{
            if(response.equals("200")){
                viewModel.getReqUser(requestAdapter.getgName());
                viewModel.getLeaderGroup(user.getUserNickname());
            }
        });

        //슬라이딩 드로어 바깥은 클릭하면 드로어를 닫게 해주는 리스너 추가
        binding.subSlidingView.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.subSlidingView.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //슬라이딩 드로어 상태변화 이벤트리스너 추가
        binding.subSlidingView.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                binding.requestRecyclerview.scrollToPosition(0);
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}