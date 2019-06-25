package com.example.bullet_journal.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.core.content.res.ResourcesCompat;

import com.example.bullet_journal.R;
import com.example.bullet_journal.enums.MoodType;
import com.example.bullet_journal.wrapperClasses.MoodWrapper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneId;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayViewMoodDecorator implements DayViewDecorator {

    private List<MoodWrapper> moods;
    private MoodType mode;
    private Activity context;

    public DayViewMoodDecorator(Activity context, List<MoodWrapper> moodsToDisplay, MoodType mode){
        this.context = context;
        this.moods = moodsToDisplay;
        this.mode = mode;
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        Date calendarDate = DateTimeUtils.toDate(calendarDay.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(calendarDate);

        for(MoodWrapper tempMood : moods){
            cal2.setTime(tempMood.getDate());
            if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
                return true;
            }
        }

        return false;
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.setBackgroundDrawable(bindDrawable());
    }

    private Drawable bindDrawable(){

        switch(mode) {
            case AWESOME:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.red_mood_button, null);
            case GOOD:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.orange_mood_button, null);
            case AVERAGE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.yellow_mood_button, null);
            case BAD:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.green_mood_button, null);
            case TERRIBLE:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.purple_mood_button, null);
        }

        return null;
    }
}
