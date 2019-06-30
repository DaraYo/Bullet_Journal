package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.DiaryImage;

public class DeleteDiaryImageAsyncTask extends AsyncTask<Long, Void, Boolean> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public DeleteDiaryImageAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Boolean doInBackground(Long... diaryImageIds) {

        try{
            DiaryImage img = database.getDiaryImageDao().get(diaryImageIds[0]);
            img.setDeleted(true);
            database.getDiaryImageDao().update(img);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean retVal) {

        delegate.taskFinished(retVal);
    }
}
