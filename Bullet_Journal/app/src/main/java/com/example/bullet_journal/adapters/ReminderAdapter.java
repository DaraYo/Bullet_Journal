package com.example.bullet_journal.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.EventActivity;
import com.example.bullet_journal.activities.TaskActivity;
import com.example.bullet_journal.activities.WalletActivity;
import com.example.bullet_journal.dialogs.AddBudgetDialog;
import com.example.bullet_journal.dialogs.DeleteReminderDialog;
import com.example.bullet_journal.enums.TaskType;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private String choosenDate = "";
    private Context context;

    public ReminderAdapter(Context context, List<Reminder> objects) {
        super(context, R.layout.reminder_adapter, objects);
        this.context = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



            View view = LayoutInflater.from(getContext()).inflate(R.layout.reminder_adapter, parent, false);

            final Reminder reminderObj = getItem(position);

            TextView reminderTitle = view.findViewById(R.id.reminder);
            reminderTitle.setText(reminderObj.getName());

            view.setOnLongClickListener(new View.OnLongClickListener() {
                                            public boolean onLongClick(View arg0) {
                                                final Dialog dialog = new DeleteReminderDialog(context, reminderObj.getName());
                                                dialog.show();
                                                return false;
                                            }
                                        }
            );


        TextView reminderDate = view.findViewById(R.id.reminder_time);

            choosenDate = CalendarCalculationsUtils.setCurrentDate("");

//            DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMM HH:mm");

//            Date date = null;
//            try {
//                date = (Date) originalFormat.parse(reminderObj.getDate());
//            } catch (P`arseException e) {
//                e.printStackTrace();
//            }

            choosenDate = targetFormat.format(reminderObj.getDate());
            reminderDate.setText(choosenDate);

        return view;

    }
}