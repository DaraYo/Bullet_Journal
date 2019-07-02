package com.example.bullet_journal.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.EventActivity;
import com.example.bullet_journal.activities.TaskActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.CompleteTaskAsyncTask;
import com.example.bullet_journal.async.GetRemindersCountForTaskEventAsyncTask;
import com.example.bullet_journal.async.GetRemindersForTaskEventAsyncTask;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.ArrayList;
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

        final ArrayList<Reminder> reminders = new ArrayList<>();

        if (mode.equals(TaskType.TASK)) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.task_display_adapter, parent, false);
            final Task taskEventObj = getItem(position);

            buildView(view, taskEventObj);

            final LinearLayout taskBox = view.findViewById(R.id.task_box);
            taskBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskEventInfo", new TaskEventRemindersWrapper(taskEventObj, reminders));
                    bundle.putBoolean("isEdit", true);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });

            final CheckBox taskEventCheckBox = view.findViewById(R.id.task_event_tracker_check);
            taskEventCheckBox.setChecked(taskEventObj.isStatus());
            taskEventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AsyncTask<Task, Void, Boolean> completeTaskAsyncTask = new CompleteTaskAsyncTask(context, new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Toast.makeText(context, R.string.task_status_updated, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute(taskEventObj);
                }
            });

            final TextView reminderCount = view.findViewById(R.id.reminder_count);

            AsyncTask<Long, Void, Integer> getRemindersCountForTaskEventAsyncTask = new GetRemindersCountForTaskEventAsyncTask(context, new AsyncResponse<Integer>() {
                @Override
                public void taskFinished(Integer retVal) {
                    reminderCount.setText(retVal.toString());
                }
            }).execute(taskEventObj.getId());

            fetchReminders(taskEventObj.getId(), reminders);

            return view;
        } else if (mode.equals(TaskType.EVENT)) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.event_display_adapter, parent, false);
            final Task taskEventObj = getItem(position);

            buildView(view, taskEventObj);

            LinearLayout taskBox = view.findViewById(R.id.task_box);
            taskBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskEventInfo", new TaskEventRemindersWrapper(taskEventObj, reminders));
                    bundle.putBoolean("isEdit", true);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });

            final TextView reminderCount = view.findViewById(R.id.reminder_count);

            AsyncTask<Long, Void, Integer> getRemindersCountForTaskEventAsyncTask = new GetRemindersCountForTaskEventAsyncTask(context, new AsyncResponse<Integer>() {
                @Override
                public void taskFinished(Integer retVal) {
                    reminderCount.setText(retVal.toString());
                }
            }).execute(taskEventObj.getId());

            fetchReminders(taskEventObj.getId(), reminders);

            return view;
        }

        return null;
    }

    private void buildView(View view, Task taskObj) {

        TextView taskEventTime = view.findViewById(R.id.task_time_str);
        taskEventTime.setText(CalendarCalculationsUtils.dateMillisToStringTime(taskObj.getDate()));

        TextView taskEventTitle = view.findViewById(R.id.task_event_title);
        taskEventTitle.setText(taskObj.getTitle());

        TextView taskEventText = view.findViewById(R.id.task_event_text);
        taskEventText.setText(taskObj.getText());
    }

    private void fetchReminders(Long taskId, final ArrayList<Reminder> reminders) {

        AsyncTask<Long, Void, List<Reminder>> getRemindersForTaskEventAsyncTask = new GetRemindersForTaskEventAsyncTask(context, new AsyncResponse<List<Reminder>>() {
            @Override
            public void taskFinished(List<Reminder> retVal) {
                reminders.clear();
                reminders.addAll(retVal);
            }
        }).execute(taskId);

    }

}
