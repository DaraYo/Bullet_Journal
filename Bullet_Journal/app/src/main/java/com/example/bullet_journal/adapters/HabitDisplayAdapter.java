package com.example.bullet_journal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.HabitActivity;
import com.example.bullet_journal.model.Habit;

import java.util.List;

public class HabitDisplayAdapter extends ArrayAdapter<Habit>  {

    private Context context;

    public HabitDisplayAdapter(Context context, List<Habit> objects) {
        super(context, R.layout.habit_preview_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.habit_preview_adapter, parent, false);

        final Habit habitObj = getItem(position);

        TextView habitTitle = view.findViewById(R.id.habit_title);
        habitTitle.setText(habitObj.getTitle());

        habitTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HabitActivity.class);
                context.startActivity(intent);
            }
        });

        CheckBox habitCheckBox = view.findViewById(R.id.habit_tracker_check);
        habitCheckBox.setChecked(habitObj.isStatus());

        return view;
    }
}
