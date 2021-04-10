package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityEnterMainGroupBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.Comment;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.view.adapter.CommentAdapter;
import com.example.linkusapp.view.adapter.MemberAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.example.linkusapp.viewModel.CommentViewModel;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EnterMainGroupActivity extends AppCompatActivity {

    private ActivityEnterMainGroupBinding binding;

    private CommentViewModel viewModel;
    private LoginViewModel loginViewModel;
    private BoardViewModel boardViewModel;

    private boolean isDrOpen = false; //드로어 오픈 여부
    /*댓글이 쓰이는 게시판 명*/
    private String gName;
    /*댓글 작성자*/
    private String writer;
    /*commentlist*/
    private List<Comment> commentList = new ArrayList<>();
    private List<User> memberList = new ArrayList<>(); // 그룹에 속한 멤버 리스트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterMainGroupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        boardViewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        Intent intent = getIntent();
        Board board = (Board)intent.getSerializableExtra("board");
        gName = board.getTitle();
        Log.d("IntentBoard", "onCreate: "+board.toString());
        viewModel.getGroupMember(gName);
        writer = viewModel.getUserInfoFromShared().getUserNickname();
        binding.groupNameTv.setText(gName);
        binding.leaderTv.setText("리더 : "+board.getLeader());
        binding.partTv.setText("분야 : "+board.getPart());
        binding.noticeEt.setText(board.getNotice());
        /*오류 해결*/
        if(board.getStartDate().equals("미정") && board.getEndDate().equals("미정")){
            binding.periodTv.setText("기간 : " + "미정");
        }else{
            binding.periodTv.setText("기간 : "+board.getStartDate()+" ~ "+board.getEndDate());
        }
        binding.groupGoalTv.setText("그룹 목표 : "+board.getPurpose());

        /*스터디 그룹 리더만 공지사항 수정 기능*/
        if(board.getLeader().equals(writer)){
            /*현 접속자가 스터디 그룹장일 경우*/
            binding.noticeEt.setEnabled(true);
        }else {
            binding.noticeEt.setEnabled(false);
        }
        binding.editNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(board.getLeader().equals(writer)){
                    String notice = binding.noticeEt.getText().toString();
                    if(notice.equals("그룹 공지사항")||notice.equals(board.getNotice())){
                        Snackbar.make(binding.enterMainGroupActivity,"공지사항이 수정한 후 눌러주세요.",Snackbar.LENGTH_SHORT).show();
                    }else{
                        viewModel.updateNotice(gName,notice);
                    }
                }
            }
        });
        viewModel.updateNoticeRsLD.observe(this, code -> {
            if(code.equals("200")){
                Snackbar.make(binding.enterMainGroupActivity,"공지사항이 수정 완료",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(binding.enterMainGroupActivity,"오류",Snackbar.LENGTH_SHORT).show();
            }
        });
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        binding.commentRv.setAdapter(commentAdapter);
        binding.commentRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        String myNickname = viewModel.getUserInfoFromShared().getUserNickname();
        MemberAdapter memberAdapter = new MemberAdapter(this,gName,memberList,myNickname);
        binding.memberRv.setAdapter(memberAdapter);
        binding.memberRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        viewModel.getComment(gName);
        viewModel.getCommentRsLD.observe(this,commentInfo -> {
            if(commentInfo.getCode()==200){
                commentList = commentInfo.getJsonArray();
                commentAdapter.updateItem(commentList);
            }else if(commentInfo.getCode()==204){

            }else {
                Snackbar.make(binding.enterMainGroupActivity,"오류",Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = binding.commentEdittext.getText().toString();
                if(comment.trim().equals(""))
                {
                    Snackbar.make(binding.enterMainGroupActivity,"댓글을 입력해 주세요.",Snackbar.LENGTH_SHORT).show();
                }
                viewModel.insertComment(gName,writer,comment);
            }
        });

        binding.memberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrOpen){
                    binding.drawerLayout.closeDrawer(Gravity.RIGHT);
                }else{
                    binding.drawerLayout.openDrawer(Gravity.RIGHT);
                }
                isDrOpen = !isDrOpen;
            }
        });
        viewModel.insertCommentRsLD.observe(this,code ->{
            if(code.equals("200")){
                Snackbar.make(binding.enterMainGroupActivity,"댓글 등록 완료",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(binding.enterMainGroupActivity,"댓글 등록 실패",Snackbar.LENGTH_SHORT).show();
            }
        } );

        viewModel.groupMembersLD.observe(this, usersInfo -> {
            memberList = usersInfo.getUsers();
            if(usersInfo.getCode()==200){
                for (int i = 0; i <memberList.size(); i++){
                    if(memberList.get(i).getUserNickname().equals(myNickname)){
                        memberList.remove(i);
                        break;
                    }
                }
                memberAdapter.updateItem(memberList);
            }
            memberAdapter.updateItem(usersInfo.getUsers());
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("check NickName","접속 아이디 : "+ loginViewModel.getUserInfoFromShared().getUserNickname());
                Log.d("check NickName","이 그룹 리더 아이디 : "+ board.getLeader());

                // 현재 그룹 리더와 접속한 닉네임 비교해서
                // 닉네임이 같으면 해당 그룹 삭제 권한을 갖는다.
                if(board.getLeader().equals(loginViewModel.getUserInfoFromShared().getUserNickname())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterMainGroupActivity.this)
                            .setTitle("그룹 삭제")
                            .setMessage("그룹을 삭제 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boardViewModel.deleteGroup(board.getTitle());
                                    Toast.makeText(getApplicationContext(),"그룹 삭제",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    Toast.makeText(getApplicationContext(),"삭제 권한 없음",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}