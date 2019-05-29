package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bullet_journal.R;

public class AddIncomeDialog extends Dialog {

    private Context context;

    public AddIncomeDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income_dialog);
        setTitle(R.string.add_income_dialog_title);

        Button dialogOkBtn = findViewById(R.id.wallet_dialog_btn_ok);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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