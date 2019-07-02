package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.WalletItem;
import com.google.firebase.auth.FirebaseAuth;

public class InsertWalletItemAsyncTask extends AsyncTask<Object, Void, Boolean> {

    public AsyncResponse delegate = null;
    private Context context;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public InsertWalletItemAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        MonthlyBudget monthlyBudgetDto = (MonthlyBudget) params[0];
        int month = monthlyBudgetDto.getMonth();
        int year = monthlyBudgetDto.getYear();
        WalletItem walletItem = (WalletItem) params[1];
        MonthlyBudget monthlyBudget;

        Long userId = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid()).getId();
        monthlyBudgetDto.setUserId(userId);
        monthlyBudget = database.getMonthlyBudgetDao().getByUserAndDate(userId, month, year);
        Long id = monthlyBudget.getId();
        if (monthlyBudget == null) {
            id = database.getMonthlyBudgetDao().insert(monthlyBudgetDto);
        }
        try {
            walletItem.setWalletId(id);
            database.getWalletItemDao().insert(walletItem);
            MonthlyBudget updated = database.getMonthlyBudgetDao().get(id);
            if (walletItem.getType().toString().equals("SPENDING")){
                updated.setBalance(updated.getBalance() - walletItem.getAmount());
            } else {
                updated.setBalance(updated.getBalance() + walletItem.getAmount());
            }
            database.getMonthlyBudgetDao().update(updated);
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
