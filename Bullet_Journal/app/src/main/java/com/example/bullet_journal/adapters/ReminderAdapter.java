package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Reminder;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private String choosenDate = "";
    private Context context;

    public ReminderAdapter(Context context, List<Reminder> objects) {
        super(context, R.layout.reminder_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.reminder_adapter, parent, false);

        final Reminder reminderObj = getItem(position);

        TextView reminderTitle = view.findViewById(R.id.reminder);
        reminderTitle.setText(reminderObj.getName());

        ImageButton deleteBtn = view.findViewById(R.id.reminder_delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(reminderObj);
                notifyDataSetChanged();
            }
        });

        TextView reminderDate = view.findViewById(R.id.reminder_time);
        choosenDate = CalendarCalculationsUtils.dateMillisToStringDateAndTime(reminderObj.getDate());
        reminderDate.setText(choosenDate);

        return view;

    }
}