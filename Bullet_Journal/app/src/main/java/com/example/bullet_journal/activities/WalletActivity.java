package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.WalletItemAdapter;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.GetWalletItemsByBudgetAsyncTask;
import com.example.bullet_journal.dialogs.AddWalletItemDialog;
import com.example.bullet_journal.enums.Month;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.WalletItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WalletActivity extends RootActivity {

    Button _chooseMonth;
    FloatingActionButton _addItem;
    WalletItemAdapter walletItemAdapter;
    private List<WalletItem> walletItems;
    private MonthlyBudget monthlyBudget;
    TextView currentMonthText;
    int year;
    Month month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date today = new Date();
        month = Month.valueOf(new SimpleDateFormat("MMM").format(today));
        year = Integer.parseInt(new SimpleDateFormat("YYYY").format(today));

        monthlyBudget = new MonthlyBudget(month.getValue(), year, null, 0.0);

        currentMonthText = findViewById(R.id.current_month);
        TextView balanceText = findViewById(R.id.balance);
        currentMonthText.setText(month + ", " + year);
        balanceText.setText(monthlyBudget.getBalance().toString() + " $");

        _chooseMonth = findViewById(R.id.choose_another_month);
        _chooseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField().show();
            }
        });

        _addItem = findViewById(R.id.add_item);

        _addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new AddWalletItemDialog(WalletActivity.this, monthlyBudget);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        fetchWallet();
                    }
                });
                dialog.show();
            }
        });

        walletItems = new ArrayList<>();
        walletItemAdapter = new WalletItemAdapter(this, walletItems);
        ListView spendingListView = findViewById(R.id.spendings_list_view);
        spendingListView.setAdapter(walletItemAdapter);

        fetchWallet();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
           monthlyBudget.setMonth(selectedMonth + 1);
           monthlyBudget.setYear(selectedYear);
           String monthString = Month.values()[selectedMonth].toString();
           currentMonthText.setText(monthString + ", " + selectedYear);
           fetchWallet();
        }
    };

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, datePickerListener, year, month.getValue()-1, 14);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }

        } catch (Exception ex) {
        }
        return dpd;
    }

    private void fetchWallet() {

        AsyncTask<MonthlyBudget, Void, List<WalletItem>> getWalletItems = new GetWalletItemsByBudgetAsyncTask(new AsyncResponse<List<WalletItem>>() {

            @Override
            public void taskFinished(List<WalletItem> retVal) {
                walletItems.clear();
                walletItems.addAll(retVal);
                walletItemAdapter.notifyDataSetChanged();
            }
        }).execute(monthlyBudget);
    }
}