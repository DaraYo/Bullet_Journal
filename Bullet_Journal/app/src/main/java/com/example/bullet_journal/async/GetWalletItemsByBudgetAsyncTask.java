package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.MonthlyBudget;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GetWalletItemsByBudgetAsyncTask extends AsyncTask<MonthlyBudget, Void, ArrayList<Object>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private MonthlyBudget monthlyBudget;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public GetWalletItemsByBudgetAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Object> doInBackground(MonthlyBudget... budget) {
        int month = budget[0].getMonth();
        int year = budget[0].getYear();

        Long userId = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid()).getId();
        monthlyBudget = database.getMonthlyBudgetDao().getByUserAndDate(userId, month, year);
        ArrayList<Object> retList = new ArrayList<>();
        if (monthlyBudget == null) {
            budget[0].setUserId(userId);
            long id = database.getMonthlyBudgetDao().insert(budget[0]);
            retList.add(database.getWalletItemDao().getAllItemsForMonthlyBudget(id));
            retList.add(new Double(0.0));
            return retList;
        } else {
            retList.add(database.getWalletItemDao().getAllItemsForMonthlyBudget(monthlyBudget.getId()));
            retList.add(monthlyBudget.getBalance());
            return retList;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Object> list) {
        this.delegate.taskFinished(list);
    }
}
