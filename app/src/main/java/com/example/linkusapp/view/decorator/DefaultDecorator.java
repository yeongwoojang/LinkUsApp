package com.example.linkusapp.view.decorator;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.example.linkusapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Date;

public class DefaultDecorator implements DayViewDecorator {

    private CalendarDay date;
    private final Calendar calendar = Calendar.getInstance();
    Context context;
    public DefaultDecorator(Date value, Context context) {
       date = CalendarDay.from(value);
       this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.NORMAL));
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.cal_state_default));
        view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)));
        if(date.getCalendar().get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
            view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue500)));
        }else if(date.getCalendar().get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY){
            view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red300)));
        }


    }

}
