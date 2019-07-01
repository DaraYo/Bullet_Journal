package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.DiaryImage;

import java.util.List;

public class GetDiaryImagesAsyncTask  extends AsyncTask<Long, Void, List<DiaryImage>> {

    public AsyncResponse delegate;
    private MainDatabase database;

    public GetDiaryImagesAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected List<DiaryImage> doInBackground(Long... longs) {
        long dayId = longs[0];
        List<DiaryImage> retVal = database.getDiaryImageDao().getByDiaryImagesId(dayId);

        return retVal;
    }

    @Override
    protected void onPostExecute(List<DiaryImage> diaryImages) {
        delegate.taskFinished(diaryImages);
    }
}
