package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;

public class SimpleDateDisplayAdapter extends ArrayAdapter<String> {

    public SimpleDateDisplayAdapter(Context context, String[] dates){
        super(context, R.layout.date_preview_adapter, dates);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.date_preview_adapter, parent, false);

        TextView weekDisplay = (TextView) view.findViewById(R.id.day_of_week);
        weekDisplay.setText(CalendarCalculationsUtils.calculateWeekDay(getItem(position)));

        TextView dateDisplay = (TextView) view.findViewById(R.id.date_display);
        dateDisplay.setText(getItem(position));

        return view;
    }

}
