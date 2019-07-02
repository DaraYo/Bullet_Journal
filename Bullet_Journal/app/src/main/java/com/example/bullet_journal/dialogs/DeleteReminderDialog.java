package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.HabitsActivity;
import com.example.bullet_journal.activities.TasksAndEventsActivity;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.DeleteHabitEventAsyncTask;
import com.example.bullet_journal.async.DeleteTaskEventAsyncTask;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.HabitRemindersWrapper;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.ArrayList;

public class DeleteReminderDialog extends Dialog {

    private Context context;
    private Habit habit;
    private Task task;
    private Button btn_delete;
    private Button btn_cancel;

    public DeleteReminderDialog(Context context, Habit habit) {
        super(context);
        this.context = context;
        this.habit=habit;
        this.task=null;
    }

    public DeleteReminderDialog(Context context, Task task) {
        super(context);
        this.context = context;
        this.task=task;
        this.habit=null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_reminder_dialog);

        btn_cancel = (Button) findViewById(R.id.dialog_btn_cancel);
        btn_delete = (Button) findViewById(R.id.dialog_btn_delete);

        if (habit != null) {
            TextView dialogTitle = findViewById(R.id.reminder_delete_id);
            dialogTitle.setText(habit.getTitle());
        } else{
            TextView dialogTitle = findViewById(R.id.reminder_delete_id);
            dialogTitle.setText(task.getTitle());
        }

        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (habit != null) {

                    AsyncTask<HabitRemindersWrapper, Void, Boolean> deleteTaskEventAsyncTask = new DeleteHabitEventAsyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Intent intent = new Intent(context, HabitsActivity.class);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT);
                            }
                        }
                    }).execute(new HabitRemindersWrapper(habit, new ArrayList<Reminder>()));
                } else {
                    AsyncTask<TaskEventRemindersWrapper, Void, Boolean> deleteTaskEventAsyncTask = new DeleteTaskEventAsyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Intent intent = new Intent(context, TasksAndEventsActivity.class);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT);
                            }
                        }
                    }).execute(new TaskEventRemindersWrapper(task, new ArrayList<Reminder>()));
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}