package com.example.linkusapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.activity.CreateGroupActivity;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.PartAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class BoardFragment extends Fragment{

    private ImageButton createBtn;
    private RecyclerView partRecyclerView;
    private RecyclerView boardRecyclerView;
    private EditText searchEdit;
    private ImageButton searchBtn;

    private BoardViewModel viewModel;
    private Spinner spinner;
    private TextView emptyView;

    private List<Board> boardList = new ArrayList<>();
    private String gpart;
    // 뷰 만드는 곳
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board, container, false);

        partRecyclerView = (RecyclerView)view.findViewById(R.id.part_recyclerview);
        boardRecyclerView = (RecyclerView)view.findViewById(R.id.board_recyclerview);
        createBtn = (ImageButton)view.findViewById(R.id.write_btn);
        spinner = (Spinner)view.findViewById(R.id.spinner_address);
        searchEdit = (EditText)view.findViewById(R.id.search_bar);
        searchBtn = (ImageButton)view.findViewById(R.id.search_btn);
        createBtn = (ImageButton)view.findViewById(R.id.write_btn);
        emptyView = (TextView)view.findViewById(R.id.empty_group);
        return view;
    }


    // 뷰 다 만들어졌을 때 해야할 일들
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        ArrayList<String> partList = new ArrayList<>();
        String[] part = {"전체", "어학", "교양", "프로그래밍", "취업","취미", "자율", "기타"};
        for (int i=0; i<part.length; i++) { partList.add(part[i]); }

        partRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        PartAdapter partAdapter = new PartAdapter(partList) ;
        partRecyclerView.setAdapter(partAdapter);


        boardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        BoardAdapter boardAdapter = new BoardAdapter(boardList,getActivity());
        boardRecyclerView.setAdapter(boardAdapter);

        viewModel.getAllBoard();

        viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
            if(boardInfo.getCode()==200){
                boardAdapter.updateItem(boardInfo.getJsonArray());
            }else if(boardInfo.getCode()==204){
                Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(view, "오류", Snackbar.LENGTH_SHORT).show();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewModel.getSearchBoard(searchEdit.getText().toString());
                viewModel.boardSearchRsLD.observe(getViewLifecycleOwner(), boardSearchInfo -> {
                    if(boardSearchInfo.getCode()==200){
                        boardList = boardSearchInfo.getJsonArray();
                        boardAdapter.updateItem(boardList);
                    }else if(boardSearchInfo.getCode()==204){
                        Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateGroupActivity.class));
            }
        });


        partAdapter.setOnItemClickListener(new PartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(position == 0){
                    viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
                        if(boardInfo.getCode()==200){
                            spinner.setSelection(0);
                            gpart = part[position];
                            boardList = boardInfo.getJsonArray();
                            boardAdapter.updateItem(boardList);
                        }else if(boardInfo.getCode()==204){
                            Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    viewModel.getPartBoard(part[position]);
                    viewModel.boardPartRsLD.observe(getViewLifecycleOwner(), boardPartInfo -> {
                        if(boardPartInfo.getCode() == 200){
                            spinner.setSelection(0);
                            gpart = part[position];
                            boardList = boardPartInfo.getJsonArray();
                            boardAdapter.updateItem(boardList);
                            boardRecyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                        else if(boardPartInfo.getCode()==204){
                            boardRecyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        /*여기*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("전체")){
                    viewModel.getAllBoard();
                }else{
                    Log.d("onItemSelected: ", parent.getItemAtPosition(position).toString());
                    viewModel.optionBoard(gpart,parent.getItemAtPosition(position).toString());
                    Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position) + "지역을 불러왔습니다.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
            if(boardInfo.getCode()==200){
                boardList = boardInfo.getJsonArray();
                Log.d("onViewCreated: ",boardList.get(0).toString());
                boardAdapter.updateItem(boardList);
                boardRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }else if(boardInfo.getCode()==204){
                Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                boardRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }else{
                Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
        viewModel.optionBoardRsLD.observe(getViewLifecycleOwner(),boardInfo -> {
            if(boardInfo.getCode() == 200){
                boardList = boardInfo.getJsonArray();
                boardAdapter.updateItem(boardList);
                boardRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }else if(boardInfo.getCode()==204){
                Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                boardRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }else{
                Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}