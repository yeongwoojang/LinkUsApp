package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MyStudyGroupActivity extends AppCompatActivity {

    private RecyclerView groupRecyclerView;
    private BoardViewModel viewModel;
    private String nickname;
    private ImageButton back;
    private TextView emptyStudyGroup;
    private List<Board> boardList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_study_group);

        Intent intent = getIntent();
        nickname = intent.getExtras().get("nickname").toString();

        groupRecyclerView = (RecyclerView) findViewById(R.id.my_study_group_rv);
        back = (ImageButton) findViewById(R.id.back_btn);
        emptyStudyGroup = (TextView) findViewById(R.id.empty_study_group);
        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
        BoardAdapter userboardAdapter = new BoardAdapter(boardList,this,viewModel,1);
        groupRecyclerView.setAdapter(userboardAdapter);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        viewModel.userBoardAll(nickname);
        viewModel.userGroupRsLD.observe(this,boardInfo -> {
            if(boardInfo.getCode()==200){
                //boardInfo를 읽어오면 BoardRecyclerview의 내용을 업데이트.
                groupRecyclerView.setVisibility(View.VISIBLE);
                emptyStudyGroup.setVisibility(View.GONE);
                userboardAdapter.updateItem(boardInfo.getJsonArray());
            }else if(boardInfo.getCode()==204){
                groupRecyclerView.setVisibility(View.GONE);
                emptyStudyGroup.setVisibility(View.VISIBLE);
                Snackbar.make(findViewById(R.id.my_study_group), "내 스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.my_study_group), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}