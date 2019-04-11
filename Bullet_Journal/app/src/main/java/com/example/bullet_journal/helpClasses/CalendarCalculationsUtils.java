package com.example.bullet_journal.helpClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarCalculationsUtils {

    public static String setCurrentDate(String newDate){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("MMM dd, yyyy");

        if(!newDate.isEmpty()){
            try {
                calendar.setTime(dateFormat.parse(newDate));
            } catch (ParseException e) {
                return "";
            }
        }

        return dateFormat.format(calendar.getTime());
    }

    public static String[] calculateWeek(String startDate, int dayNum){
        String[] retVal = new String[dayNum];

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat();
        dateFormat.applyPattern("MMM dd, yyyy");

        try {
            calendar.setTime(dateFormat.parse(startDate));
        } catch (ParseException e) {
            return retVal;
        }

        for(int i = 0; i < dayNum; i++){
            calendar.add(Calendar.DATE, 1);
            retVal[i] = dateFormat.format(calendar.getTime());
        }
        return retVal;
    }

    public static String calculateWeekDay(String startDate){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        try {
            c.setTime(sdf.parse(startDate));
        } catch (ParseException e) {
            return "";
        }
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch(dayOfWeek) {
            case 1 : return "Sunday";
            case 2 : return "Monday";
            case 3 : return "Tuesday";
            case 4 : return "Wednesday";
            case 5 : return "Thursday";
            case 6 : return "Friday";
            case 7 : return "Saturday";
        }

        return "";
    }
}
