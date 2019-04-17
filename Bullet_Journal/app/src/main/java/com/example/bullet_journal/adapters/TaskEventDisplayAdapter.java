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
import com.example.bullet_journal.activities.EventActivity;
import com.example.bullet_journal.activities.TaskActivity;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.model.Task;

import java.util.List;

public class TaskEventDisplayAdapter extends ArrayAdapter<Task> {

    private Context context;
    private TaskType mode;

    public TaskEventDisplayAdapter(Context context, List<Task> objects, TaskType mode) {
        super(context, R.layout.task_event_display_adapter, objects);
        this.context = context;
        this.mode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.task_event_display_adapter, parent, false);

        final Task taskEventObj = getItem(position);

        TextView taskEventTitle = view.findViewById(R.id.task_event_title);
        taskEventTitle.setText(taskEventObj.getTitle());

        if(mode.equals(TaskType.TASK)){
            taskEventTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskActivity.class);
                    context.startActivity(intent);
                }
            });

        }else if(mode.equals(TaskType.EVENT)){
            taskEventTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        CheckBox taskEventCheckBox = view.findViewById(R.id.task_event_tracker_check);
        taskEventCheckBox.setChecked(taskEventObj.isStatus());

        return view;
    }
}