package com.example.linkusapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.FragmentMainBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.Timer;
import com.example.linkusapp.view.activity.TimerDialog;
import com.example.linkusapp.view.adapter.BoardAdapter;
import com.example.linkusapp.view.decorator.GoodDecorator;
import com.example.linkusapp.view.decorator.GreatDecorator;
import com.example.linkusapp.view.decorator.HighDecorator;
import com.example.linkusapp.view.decorator.MiddleDecorator;
import com.example.linkusapp.view.decorator.DefaultDecorator;
import com.example.linkusapp.view.decorator.RowDecorator;
import com.example.linkusapp.view.decorator.SaturDayDecorator;
import com.example.linkusapp.view.decorator.SunDayDecorator;
import com.example.linkusapp.view.decorator.TodayDecorator;
import com.example.linkusapp.view.dialog.PopupDialog;
import com.example.linkusapp.viewModel.BoardViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment  implements OnDateSelectedListener {

    private FragmentMainBinding binding;
    private BoardViewModel viewModel;
    private List<Timer> timeList = new ArrayList<>();
    private DayViewDecorator dayDecorator;
    String userNickName;
    //캘린더관련 변수
    String selectedDate = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d("LIFE", "onCreateView: ");
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("LIFE", "onViewCreated: ");
        viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        userNickName = viewModel.getUserInfoFromShared().getUserNickname();

        binding.imgWatch.setColorFilter(ContextCompat.getColor(getActivity(),R.color.red400));
        viewModel.entireRecordLD.observe(getViewLifecycleOwner(),timerInfo -> {
            if(timerInfo.getCode()==200){
                timeList = timerInfo.getTimer();
                SimpleDateFormat Dateformat = new SimpleDateFormat("yyyy-MM-dd");

                for(int i=0;i<timeList.size();i++){
                    String recordTime = timeList.get(i).getStudyTime();
                    int hour = Integer.parseInt(recordTime.substring(0,2));
                    int min = Integer.parseInt(recordTime.substring(3,5));
                    try {
                        if(hour == 1){
                            dayDecorator =  new RowDecorator(Dateformat.parse(timeList.get(i).getStudyDate()),getActivity());
                        }else if(hour==2){
                            dayDecorator =  new MiddleDecorator(Dateformat.parse(timeList.get(i).getStudyDate()),getActivity());
                        }else if(hour == 3){
                            dayDecorator =  new HighDecorator(Dateformat.parse(timeList.get(i).getStudyDate()),getActivity());
                        }else if(hour ==4){
                            dayDecorator =  new GoodDecorator(Dateformat.parse(timeList.get(i).getStudyDate()),getActivity());
                        }else if(hour >=5){
                            dayDecorator =  new GreatDecorator(Dateformat.parse(timeList.get(i).getStudyDate()),getActivity());
                        }else if(hour==0 && min>=30){
                            dayDecorator =  new DefaultDecorator(Dateformat.parse(timeList.get(i).getStudyDate()),getActivity());
                        }
                        binding.calendar.addDecorator(dayDecorator);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }else{

            }
        });

        binding.btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimerDialog.class);
                intent.putExtra("userNick",userNickName);
                startActivity(intent);
            }
        });

        //캘린더 관련 기능
        binding.calendar.setOnDateChangedListener(this);
        binding.calendar.setTopbarVisible(true);
        binding.calendar.addDecorators(new TodayDecorator(getActivity()), new SunDayDecorator(getActivity()), new SaturDayDecorator(getActivity()));


    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = format.format(date.getDate());
        Intent intent = new Intent(getActivity(), PopupDialog.class);
        Timer intentData = new Timer();
        for(int i = 0 ; i<timeList.size();i++){
            if(selectedDate.equals(timeList.get(i).getStudyDate())){
                intentData = timeList.get(i);
                break;
            }else{
                intentData.setStudyDate(selectedDate);
                intentData.setStudyTime("");
                intentData.setUserNick(userNickName);
            }
        }
        intent.putExtra("studyTime",intentData);

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LIFE", "onResume: ");
        viewModel.getEntireRecord(userNickName);
    }
}
