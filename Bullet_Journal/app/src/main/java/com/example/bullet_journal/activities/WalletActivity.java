package com.example.bullet_journal.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.SpendingAdapter;
import com.example.bullet_journal.dialogs.AddWalletItemDialog;
import com.example.bullet_journal.enums.WalletItemType;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.WalletItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WalletActivity extends RootActivity {

    Button _chooseMonth;
    FloatingActionButton _addItem;

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
        monthlyBudget.setBalance(1527.35);

        TextView currentMonthText = (TextView) findViewById(R.id.current_month);
        TextView balanceText = (TextView) findViewById(R.id.balance);
        currentMonthText.setText(monthlyBudget.getCurrentMonth());
        balanceText.setText(monthlyBudget.getBalance().toString() + " $");

        _chooseMonth = (Button) findViewById(R.id.choose_another_month);
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
            final Dialog dialog = new AddWalletItemDialog(WalletActivity.this);
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

    private List<WalletItem> buildSpendings() {
        List<WalletItem> retVal = new ArrayList<>();

        WalletItem walletItem1 = new WalletItem("Water", 0.55, WalletItemType.SPENDING);
        WalletItem walletItem2 = new WalletItem("Salary", 1050.00, WalletItemType.INCOME);
        WalletItem walletItem3 = new WalletItem("Bag", 20.0, WalletItemType.SPENDING);
        WalletItem walletItem4 = new WalletItem("Pants", 0.55, WalletItemType.SPENDING);
        WalletItem walletItem5 = new WalletItem("Reward", 50.00, WalletItemType.INCOME);
        WalletItem walletItem6 = new WalletItem("Pizza", 10.0, WalletItemType.SPENDING);
        WalletItem walletItem7 = new WalletItem("Monitor", 300.00, WalletItemType.SPENDING);
        WalletItem walletItem8 = new WalletItem("Jeans", 42.20, WalletItemType.SPENDING);
        WalletItem walletItem9 = new WalletItem("Dress", 55.3, WalletItemType.SPENDING);

        retVal.add(walletItem1);
        retVal.add(walletItem2);
        retVal.add(walletItem3);
        retVal.add(walletItem4);
        retVal.add(walletItem5);
        retVal.add(walletItem6);
        retVal.add(walletItem7);
        retVal.add(walletItem8);
        retVal.add(walletItem9);

        return retVal;
    }
}