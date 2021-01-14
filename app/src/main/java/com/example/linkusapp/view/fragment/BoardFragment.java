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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.view.activity.CreateGroupActivity;
import com.example.linkusapp.view.activity.LoadingActivity;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.PartAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

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
    private ViewSwitcher viewSwitcher;

    private BoardViewModel viewModel;
    private TextView emptyView;
    private Spinner spinner;

    private List<Board> boardList = new ArrayList<>();
//    private ArrayList<String> partList = new ArrayList<>();

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
        viewSwitcher = (ViewSwitcher)view.findViewById(R.id.switcher);
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
                        Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(view, "오류", Snackbar.LENGTH_SHORT).show();
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
                    Log.d(TAG, "onItemClick: " + part[0]);
                    viewModel.getAllBoard();
                    viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
                        if(boardInfo.getCode()==200){
                            viewSwitcher.showNext();
                            boardList = boardInfo.getJsonArray();
                            boardAdapter.updateItem(boardList);
                        }else if(boardInfo.getCode()==204){
                            Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                            viewSwitcher.showNext();
                        }else if(boardAdapter.getItemCount()==0){
                            Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            Snackbar.make(view, "오류", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    viewModel.getPartBoard(part[position]);
                    viewModel.boardPartRsLD.observe(getViewLifecycleOwner(), boardPartInfo -> {
                        if(boardPartInfo.getCode() == 200){
                            Log.d(TAG, "onItemClick: code == 200");
                            boardList = boardPartInfo.getJsonArray();
                            boardAdapter.updateItem(boardList);
                        }
                    });
                }
            }
        });
    }
}