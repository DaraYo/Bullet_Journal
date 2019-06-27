package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.ArrayList;

public class InsertTaskEventAsyncTask extends AsyncTask<TaskEventRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertTaskEventAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(TaskEventRemindersWrapper... taskEventRemindersWrappers) {

        try{
            Task taskEvent = taskEventRemindersWrappers[0].getTaskEvent();
            ArrayList<Reminder> reminders = taskEventRemindersWrappers[0].getReminders();

            long id = database.getTaskEventDao().insert(taskEvent);

            for(Reminder reminder : reminders){
                reminder.setTaskId(id);
                database.getReminderDao().insert(reminder);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        delegate.taskFinished(aBoolean);
    }
}