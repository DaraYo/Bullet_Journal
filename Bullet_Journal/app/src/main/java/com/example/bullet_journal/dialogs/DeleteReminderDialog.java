package com.example.bullet_journal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bullet_journal.R;

public class DeleteReminderDialog extends Dialog {

    private Context context;
    private String title;
    private Button btn_delete;
    private Button btn_cancel;

    public DeleteReminderDialog(Context context, String title) {
        super(context);
        this.context = context;
        this.title=title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_reminder_dialog);

        TextView dialogTitle = findViewById(R.id.reminder_delete_id);
        dialogTitle.setText(title);

        btn_cancel = (Button) findViewById(R.id.dialog_btn_cancel);
        btn_delete = (Button) findViewById(R.id.dialog_btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}