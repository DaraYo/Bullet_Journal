package com.example.bullet_journal.helpClasses;


import android.content.Context;

import com.example.bullet_journal.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarCalculationsUtils {

    public static final long ONE_DAY_MILLIS = 86400000;
    public static final long ONE_HOUR_MILLIS = 3600000;
    public static final long ONE_MINUTE_MILLIS = 60000;

    public static String dateMillisToString(long milliseconds){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        calendar.setTimeInMillis(milliseconds);
        return dateFormat.format(calendar.getTime());
    }

    public static String dateMillisToStringDateAndTime(long milliseconds){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm");
        calendar.setTimeInMillis(milliseconds);
        return dateFormat.format(calendar.getTime());
    }

    public static String dateMillisToStringTime(long milliseconds){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        calendar.setTimeInMillis(milliseconds);
        return dateFormat.format(calendar.getTime());
    }

    public static String calculateWeekDay(long milliseconds, Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch(dayOfWeek) {
            case 1 : return context.getString(R.string.sun);
            case 2 : return context.getString(R.string.mon);
            case 3 : return context.getString(R.string.tue);
            case 4 : return context.getString(R.string.wed);
            case 5 : return context.getString(R.string.thu);
            case 6 : return context.getString(R.string.fri);
            case 7 : return context.getString(R.string.sat);
        }

        return "";
    }

    public static Date convertCalendarDialogDate(int day, int month, int year){
        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        try {
            return originalFormat.parse(month + "/" + day + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long trimTimeFromDateMillis(long milliseconds){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date(milliseconds);
        try {
            return formatter.parse(formatter.format(today)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getBeginningOfTheMonth(int month, int year){
        Calendar calendar = Calendar.getInstance();
        int day = 0;
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfTheMonth(int month, int year){
        Calendar calendar = Calendar.getInstance();
        int day = 1;
        calendar.set(year, month, day);
        int numOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);

        return calendar.getTimeInMillis();
    }

    public static long addHoursAndMinutesToDate(long dateMillis, int hours, int minutes){

        return dateMillis+(hours*ONE_HOUR_MILLIS)+(minutes*ONE_MINUTE_MILLIS);
    }
}
