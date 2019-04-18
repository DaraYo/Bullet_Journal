package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.SpendingAdapter;
import com.example.bullet_journal.dialogs.AddBudgetDialog;
import com.example.bullet_journal.dialogs.AddEditMoodDialog;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.Spending;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class WalletActivity extends RootActivity {

    Button _chooseMonth;
    FloatingActionButton _addBudget, addItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MonthlyBudget monthlyBudget = new MonthlyBudget();
        Date today = new Date();
        DateFormat format = new SimpleDateFormat("MMM, yyyy");
        String currentMonth = format.format(today);
        monthlyBudget.setCurrentMonth(currentMonth);
        monthlyBudget.setAmount(1550.0);
        monthlyBudget.setBalance(1527.35);

        TextView currentMonthText = (TextView) findViewById(R.id.current_month);
        TextView budgetText = (TextView) findViewById(R.id.monthly_budget);
        TextView balanceText = (TextView) findViewById(R.id.balance);
        currentMonthText.setText(monthlyBudget.getCurrentMonth());
        budgetText.setText(monthlyBudget.getAmount().toString() + " $");
        balanceText.setText(monthlyBudget.getBalance().toString() + " $");

        _chooseMonth = (Button) findViewById(R.id.choose_another_month);
        _addBudget = findViewById(R.id.add_budget);
        _chooseMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField().show();
            }
        });

        _addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final Dialog dialog = new AddBudgetDialog(WalletActivity.this);
            dialog.show();
        }});

    SpendingAdapter spendingAdapter = new SpendingAdapter(this, buildSpendings());
    ListView spendingsListView = findViewById(R.id.spendings_list_view);
        spendingsListView.setAdapter(spendingAdapter);

}

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2019, 4, 18);
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

    private List<Spending> buildSpendings() {
        List<Spending> retVal = new ArrayList<>();

        Spending spending1 = new Spending("Water", 0.55);
        Spending spending2 = new Spending("Pizza", 2.10);
        Spending spending3 = new Spending("Bag", 20.0);

        retVal.add(spending1);
        retVal.add(spending2);
        retVal.add(spending3);

        return retVal;
    }
}