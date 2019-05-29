package com.example.bullet_journal.helpClasses;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarCalculationsUtils {

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

    public static String calculateWeekDay(long milliseconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

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

    public static long getBeginingOfTheMonth(int month, int year){
        Calendar calendar = Calendar.getInstance();
        int day = 1;
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
}
