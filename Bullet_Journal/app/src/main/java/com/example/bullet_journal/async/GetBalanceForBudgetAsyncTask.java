package com.example.bullet_journal.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.model.MonthlyBudget;
import com.google.firebase.auth.FirebaseAuth;

public class GetBalanceForBudgetAsyncTask extends AsyncTask<MonthlyBudget, Void, Double> {

    public AsyncResponse delegate;
    private MainDatabase database;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public GetBalanceForBudgetAsyncTask(Context context, AsyncResponse delegate) {
        this.delegate = delegate;
        this.database = DatabaseClient.getInstance(context).getDatabase();
    }

    @Override
    protected Double doInBackground(MonthlyBudget... budget) {
        int month = budget[0].getMonth();
        int year = budget[0].getYear();
        MonthlyBudget monthlyBudget;

        Long userId = database.getUserDao().getByFirestoreId(fAuth.getCurrentUser().getUid()).getId();
        monthlyBudget = database.getMonthlyBudgetDao().getByUserAndDate(userId, month, year);
        if (monthlyBudget == null) {
            return 0.0;
        } else {
            return database.getMonthlyBudgetDao().get(monthlyBudget.getId()).getBalance();
        }
    }

    @Override
    protected void onPostExecute(Double balance) {

        this.delegate.taskFinished(balance);
    }
}