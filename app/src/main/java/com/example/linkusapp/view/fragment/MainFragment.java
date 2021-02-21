package com.example.linkusapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.activity.TimerDialog;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.MyGroupAdapter;
import com.example.linkusapp.view.custom.RoundImageView;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.example.linkusapp.viewModel.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private Button selectStudy;
    private TextView defaultText, nothingText, selectedGname;
    private LinearLayout timerBt;
    private RecyclerView selectRecyclerview;
    private CardView selectedContainer;
    private BoardAdapter adapter;
    private BoardViewModel viewModel;
    private List<Board> boardList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        selectStudy = (Button) view.findViewById(R.id.select_study);
        defaultText = (TextView) view.findViewById(R.id.default_text);
        nothingText = (TextView) view.findViewById(R.id.noting_text);
        selectedGname = (TextView) view.findViewById(R.id.selected_gname);
        timerBt = (LinearLayout)view.findViewById(R.id.btn_timer);
        selectRecyclerview = (RecyclerView) view.findViewById(R.id.select_recyclerview);
        selectedContainer = (CardView) view.findViewById(R.id.selected_container);
        selectRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        String userNickName = viewModel.getUserInfoFromShared().getUserNickname();
        adapter = new BoardAdapter(boardList, getActivity(), viewModel, 2);
        selectRecyclerview.setAdapter(adapter);


        viewModel.getSelectedGroup(userNickName);

        viewModel.selectedGroupLD.observe(getViewLifecycleOwner(), boardInfo -> {
            if (boardInfo.getCode() == 200) {
                Log.d("LIST", boardInfo.getJsonArray().toString());
                selectedContainer.setVisibility(View.VISIBLE);
                defaultText.setVisibility(View.INVISIBLE);
                nothingText.setVisibility(View.INVISIBLE);
                selectRecyclerview.setVisibility(View.INVISIBLE);
                selectedGname.setText(boardInfo.getJsonArray().get(0).getgName());
            } else if (boardInfo.getCode() == 204) {
                selectedContainer.setVisibility(View.INVISIBLE);
                defaultText.setVisibility(View.VISIBLE);
            }
        });
        selectStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.userBoardAll(userNickName);
            }
        });

        viewModel.userGroupRsLD.observe(getViewLifecycleOwner(), boardInfo -> {
            if (boardInfo.getCode() == 200) {
                adapter.updateItem(boardInfo.getJsonArray());
                selectedContainer.setVisibility(View.INVISIBLE);
                defaultText.setVisibility(View.INVISIBLE);
                nothingText.setVisibility(View.INVISIBLE);
                selectRecyclerview.setVisibility(View.VISIBLE);
            } else if (boardInfo.getCode() == 204) {
                defaultText.setVisibility(View.INVISIBLE);
                nothingText.setVisibility(View.VISIBLE);
            }
        });
        viewModel.updateSelectedLD.observe(getViewLifecycleOwner(), response -> {
            if (response.equals("200")) {
                viewModel.getSelectedGroup(userNickName);


            }
        });

        timerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimerDialog.class);
                startActivity(intent);
            }
        });

    }

}
