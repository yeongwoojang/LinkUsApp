package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.CommentDialog;
import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.viewModel.BoardViewModel;

public class EnterMainGroupActivity extends AppCompatActivity {

    private ImageButton backBtn,addCommentBtn,settingBtn;
    private TextView leaderTv,partTv,periodTv,noticeTv,groupNameTv;
    private RecyclerView commentRv;
    private BoardViewModel viewModel;
    private String gName;
    private String userNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_main_group);

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        addCommentBtn =(ImageButton) findViewById(R.id.add_comment);
        settingBtn = (ImageButton) findViewById(R.id.setting_btn);
        leaderTv = (TextView) findViewById(R.id.leader_tv);
        partTv = (TextView) findViewById(R.id.part_tv);
        periodTv = (TextView) findViewById(R.id.period_tv);
        noticeTv =(TextView) findViewById(R.id.notice_tv);
        groupNameTv = (TextView) findViewById(R.id.group_name_tv);
        commentRv = (RecyclerView) findViewById(R.id.comment_rv);

        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        Intent intent = getIntent();
        Board board = (Board)intent.getSerializableExtra("board");
        gName = board.getgName();
        groupNameTv.setText(gName);
        leaderTv.setText("리더 : "+board.getgReader());
        partTv.setText("분야 : "+board.getgPart());
        periodTv.setText("기간 : "+board.getgStartDate()+" ~ "+board.getgEndDate());
        noticeTv.setText(board.getgPurpose());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*댓글 남기기 버튼*/
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialog dlg = new CommentDialog(EnterMainGroupActivity.this);
                dlg.show();
            }
        });

    }
}