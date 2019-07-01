package com.example.bullet_journal.async;

import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.WalletItem;

public class InsertWalletItemAsyncTask extends AsyncTask<Object, Void, Boolean> {

    public AsyncResponse delegate = null;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    public InsertWalletItemAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        MonthlyBudget monthlyBudgetDto = (MonthlyBudget) params[0];
        Long userId = monthlyBudgetDto.getUserId();
        int month = monthlyBudgetDto.getMonth();
        int year = monthlyBudgetDto.getYear();
        WalletItem walletItem = (WalletItem) params[1];
        MonthlyBudget monthlyBudget;

        monthlyBudget = database.getMonthlyBudgetDao().getByUserAndDate(month, year);
        if (monthlyBudget == null) {
            monthlyBudget.setId(database.getMonthlyBudgetDao().insert(monthlyBudgetDto));
        }
        try {
            walletItem.setWalletId(monthlyBudget.getId());
            database.getWalletItemDao().insert(walletItem);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        this.delegate.taskFinished(aBoolean);
    }
}
