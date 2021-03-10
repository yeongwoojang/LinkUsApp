package com.example.linkusapp.view.decorator;

import android.content.Context;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.example.linkusapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SaturDayDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date;
    Context context;
    public SaturDayDecorator(Context context) {
        this.context = context;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue400)));
        view.addSpan(new RelativeSizeSpan(1.1f));
    }
}
