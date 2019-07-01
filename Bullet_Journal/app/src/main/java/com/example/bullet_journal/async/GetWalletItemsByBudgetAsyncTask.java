package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.WalletItem;

import java.util.List;

public class GetWalletItemsByBudgetAsyncTask extends AsyncTask<MonthlyBudget, Void, List<WalletItem>> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private MonthlyBudget monthlyBudget;

    public GetWalletItemsByBudgetAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<WalletItem> doInBackground(MonthlyBudget... budget) {
        Long userId = budget[0].getUserId();
        int month = budget[0].getMonth();
        int year = budget[0].getYear();

        monthlyBudget = database.getMonthlyBudgetDao().getByUserAndDate(month, year);
        if (monthlyBudget == null) {
            long id = database.getMonthlyBudgetDao().insert(budget[0]);
            return database.getWalletItemDao().getAllItemsForMonthlyBudget(id);
        } else {
            return database.getWalletItemDao().getAllItemsForMonthlyBudget(monthlyBudget.getId());
        }
    }

    @Override
    protected void onPostExecute(List<WalletItem> walletItems) {
        this.delegate.taskFinished(walletItems);
    }
}
