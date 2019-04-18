package com.example.bullet_journal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.model.Spending;

import java.util.List;

public class SpendingAdapter extends ArrayAdapter<Spending> {

    private Context context;

    public SpendingAdapter(Context context, List<Spending> objects) {
        super(context, R.layout.spending_preview_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.spending_preview_adapter, parent, false);

        final Spending spending = getItem(position);

        TextView spendingName = view.findViewById(R.id.spending_name);
        spendingName.setText(spending.getItemName());

        TextView spendingPrice = view.findViewById(R.id.spending_price);
        spendingPrice.setText(spending.getItemPrice().toString() + " $");

        return view;
    }
}