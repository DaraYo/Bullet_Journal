package com.example.bullet_journal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.enums.WalletItemType;
import com.example.bullet_journal.model.WalletItem;

import java.util.List;

public class WalletItemAdapter extends ArrayAdapter<WalletItem> {

    private Context context;

    public WalletItemAdapter(Context context, List<WalletItem> objects) {
        super(context, R.layout.wallet_item_preview_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.wallet_item_preview_adapter, parent, false);

        final WalletItem walletItem = getItem(position);

        TextView spendingName = view.findViewById(R.id.wallet_item_name);
        spendingName.setText(walletItem.getName());

        TextView spendingPrice = view.findViewById(R.id.wallet_item_price);

        String incomeText = "+ " + walletItem.getAmount().toString() + " $";
        String spendingText = "- " + walletItem.getAmount().toString() + " $";
        if (walletItem.getType().equals(WalletItemType.INCOME)) {
            spendingPrice.setText(incomeText);
            spendingPrice.setTextColor(context.getResources().getColor(R.color.pastelGreen));
        } else if (walletItem.getType().equals(WalletItemType.SPENDING)) {
            spendingPrice.setText(spendingText);
            spendingPrice.setTextColor(context.getResources().getColor(R.color.moodRed));
        }
        return view;
    }
}