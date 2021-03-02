package com.example.linkusapp.view.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.Timer;
import com.example.linkusapp.view.activity.TimerDialog;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.adapter.MyGroupAdapter;
import com.example.linkusapp.view.custom.RoundImageView;
import com.example.linkusapp.view.decorator.DayDecorator;
import com.example.linkusapp.view.decorator.SaturDayDecorator;
import com.example.linkusapp.view.decorator.SunDayDecorator;
import com.example.linkusapp.view.decorator.TodayDecorator;
import com.example.linkusapp.view.dialog.PopupDialog;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment  implements OnDateSelectedListener {

    private Button selectStudy;
    private TextView defaultText, nothingText, selectedGname, groupMemberCount;
    private LinearLayout timerBt;
    private RecyclerView selectRecyclerview;
    MaterialCalendarView materialCalendarView;
    private CardView selectedContainer;
    private BoardAdapter adapter;
    private BoardViewModel viewModel;
    private List<Board> boardList = new ArrayList<>();
    private List<Timer> timeList = new ArrayList<>();
    private DayDecorator dayDecorator;
    //캘린더관련 변수
    String selectedDate = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        selectStudy = (Button) view.findViewById(R.id.select_study);
        defaultText = (TextView) view.findViewById(R.id.default_text);
        nothingText = (TextView) view.findViewById(R.id.noting_text);
        materialCalendarView = view.findViewById(R.id.calendar);
        selectedGname = (TextView) view.findViewById(R.id.selected_gname);
        groupMemberCount = (TextView) view.findViewById(R.id.member_count);
        timerBt = (LinearLayout) view.findViewById(R.id.btn_timer);
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
        viewModel.getEntireRecord(userNickName);
        adapter = new BoardAdapter(boardList, getActivity(), viewModel, 2);
        selectRecyclerview.setAdapter(adapter);

        Log.d("로그인한유저 : ", userNickName);
        viewModel.getSelectedGroup(userNickName);

        viewModel.selectedGroupLD.observe(getViewLifecycleOwner(), boardInfo -> {
            if (boardInfo.getCode() == 200) {
                Log.d("LIST", boardInfo.getJsonArray().toString());
                selectedContainer.setVisibility(View.VISIBLE);
                defaultText.setVisibility(View.INVISIBLE);
                nothingText.setVisibility(View.INVISIBLE);
                selectRecyclerview.setVisibility(View.INVISIBLE);
                viewModel.getGroupMember(boardInfo.getJsonArray().get(0).getgName());
                selectedGname.setText(boardInfo.getJsonArray().get(0).getgName());
            } else if (boardInfo.getCode() == 204) {
                selectedContainer.setVisibility(View.INVISIBLE);
                defaultText.setVisibility(View.VISIBLE);
            }
        });

        viewModel.groupMembersLD.observe(getViewLifecycleOwner(), usersInfo -> {
            if (usersInfo.getUsers() != null) {
                groupMemberCount.setText("멤버수 : " + usersInfo.getUsers().size() + "명");
            }else{
                groupMemberCount.setText("멤버수 : " + 0+ "명");
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

        viewModel.entireRecordLD.observe(getViewLifecycleOwner(),timerInfo -> {
            if(timerInfo.getCode()==200){
                timeList = timerInfo.getTimer();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                for(int i=0;i<timeList.size();i++){

                    try {
                        dayDecorator  = new DayDecorator(format.parse(timeList.get(i).getStudyDate()),getActivity());
                        materialCalendarView.addDecorator(dayDecorator);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }else{

            }
        });

        timerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimerDialog.class);
                intent.putExtra("userNick",userNickName);
                startActivity(intent);
            }
        });

        //캘린더 관련 기능
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setTopbarVisible(true);
        materialCalendarView.addDecorators(new TodayDecorator(getActivity()), new SunDayDecorator(getActivity()), new SaturDayDecorator(getActivity()));


    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = format.format(date.getDate());
        Intent intent = new Intent(getActivity(), PopupDialog.class);
        intent.putExtra("selectedDate",selectedDate);
        startActivity(intent);
    }
}
