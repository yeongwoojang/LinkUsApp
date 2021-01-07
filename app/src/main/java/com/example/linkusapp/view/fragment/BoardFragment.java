package com.example.linkusapp.view.fragment;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.PartAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class BoardFragment extends Fragment{

    ImageButton createBtn;
    private RecyclerView partRecyclerView;
    private RecyclerView boardRecyclerView;

    private BoardViewModel viewModel;

    private List<Board> boardList = new ArrayList<>();

    // 뷰 만드는 곳
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board, container, false);

        partRecyclerView = (RecyclerView)view.findViewById(R.id.part_recyclerview);
        boardRecyclerView = (RecyclerView)view.findViewById(R.id.board_recyclerview);
        createBtn = (ImageButton)view.findViewById(R.id.write_btn);

        return view;
    }


    // 뷰 다 만들어졌을 때 해야할 일들
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> list = new ArrayList<>();
        String[] part = {"전체", "어학", "교양", "프로그래밍", "취업","취미", "자율", "기타"};
        for (int i=0; i<part.length; i++) {
            list.add(part[i]);
        }
        partRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        PartAdapter adapter = new PartAdapter(list) ;
        partRecyclerView.setAdapter(adapter);

        // 분야 아이템 클릭시 event
        adapter.setOnItemClickListener(new PartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(position == 0){
                    Toast.makeText(getApplicationContext(),"전체",Toast.LENGTH_SHORT).show();
                }else if(position == 1){
                    Toast.makeText(getApplicationContext(),"어학",Toast.LENGTH_SHORT).show();
                }else if(position == 2){
                    Toast.makeText(getApplicationContext(),"교양",Toast.LENGTH_SHORT).show();
                }else if(position == 3){
                    Toast.makeText(getApplicationContext(),"프로그래밍",Toast.LENGTH_SHORT).show();
                }else if(position == 4){
                    Toast.makeText(getApplicationContext(),"취업",Toast.LENGTH_SHORT).show();
                }else if(position == 5){
                    Toast.makeText(getApplicationContext(),"취미",Toast.LENGTH_SHORT).show();
                }else if(position == 6) {
                    Toast.makeText(getApplicationContext(), "자율", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"기타",Toast.LENGTH_SHORT).show();
                }
            }
        });

        boardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        viewModel.getAllBoard();

        viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
            if(boardInfo.getCode()==200){
                boardList = boardInfo.getJsonArray();
                BoardAdapter boardAdapter = new BoardAdapter(boardList);
                boardRecyclerView.setAdapter(boardAdapter);
                Snackbar.make(view, "스터디 그룹 LOAD", Snackbar.LENGTH_SHORT).show();
            }else if(boardInfo.getCode()==204){
                Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(view, "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}