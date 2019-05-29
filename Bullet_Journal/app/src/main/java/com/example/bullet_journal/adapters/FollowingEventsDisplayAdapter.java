package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Task;

import java.util.List;

public class FollowingEventsDisplayAdapter extends ArrayAdapter<Task>{

    private Context context;

    public FollowingEventsDisplayAdapter(Context context, List<Task> objects) {
        super(context, R.layout.task_display_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.day_event_preview_adapter, parent, false);

        Task taskObj = getItem(position);
        String date= CalendarCalculationsUtils.dateMillisToString(taskObj.getDate());

        TextView month = view.findViewById(R.id.event_preview_month);
        month.setText(date.substring(0, 3));

        TextView day = view.findViewById(R.id.event_preview_day);
        day.setText(date.substring(4, 6));

        TextView time = view.findViewById(R.id.event_preview_time);
        time.setText(date.substring(13, date.length()));

        TextView title = view.findViewById(R.id.event_preview_title);
        title.setText(taskObj.getTitle());

        TextView text = view.findViewById(R.id.event_preview_description);
        text.setText(taskObj.getText());

        return view;
    }

}
