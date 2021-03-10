package com.example.linkusapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.FragmentBoardBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.activity.CreateGroupActivity;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.PartAdapter;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BoardFragment extends Fragment{


    private BoardViewModel viewModel;
    private FragmentBoardBinding binding;

    private List<Board> boardList = new ArrayList<>();
    private String gPart ="전체";
    // 뷰 만드는 곳
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    // 뷰 다 만들어졌을 때 해야할 일들
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        ArrayList<String> partList = new ArrayList<>();
        String[] part = {"전체", "어학", "교양", "프로그래밍", "취업","취미", "자율", "기타"};
        for (int i=0; i<part.length; i++) { partList.add(part[i]); }

        binding.partRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        PartAdapter partAdapter = new PartAdapter(partList) ;
        binding.partRecyclerview.setAdapter(partAdapter);


        binding.boardRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        BoardAdapter boardAdapter = new BoardAdapter(boardList,getActivity(),viewModel,1);
        binding.boardRecyclerview.setAdapter(boardAdapter);

        viewModel.getAllBoard();
        viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
            if(boardInfo.getCode()==200){
                binding.emptyGroup.setVisibility(View.GONE);
                boardAdapter.updateItem(boardInfo.getJsonArray());
            }else if(boardInfo.getCode()==204){
                binding.emptyGroup.setVisibility(View.VISIBLE);
                Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(view, "오류", Snackbar.LENGTH_SHORT).show();
            }
        });


        // 버튼클릭 새로고침
        binding.refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewModel.getRefreshBoard();
                binding.boardRecyclerview.smoothScrollToPosition(0);
                viewModel.boardRefreshRsLD.observe(getViewLifecycleOwner(), boardInfo -> {
                    if(boardInfo.getCode()==200){
                        boardAdapter.updateItem(boardInfo.getJsonArray());
                    }else if(boardInfo.getCode()==204){
                        Snackbar.make(view, "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(view, "오류", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                viewModel.getSearchBoard(binding.searchBar.getText().toString(), binding.searchBar.getText().toString());
                viewModel.boardSearchRsLD.observe(getViewLifecycleOwner(), boardInfo -> {
                    if(boardInfo.getCode()==200){
                        boardList = boardInfo.getJsonArray();
                        boardAdapter.updateItem(boardList);
                    }else if(boardInfo.getCode()==204){
                        Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.writeBtn.setOnClickListener(new View.OnClickListener() {
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
                            binding.emptyGroup.setVisibility(View.GONE);
                            binding.spinnerAddress.setSelection(0);
                            gPart = part[position];
                            boardList = boardInfo.getJsonArray();
                            boardAdapter.updateItem(boardList);
                        }else if(boardInfo.getCode()==204){
                            Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                            binding.emptyGroup.setVisibility(View.VISIBLE);
                        }else{
                            Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    viewModel.getPartBoard(part[position]);
                    viewModel.boardPartRsLD.observe(getViewLifecycleOwner(), boardPartInfo -> {
                        if(boardPartInfo.getCode() == 200){
                            binding.spinnerAddress.setSelection(0);
                            gPart = part[position];
                            boardList = boardPartInfo.getJsonArray();
                            boardAdapter.updateItem(boardList);
                            binding.boardRecyclerview.setVisibility(View.VISIBLE);
                            binding.emptyGroup.setVisibility(View.GONE);
                        }
                        else if(boardPartInfo.getCode()==204){
                            binding.boardRecyclerview.setVisibility(View.GONE);
                            binding.emptyGroup.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        /*여기*/
        binding.spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("전체")){
                    viewModel.getAllBoard();
                }else{
                    Log.d("onItemSelected: ", parent.getItemAtPosition(position).toString());
                    viewModel.optionBoard(gPart,parent.getItemAtPosition(position).toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewModel.boardRsLD.observe(getViewLifecycleOwner(), boardInfo ->{
            if(boardInfo.getCode()==200){
                binding.emptyGroup.setVisibility(View.GONE);
                boardList = boardInfo.getJsonArray();
                Log.d("onViewCreated: ",boardList.get(0).toString());
                boardAdapter.updateItem(boardList);
                binding.boardRecyclerview.setVisibility(View.VISIBLE);
                binding.emptyGroup.setVisibility(View.GONE);
            }else if(boardInfo.getCode()==204){
                Snackbar.make(view.findViewById(R.id.board_fragment), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
                binding.boardRecyclerview.setVisibility(View.GONE);
                binding.emptyGroup.setVisibility(View.VISIBLE);
            }else{
                Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
        viewModel.optionBoardRsLD.observe(getViewLifecycleOwner(),boardInfo -> {
            if(boardInfo.getCode() == 200){
                boardList = boardInfo.getJsonArray();
                boardAdapter.updateItem(boardList);
                binding.boardRecyclerview.setVisibility(View.VISIBLE);
                binding.emptyGroup.setVisibility(View.GONE);
            }else if(boardInfo.getCode()==204){
                binding.boardRecyclerview.setVisibility(View.GONE);
                binding.emptyGroup.setVisibility(View.VISIBLE);
            }else{
                Snackbar.make(view.findViewById(R.id.board_fragment), "오류", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void listRefresh(){
//        boardRecyclerView.removeAllViewsInLayout();
//        BoardAdapter boardAdapter = new BoardAdapter(boardList,getActivity());
//        boardRecyclerView.setAdapter(boardAdapter);
        BoardAdapter boardAdapter = new BoardAdapter(boardList,getActivity(),viewModel, 1);
        boardAdapter.notifyDataSetChanged();
    }
}