package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bullet_journal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SimpleDateDisplayAdapter extends ArrayAdapter<String> {

    public SimpleDateDisplayAdapter(Context context, String[] dates){
        super(context, R.layout.date_preview_adapter, dates);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.date_preview_adapter, parent, false);

        TextView weekDisplay = (TextView) view.findViewById(R.id.day_of_week);
        weekDisplay.setText(calculateWeekDay(getItem(position)));

        TextView dateDisplay = (TextView) view.findViewById(R.id.date_display);
        dateDisplay.setText(getItem(position));

        return view;
    }

    private String calculateWeekDay(String dateStr){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        try {
            c.setTime(sdf.parse(dateStr.trim()));
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
