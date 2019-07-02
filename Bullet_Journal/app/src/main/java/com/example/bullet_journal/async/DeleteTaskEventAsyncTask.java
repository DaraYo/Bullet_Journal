package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.List;

public class DeleteTaskEventAsyncTask extends AsyncTask<Task, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public DeleteTaskEventAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Task... tasks) {

        try{
            Task task = tasks[0];

            task.setDeleted(true);
            database.getTaskEventDao().update(task);

            List<Reminder> reminders = database.getReminderDao().getAllRemindersForTask(task.getId());
            if(!reminders.isEmpty()){
                for(Reminder reminder : reminders){
                    reminder.setDeleted(true);
                    database.getReminderDao().update(reminder);
                }
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