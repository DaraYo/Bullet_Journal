package com.example.bullet_journal.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.EventActivity;
import com.example.bullet_journal.activities.TaskActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetRemindersCountForTaskEventAsyncTask;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Task;

import java.util.List;

public class TaskEventDisplayAdapter extends ArrayAdapter<Task> {

    private Context context;
    private TaskType mode;

    public TaskEventDisplayAdapter(Context context, List<Task> objects, TaskType mode) {
        super(context, R.layout.task_display_adapter, objects);
        this.context = context;
        this.mode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(mode.equals(TaskType.TASK)){

            View view = LayoutInflater.from(getContext()).inflate(R.layout.task_display_adapter, parent, false);
            final Task taskEventObj = getItem(position);

            buildView(view, taskEventObj);

            LinearLayout taskBox = view.findViewById(R.id.task_box);
            taskBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskActivity.class);
                    context.startActivity(intent);
                }
            });

            CheckBox taskEventCheckBox = view.findViewById(R.id.task_event_tracker_check);
            taskEventCheckBox.setChecked(taskEventObj.isStatus());

            final TextView reminderCount = view.findViewById(R.id.reminder_count);

            AsyncTask<Long, Void, Integer> getRemindersCountForTaskEventAsyncTask = new GetRemindersCountForTaskEventAsyncTask(new AsyncResponse<Integer>(){
                @Override
                public void taskFinished(Integer retVal) {
                    reminderCount.setText(retVal.toString());
                }
            }).execute(taskEventObj.getId());

            return view;
        }else if(mode.equals(TaskType.EVENT)){

            View view = LayoutInflater.from(getContext()).inflate(R.layout.event_display_adapter, parent, false);
            final Task taskEventObj = getItem(position);

            buildView(view, taskEventObj);

            LinearLayout taskBox = view.findViewById(R.id.task_box);
            taskBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventActivity.class);
                    context.startActivity(intent);
                }
            });

            return view;
        }

        return null;
    }

    private void buildView(View view, Task taskObj){

        TextView taskEventTime = view.findViewById(R.id.task_time_str);
        taskEventTime.setText(CalendarCalculationsUtils.dateMillisToStringTime(taskObj.getDate()));

        TextView taskEventTitle = view.findViewById(R.id.task_event_title);
        taskEventTitle.setText(taskObj.getTitle());

        TextView taskEventText = view.findViewById(R.id.task_event_text);
        taskEventText.setText(taskObj.getText());
    }

}
