package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.DiaryImage;

public class InsertDiaryImageAsyncTask extends AsyncTask<DiaryImage, Void, DiaryImage> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public InsertDiaryImageAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected DiaryImage doInBackground(DiaryImage... diaryImages) {
        long id= database.getDiaryImageDao().insert(diaryImages[0]);
        return database.getDiaryImageDao().get(id);
    }

    @Override
    protected void onPostExecute(DiaryImage diaryImage) {
        delegate.taskFinished(diaryImage);
    }
}
