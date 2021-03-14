package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityGroupMainBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.viewModel.CreateGrpViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

public class GroupMainActivity extends AppCompatActivity {

    private ActivityGroupMainBinding binding;
    //viewModel
    CreateGrpViewModel viewModel;

    //현재 그룹에 참여한 맴버 수
    int memberCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //리사이클러뷰에서 선택한 항목의 정보를 받는부분
        Intent intent = getIntent();
        Board board = (Board) intent.getSerializableExtra("board");

        binding.txtGroupName.setText(board.getTitle());
        binding.memberCount.setText("인원제한 " + board.getMemberLimit() + "명");
        binding.readerNameTxt.setText("리더 :" + board.getLeader());
        binding.joinMethod.setText("가입방식 :" + board.getGroupJoinMethod());
        if(board.getStartDate().equals("미정") && board.getEndDate().equals("미정")){
            binding.period.setText("기간 : " + "미정");
        }else{
            binding.period.setText("기간 : " + board.getStartDate() + " ~ " + board.getEndDate());
        }
        binding.txtGroupExpl.setText(board.getExplanation());
        binding.txtGroupPurpose.setText(board.getPurpose());

        viewModel = new ViewModelProvider(this).get(CreateGrpViewModel.class);

        User user = viewModel.getUserInfoFromShared();
        if (board.getGroupJoinMethod().equals("자유")) {
            binding.reqJoinLayout.setVisibility(View.INVISIBLE);
        } else {
            binding.reqJoinLayout.setVisibility(View.VISIBLE);
            binding.joinGroupBt.setVisibility(View.GONE);
        }

        if( board.getLeader().equals(user.getUserNickname())){
            binding.requestJoinBtn.setVisibility(View.INVISIBLE);
            binding.joinGroupBt.setVisibility(View.INVISIBLE);
        }

        viewModel.getGroupMember(board.getTitle());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.joinGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //그룹 가입 방식이 자유형일 경우
                if (board.getGroupJoinMethod().equals("자유")) {
                    if (memberCount < Integer.parseInt(board.getMemberLimit())) {
                        User user = viewModel.getUserInfoFromShared();
                        String userId = user.getUserId();
                        String userNick = user.getUserNickname();
                        viewModel.joinGroup(board.getTitle(), userId, userNick);
                    } else {
                        Snackbar.make(binding.groupMainLayout, "인원이 꽉 찼습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                    //그룹 가입 방식이 승이형일 경우
                } else {
                    Snackbar.make(binding.groupMainLayout, "리더의 승인을 받아야 가입할 수 있습니다.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        //그룹의 현재 멤버 수를 가져온다.
        viewModel.memberCount.observe(this, usersInfo -> {
            if(usersInfo.getCode()==200){
               this.memberCount = usersInfo.getUsers().size();
            }
        });

        viewModel.joinGroupRes.observe(this, response -> {
            if (response.equals("200")) {
                Snackbar.make(binding.groupMainLayout, "그룹에 가입되었습니다.", Snackbar.LENGTH_SHORT).show();
            } else if (response.equals("204")) {
                Snackbar.make(binding.groupMainLayout, "이미 가입한 그룹입니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(binding.groupMainLayout, "요청이 실패했습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.requestJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberCount < Integer.parseInt(board.getMemberLimit())) {



                    viewModel.insertRequest(board.getTitle(), user.getUserNickname());
                } else {
                    Snackbar.make(binding.groupMainLayout, "인원이 꽉 찼습니다.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        viewModel.insertReqRes.observe(this, response -> {
            if (response.equals("200")) {
                Snackbar.make(findViewById(R.id.group_main_layout), "가입 요청 성공.", Snackbar.LENGTH_SHORT).show();
                viewModel.requestJoin(board.getLeader(), user.getUserNickname(), user.getAge(), user.getGender(), user.getAddress());
            } else if (response.equals("204")) {
                Snackbar.make(findViewById(R.id.group_main_layout), "이미 요청했습니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.group_main_layout), "가입 요청에 실패했습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}