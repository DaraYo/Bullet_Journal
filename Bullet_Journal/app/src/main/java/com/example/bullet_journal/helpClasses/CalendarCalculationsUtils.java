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

        Date date = null;
        try {
            date = originalFormat.parse(month + "/" + day + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
