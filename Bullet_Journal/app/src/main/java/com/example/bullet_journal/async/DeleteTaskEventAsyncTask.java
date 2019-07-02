package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.List;

public class DeleteTaskEventAsyncTask extends AsyncTask<TaskEventRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public DeleteTaskEventAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(TaskEventRemindersWrapper... taskEventRemindersWrapper) {

        try{
            Task task  = taskEventRemindersWrapper[0].getTaskEvent();

            database.getTaskEventDao().update(task);

            List<Reminder> dbReminedrs = database.getReminderDao().getAllRemindersForTask(task.getId());
            Long id = task.getId();

            // Delete reminders
            if(!dbReminedrs.isEmpty()){
                for(Reminder tempDbReminder : dbReminedrs){
                    database.getReminderDao().delete(tempDbReminder);
                }
            }

            database.getTaskEventDao().delete(task);

            //TODO: delete task/da

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