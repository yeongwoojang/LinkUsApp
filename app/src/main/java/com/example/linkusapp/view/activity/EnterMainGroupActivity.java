package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityEnterMainGroupBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.Comment;
import com.example.linkusapp.model.vo.CommentInfo;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.view.adapter.CommentAdapter;
import com.example.linkusapp.view.adapter.MemberAdapter;
import com.example.linkusapp.viewModel.CommentViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EnterMainGroupActivity extends AppCompatActivity {

    private ActivityEnterMainGroupBinding binding;

    private CommentViewModel viewModel;

    private boolean isDrOpen = false; //드로어 오픈 여부
    private String gName; // 댓글이 작성되는 게시판 이름
    private String writer; //댓글 작성자
    private String toWriter; //답글이 달릴 댓글 작성자
    private String toComment; //답글이 달릴 댓글
    private List<Comment> commentList = new ArrayList<>(); // 댓글 리스트
    private List<User> memberList = new ArrayList<>(); // 그룹에 속한 멤버 리스트
    List<Comment> replyList = new ArrayList<>(); //댓글의 답글 리스트
    List<List<Comment>> cmtRpyList = new ArrayList<>(); //댓글과 답글을 담고 있는 이차원 리스트
    private List<Comment> tmp;
    private boolean isReply = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnterMainGroupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        Intent intent = getIntent();
        Board board = (Board)intent.getSerializableExtra("board");
        gName = board.getTitle(); //그룹명 초기화

        viewModel.getGroupMember(gName); //그룹의 멤버 리스트를 가져온다.
        writer = viewModel.getUserInfoFromShared().getUserNickname(); //댓글 작성자 초기화

        binding.groupNameTv.setText(gName);
        binding.leaderTv.setText("리더 : "+board.getLeader());
        binding.partTv.setText("분야 : "+board.getPart());

        if(board.getStartDate().equals("미정") && board.getEndDate().equals("미정")){
            binding.periodTv.setText("기간 : " + "미정");
        }else{
            binding.periodTv.setText("기간 : "+board.getStartDate()+" ~ "+board.getEndDate());
        }
        binding.groupGoalTv.setText("그룹 목표 : "+board.getPurpose());

        //댓글 RecyclerView + adapter
        CommentAdapter commentAdapter = new CommentAdapter(commentList,viewModel,this,cmtRpyList);
        binding.commentRv.setAdapter(commentAdapter);
        binding.commentRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        String myNickname = viewModel.getUserInfoFromShared().getUserNickname();
        //채팅을 하기위한 member RecyclerView + adapter
        MemberAdapter memberAdapter = new MemberAdapter(this,gName,memberList,myNickname);
        binding.memberRv.setAdapter(memberAdapter);
        binding.memberRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

//        viewModel.getComment(gName); //작성되어있는 댓글 목록을 불러온다.


        viewModel.getCommentRsLD.observe(this,commentInfo -> {
                commentList = commentInfo.getJsonArray();
            Log.d("commentList", "onCreate: "+commentList);
//                for(Comment comment : commentList){
//                    comment.setWriteTime(comment.getWriteTime().substring(2, 10) + "  " + comment.getWriteTime().substring(11, 15));
//                }
                viewModel.getEntireReply(gName); //작성되어있는 댓글의 모든 답글 목록을 불러온다.
//                commentAdapter.updateItem(commentList);
                binding.commentRv.scrollToPosition(commentAdapter.getItemCount()-1);

        });

        viewModel.getEntireReplyRsLD.observe(this,result ->{
                replyList = result.getJsonArray();

                for(Comment cmt : commentList){
                    tmp = new ArrayList<>();
                    tmp.add(cmt);
                    for(Comment rpy : replyList){
                        if(cmt.getComment().equals(rpy.getComment()) && cmt.getWriter().equals(rpy.getWriter())){
                            tmp.add(rpy);
                            Log.d("rpy", tmp+"");
                        }
                    }
                        cmtRpyList.add(tmp);
                }
                commentAdapter.updateList(cmtRpyList);

        } );

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
                }else{
                    binding.commentEdittext.setText(" ");
                    if(isReply){
                        viewModel.insertReply(gName,toWriter,toComment,writer,comment);
                    }else{

                        viewModel.insertComment(gName,writer,comment);
                    }
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); //키보드 조작을 위한 객체
                    imm.hideSoftInputFromWindow(binding.commentEdittext.getWindowToken(),0);
                }
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
                isDrOpen  = !isDrOpen;
            }
        });
        viewModel.insertCommentRsLD.observe(this,code ->{
            if(code.equals("200")){
                viewModel.getComment(gName);
            }else{
                Snackbar.make(binding.enterMainGroupActivity,"댓글 등록 실패",Snackbar.LENGTH_SHORT).show();
            }
        } );

        viewModel.insertRpyRsLD.observe(this,code->{
            if(code.equals("200")){
                viewModel.getReply(gName,writer,toComment);
        }
        });

        viewModel.getReplyRsLD.observe(this,result->{

            replyList.addAll(result.getJsonArray());
//            commentAdapter.setReplyList(replyList);
            Log.d("REPLY", replyList.toString());
        });
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

        commentAdapter.setReplyBtnClickListener(new CommentAdapter.OnreplyBtnClickListener() {
            @Override
            public void onClick(String writer,String comment) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); //키보드 조작을 위한 객체
                binding.commentEdittext.requestFocus();
                imm.showSoftInput(binding.commentEdittext,InputMethodManager.SHOW_IMPLICIT);
                toWriter = writer;
                toComment = comment;
                isReply = true;

            }
        });

    }
}