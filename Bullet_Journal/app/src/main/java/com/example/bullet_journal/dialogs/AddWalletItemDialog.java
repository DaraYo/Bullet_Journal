package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.InsertWalletItemAsyncTask;
import com.example.bullet_journal.enums.WalletItemType;
import com.example.bullet_journal.model.MonthlyBudget;
import com.example.bullet_journal.model.WalletItem;

public class AddWalletItemDialog extends Dialog {

    Object[] obj = null;
    private Context context;
    private MonthlyBudget monthlyBudget;
    private WalletItem walletItem = new WalletItem();
    private EditText itemAmount;
    private EditText itemName;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    public AddWalletItemDialog(Context context, MonthlyBudget monthlyBudget) {
        super(context);
        this.context = context;
        this.monthlyBudget = monthlyBudget;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        obj = new Object[2];
        obj[0] = monthlyBudget;
        obj[1] = walletItem;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wallet_item_dialog);
        setTitle(R.string.add_item_dialog_title);
        itemName = findViewById(R.id.item_name);
        itemAmount = findViewById(R.id.item_amount);
        radioGroup = findViewById(R.id.radioSex);

        final Button dialogOkBtn = findViewById(R.id.wallet_dialog_btn_ok);

        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemName.getText().toString().length() == 0) {
                    itemName.setError("Item name is required.");
                    return;
                }
                if (itemAmount.getText().toString().length() == 0) {
                    itemAmount.setError("Item amount is required.");
                    return;
                }
                walletItem.setName(itemName.getText().toString());
                walletItem.setAmount(Double.parseDouble(itemAmount.getText().toString()));

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                walletItem.setType(WalletItemType.valueOf(radioButton.getText().toString()));
                AsyncTask<Object, Void, Boolean> insertWalletItemAsyncTask = new InsertWalletItemAsyncTask(new AsyncResponse<Boolean>() {
                    @Override
                    public void taskFinished(Boolean retVal) {
                        if (retVal) {
                            Toast.makeText(context, R.string.wallet_item_saved, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                        }
                        dismiss();
                    }
                }).execute(obj);
            }
        });

        Button dialogCancelBtn = findViewById(R.id.wallet_dialog_btn_cancel);
        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}