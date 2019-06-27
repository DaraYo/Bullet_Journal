package com.example.bullet_journal.helpClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MockupData {
    private static List<Diary> diaries;

    public static Diary getDiary(Date date){

        if(diaries == null)
            diaries = new ArrayList<>();

        Diary retVal = null;

        Calendar cal = Calendar.getInstance();
        for (Diary item : diaries) {
            cal.setTime(item.getDiaryDate());
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            cal.setTime(date);
            int day2 = cal.get(Calendar.DAY_OF_MONTH);
            int month2 = cal.get(Calendar.MONTH);
            int year2 = cal.get(Calendar.YEAR);

            if(day == day2 && month == month2 && year == year2){
                retVal = item;
                break;
            }
        }
        return  retVal;
    }

    public static void updateDate(Diary diary){
        Diary tempDiary = getDiary((diary.getDiaryDate()));
        if(tempDiary != null)
        {
            diaries.remove(tempDiary);
        }

        diaries.add(diary);
    }

}
