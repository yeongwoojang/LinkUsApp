package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.LeaderGroup;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.view.adapter.MyGroupAdapter;
import com.example.linkusapp.viewModel.ManageJoinViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManageJoinReqActivity extends AppCompatActivity {

    private RecyclerView myGroupRecyclerView;
    private MyGroupAdapter adapter;
    private ManageJoinViewModel viewModel;

    private List<LeaderGroup> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_join_req);
        myGroupRecyclerView = (RecyclerView) findViewById(R.id.my_group_recyclerview);
        adapter = new MyGroupAdapter(items);
        myGroupRecyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(ManageJoinViewModel.class);

        myGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        adapter = new MyGroupAdapter(items);
        User user = viewModel.getUserInfoFromShared();
        viewModel.getLeaderGroup(user.getUserNickname());
        viewModel.leaderGroupRes.observe(this, leaderGroupInfo -> {
            if (leaderGroupInfo.getCode() == 200) {
                adapter.updateItem(leaderGroupInfo.getLeaderGroupList());
            }
        });


    }
}