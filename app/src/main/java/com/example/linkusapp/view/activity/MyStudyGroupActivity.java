package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityMyStudyGroupBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MyStudyGroupActivity extends AppCompatActivity {

    private ActivityMyStudyGroupBinding binding;
    private BoardViewModel viewModel;
    private String nickname;
    private List<Board> boardList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyStudyGroupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });

        BoardAdapter userboardAdapter = new BoardAdapter(boardList,this,viewModel,1);
        binding.myStudyGroupRv.setAdapter(userboardAdapter);
        binding.myStudyGroupRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        viewModel.userBoardAll(nickname);
        viewModel.userGroupRsLD.observe(this,boardInfo -> {
            if(boardInfo.getCode()==200){
                //boardInfo를 읽어오면 BoardRecyclerview의 내용을 업데이트.
                binding.myStudyGroupRv.setVisibility(View.VISIBLE);
                binding.emptyStudyGroup.setVisibility(View.GONE);
                Log.d("LiveDataaa", "onCreate: "+boardInfo.getJsonArray());
                userboardAdapter.updateItem(boardInfo.getJsonArray());
            }else if(boardInfo.getCode()==204){
                binding.myStudyGroupRv.setVisibility(View.GONE);
                binding.emptyStudyGroup.setVisibility(View.VISIBLE);
                Snackbar.make(binding.myStudyGroup, "내 스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(binding.myStudyGroup, "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}