package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.DiaryImage;

public class InsertDiaryImageAsyncTask extends AsyncTask<DiaryImage, Void, DiaryImage> {
    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertDiaryImageAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
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
