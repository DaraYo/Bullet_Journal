package com.example.bullet_journal.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.synchronization.GetDaysForSyncTask;
import com.example.bullet_journal.synchronization.GetMoodsForSyncTask;
import com.example.bullet_journal.synchronization.GetRatingsForSyncTask;
import com.example.bullet_journal.synchronization.GetRemindersForSyncTask;
import com.example.bullet_journal.synchronization.GetTasksForSyncTask;

public class PushToFirestoreJobService extends JobService {

    private boolean isCanceled = false;
    private static String LOG_TAG = "SYNC_BG_JOB: ";
    public static final long REDO_FIRESTORE_PULL = 15 * 60 * 1000;

    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();


    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(LOG_TAG, "Job canceled before completion");
        isCanceled = true;
        return false;
    }

    private void doBackgroundWork(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (isCanceled) {
                    return;
                }

                try {

                    AsyncTask<Void, Void, Boolean> manageDaysTask = new GetDaysForSyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Log.i("** SYNC DONE **", "DAYS, SUCCESS");
                            } else {
                                Log.i("** SYNC DONE **", "DAYS, FAILED");
                            }
                        }
                    }).execute();

                    AsyncTask<Void, Void, Boolean> manageRatingsTask = new GetRatingsForSyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Log.i("** SYNC DONE **", "RATINGS, SUCCESS");
                            } else {
                                Log.i("** SYNC DONE **", "RATINGS, FAILED");
                            }
                        }
                    }).execute();

                    AsyncTask<Void, Void, Boolean> manageMoodsTask = new GetMoodsForSyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Log.i("** SYNC DONE **", "MOODS, SUCCESS");
                            } else {
                                Log.i("** SYNC DONE **", "MOODS, FAILED");
                            }
                        }
                    }).execute();

                    AsyncTask<Void, Void, Boolean> manageTasksTask = new GetTasksForSyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Log.i("** SYNC DONE **", "TASKS, SUCCESS");
                            } else {
                                Log.i("** SYNC DONE **", "TASKS, FAILED");
                            }
                        }
                    }).execute();

                    AsyncTask<Void, Void, Boolean> manageRemindersTask = new GetRemindersForSyncTask(new AsyncResponse<Boolean>() {
                        @Override
                        public void taskFinished(Boolean retVal) {
                            if (retVal) {
                                Log.i("** SYNC DONE **", "REMINDERS, SUCCESS");
                            } else {
                                Log.i("** SYNC DONE **", "REMINDERS, FAILED");
                            }
                        }
                    }).execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(LOG_TAG, "JOB FINISHED");
                jobFinished(params, false);
            }
        }).start();
    }

}
