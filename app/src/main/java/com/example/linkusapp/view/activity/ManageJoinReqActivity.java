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

    private TextView groupName, emptyText;
    private ImageButton backBt;
    private RecyclerView myGroupRecyclerView, requestRecyclerView;
    private MyGroupAdapter myGroupAdapter;
    private RequestAdapter requestAdapter;
    private ManageJoinViewModel viewModel;
    private SlidingUpPanelLayout slidingView;
    private List<LeaderGroup> leaderGroups = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_join_req);

        groupName = (TextView)findViewById(R.id.group_name_txt);
        emptyText = (TextView)findViewById(R.id.empty_txt);
        backBt = (ImageButton)findViewById(R.id.btn_back);
        myGroupRecyclerView = (RecyclerView) findViewById(R.id.my_group_recyclerview);
        requestRecyclerView = (RecyclerView) findViewById(R.id.request_recyclerview);
        slidingView = (SlidingUpPanelLayout) findViewById(R.id.sub_sliding_view);

        viewModel = new ViewModelProvider(this).get(ManageJoinViewModel.class);

        //myGroup 리사이클러뷰 어댑터 생성
        myGroupAdapter = new MyGroupAdapter(leaderGroups, slidingView,viewModel);
        myGroupRecyclerView.setAdapter(myGroupAdapter);
        myGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        //requet 리사이클러뷰 어댑터 생성
        requestAdapter = new RequestAdapter(users,viewModel,this);
        requestRecyclerView.setAdapter(requestAdapter);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        User user = viewModel.getUserInfoFromShared();
        viewModel.getLeaderGroup(user.getUserNickname());

        viewModel.leaderGroupRes.observe(this, leaderGroupInfo -> {
            if (leaderGroupInfo.getCode() == 200) {
                myGroupAdapter.updateItem(leaderGroupInfo.getLeaderGroupList());
            }else if(leaderGroupInfo.getCode()==204){
                myGroupAdapter.updateItem(new ArrayList<LeaderGroup>());
                emptyText.setVisibility(View.VISIBLE);
            }
        });

        viewModel.reqUserListRes.observe(this, UsersInfo -> {
            Log.d("WhatCode", "onCreate: "+UsersInfo.getCode());
            if (UsersInfo.getCode() == 200) {
                requestAdapter.updateItem(UsersInfo.getUsers());
                slidingView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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
                Snackbar.make(findViewById(R.id.sub_sliding_view), "가입되었습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.sub_sliding_view), "가입이 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.deleteRes.observe(this, response ->{
            if(response.equals("200")){
                viewModel.getReqUser(requestAdapter.getgName());
                viewModel.getLeaderGroup(user.getUserNickname());
            }
        });

        //슬라이딩 드로어 바깥은 클릭하면 드로어를 닫게 해주는 리스너 추가
        slidingView.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingView.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //슬라이딩 드로어 상태변화 이벤트리스너 추가
        slidingView.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                requestRecyclerView.scrollToPosition(0);
            }
        });

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}