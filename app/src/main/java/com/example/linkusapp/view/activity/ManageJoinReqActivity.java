package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.LeaderGroup;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UsersInfo;
import com.example.linkusapp.view.adapter.MyGroupAdapter;
import com.example.linkusapp.view.adapter.RequestAdapter;
import com.example.linkusapp.viewModel.ManageJoinViewModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class ManageJoinReqActivity extends AppCompatActivity {

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

        myGroupRecyclerView = (RecyclerView) findViewById(R.id.my_group_recyclerview);
        requestRecyclerView = (RecyclerView) findViewById(R.id.request_recyclerview);
        slidingView = (SlidingUpPanelLayout) findViewById(R.id.sub_sliding_view);

        viewModel = new ViewModelProvider(this).get(ManageJoinViewModel.class);


        //myGroup 리사이클러뷰 어댑터 생성
        myGroupAdapter = new MyGroupAdapter(leaderGroups, slidingView,viewModel);
        myGroupRecyclerView.setAdapter(myGroupAdapter);
        myGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        //requet 리사이클러뷰 어댑터 생성
        requestAdapter = new RequestAdapter(users);
        requestRecyclerView.setAdapter(requestAdapter);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


        User user = viewModel.getUserInfoFromShared();
        viewModel.getLeaderGroup(user.getUserNickname());
        viewModel.leaderGroupRes.observe(this, leaderGroupInfo -> {
            if (leaderGroupInfo.getCode() == 200) {
                myGroupAdapter.updateItem(leaderGroupInfo.getLeaderGroupList());
            }
        });

        viewModel.reqUserListRes.observe(this, UsersInfo -> {
            if (UsersInfo.getCode() == 200) {
                requestAdapter.updateItem(UsersInfo.getUsers());
                slidingView.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });


    }
}