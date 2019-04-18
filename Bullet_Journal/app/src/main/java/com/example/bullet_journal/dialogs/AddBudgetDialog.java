package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bullet_journal.R;

public class AddBudgetDialog extends Dialog {

    private Context context;

    public AddBudgetDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_budget_dialog);

        TextView dialogTitle = findViewById(R.id.add_budget_dialog_title);
        dialogTitle.setText(R.string.add_budget);
    }

  /*  Button dialogOkBtn = findViewById(R.id.budget_dialog_btn_ok);
        dialogOkBtn. setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        dismiss();
    }
    });

    Button dialogCancelBtn = findViewById(R.id.budget_dialog_btn_cancel);
        dialogCancelBtn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        dismiss();
    }
    });
    }*/
}