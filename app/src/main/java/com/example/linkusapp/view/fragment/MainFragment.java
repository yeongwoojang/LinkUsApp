package com.example.linkusapp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.MyGroupAdapter;
import com.example.linkusapp.view.custom.RoundImageView;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.example.linkusapp.viewModel.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RoundImageView logoImage;
    private Button selectStudy;
    private TextView defaultText, nothingText;
    private RecyclerView selectRecyclerview;
    private BoardAdapter adapter;
    private BoardViewModel viewModel;
    private List<Board> boardList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        selectStudy = (Button)view.findViewById(R.id.select_study);
        defaultText = (TextView)view.findViewById(R.id.default_text);
        nothingText = (TextView)view.findViewById(R.id.noting_text);
        selectRecyclerview = (RecyclerView)view.findViewById(R.id.select_recyclerview);
        selectRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        String userNickName = viewModel.getUserInfoFromShared().getUserNickname();
        adapter = new BoardAdapter(boardList,getActivity(),viewModel,2);
        selectRecyclerview.setAdapter(adapter);


        selectStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.userBoardAll(userNickName);
            }
        });

        viewModel.userGroupRsLD.observe(getViewLifecycleOwner(),boardInfo -> {
            if(boardInfo.getCode()==200){
                adapter.updateItem(boardInfo.getJsonArray());
                defaultText.setVisibility(View.INVISIBLE);
                nothingText.setVisibility(View.INVISIBLE);
                selectRecyclerview.setVisibility(View.VISIBLE);
            }else if(boardInfo.getCode()==204){
                defaultText.setVisibility(View.INVISIBLE);
                nothingText.setVisibility(View.VISIBLE);
            }
        });
        viewModel.updateSelectedLD.observe(getViewLifecycleOwner(),response -> {
            if(response.equals("200")){
            selectRecyclerview.setVisibility(View.INVISIBLE);
            }
        });

    }

}
