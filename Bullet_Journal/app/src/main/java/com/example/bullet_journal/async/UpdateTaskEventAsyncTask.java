package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;
import com.example.bullet_journal.wrapperClasses.TaskEventRemindersWrapper;

import java.util.ArrayList;
import java.util.List;

public class UpdateTaskEventAsyncTask extends AsyncTask<TaskEventRemindersWrapper, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public UpdateTaskEventAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(TaskEventRemindersWrapper... taskEventRemindersWrappers) {

        try{
            Task taskEvent = taskEventRemindersWrappers[0].getTaskEvent();
            ArrayList<Reminder> reminders = taskEventRemindersWrappers[0].getReminders();

            taskEvent.setSynced(false);
            database.getTaskEventDao().update(taskEvent);

            List<Reminder> dbReminedrs = database.getReminderDao().getAllRemindersForTask(taskEvent.getId());
            Long id = taskEvent.getId();

            // Delete removed reminders
            if(!dbReminedrs.isEmpty()){
                for(Reminder tempDbReminder : dbReminedrs){
                    boolean found = false;

                    if(!reminders.isEmpty()){
                        for(Reminder tempReminder : reminders){
                            if(tempDbReminder.getId().equals(tempReminder.getId())){
                                found = true;
                                break;
                            }
                        }
                    }

                    if(!found){
                        database.getReminderDao().delete(tempDbReminder);
                    }
                }
            }

            // Insert new reminders
            if(!reminders.isEmpty()){
                for(Reminder reminder : reminders){
                    if(reminder.getId() == null){
                        reminder.setTaskId(id);
                        database.getReminderDao().insert(reminder);
                    }
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