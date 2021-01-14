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
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.viewModel.CreateGrpViewModel;
import com.google.firebase.FirebaseApp;

public class GroupMainActivity extends AppCompatActivity {

    private ImageButton backBt;
    private TextView groupNameTxt, memberCntTxt,readerNameTxt,joinMethodTxt,period,groupExplTxt,groupPurposeTxt;
    private FrameLayout reqJoinLayout;
    Button inviteBt;

    CreateGrpViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        //리사이클러뷰에서 선택한 항목의 정보를 받는부분
        Intent intent = getIntent();
        Board board = (Board)intent.getSerializableExtra("board");
        Log.d("board", "onCreate: "+board);
        groupNameTxt = (TextView)findViewById(R.id.txt_group_name);
        memberCntTxt = (TextView)findViewById(R.id.member_count);
        readerNameTxt = (TextView)findViewById(R.id.reader_name_txt);
        joinMethodTxt = (TextView)findViewById(R.id.join_method);
        period = (TextView)findViewById(R.id.period);
        groupExplTxt = (TextView)findViewById(R.id.txt_group_expl);
        groupPurposeTxt = (TextView)findViewById(R.id.txt_group_purpose);
        backBt = (ImageButton)findViewById(R.id.back_btn);
        inviteBt = (Button)findViewById(R.id.invite_btn);
        reqJoinLayout = (FrameLayout)findViewById(R.id.req_join_layout);

        groupNameTxt.setText(board.getgName());
        memberCntTxt.setText("멤버 "+board.getgMemberCnt());
        readerNameTxt.setText("리더 :"+board.getgReader());
        joinMethodTxt.setText("가입방식 :"+board.getgJoinMethod());
        period.setText("기간 : "+board.getgStartDate()+" ~ "+board.getgEndDate());
        groupExplTxt.setText(board.getgExplanation());
        groupPurposeTxt.setText(board.getgPurpose());

        if(board.getgJoinMethod().equals("자유형")){
            reqJoinLayout.setVisibility(View.INVISIBLE);
        }

        viewModel = new ViewModelProvider(this).get(CreateGrpViewModel.class);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inviteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("firebase", "onClick: ");
                String user = viewModel.getUserInfoFromShared().getUserNickname();
                viewModel.requestJoin(board.getgReader(),user);
            }
        });
    }
}