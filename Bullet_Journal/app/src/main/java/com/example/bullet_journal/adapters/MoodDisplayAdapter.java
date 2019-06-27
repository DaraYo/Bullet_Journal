package com.example.bullet_journal.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.bullet_journal.R;
import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.async.CalculateNewAverageMoodAsyncTask;
import com.example.bullet_journal.async.DeleteMoodAsyncTask;
import com.example.bullet_journal.dialogs.AddEditMoodDialog;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Mood;

import java.util.List;

public class MoodDisplayAdapter extends ArrayAdapter<Mood> {

    private Context context;

    public MoodDisplayAdapter(Context context, List<Mood> objects) {
        super(context, R.layout.mood_preview_adapter, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.mood_preview_adapter, parent, false);

        final Mood moodObj = getItem(position);

        ImageButton emojiView = view.findViewById(R.id.mood_adapter_img);
        TextView textView = view.findViewById(R.id.mood_adapter_img_str);

        switch (moodObj.getRating()) {
            case 5 : {
                emojiView.setImageResource(R.drawable.very_happy);
                textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moodRed));
                textView.setText(R.string.mood_awesome);
                break;
            }

            case 4 : {
                emojiView.setImageResource(R.drawable.happy);
                textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moodOrange));
                textView.setText(R.string.mood_good);
                break;
            }

            case 3 : {
                emojiView.setImageResource(R.drawable.ok);
                textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moodYellow));
                textView.setText(R.string.mood_average);
                break;
            }

            case 2 : {
                emojiView.setImageResource(R.drawable.bad);
                textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moodGreen));
                textView.setText(R.string.mood_bad);
                break;
            }

            case 1 : {
                emojiView.setImageResource(R.drawable.sad);
                textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.moodPurple));
                textView.setText(R.string.mood_terrible);
                break;
            }
        }

        TextView description = view.findViewById(R.id.mood_adapter_description);
        description.setText(moodObj.getDescription());

        TextView date = view.findViewById(R.id.mood_adapter_date);
        date.setText(CalendarCalculationsUtils.dateMillisToStringDateAndTime(moodObj.getDate()));

        ImageButton editBtn = view.findViewById(R.id.mood_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new AddEditMoodDialog(context, moodObj.getDate(), moodObj);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        calculateNewAverage(moodObj);
                        notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });

        ImageButton deleteBtn = view.findViewById(R.id.mood_delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AsyncTask<Mood, Void, Boolean> deleteMoodAsyncTask = new DeleteMoodAsyncTask(new AsyncResponse<Boolean>(){
                    @Override
                    public void taskFinished(Boolean retVal) {
                        if(retVal){
                            calculateNewAverage(moodObj);
                            remove(moodObj);
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute(moodObj);
            }
        });

        return view;
    }

    private void calculateNewAverage(Mood moodObj) {

        AsyncTask<Mood, Void, Boolean> calculateNewAverageMoodAsyncTask = new CalculateNewAverageMoodAsyncTask(new AsyncResponse<Boolean>() {
            @Override
            public void taskFinished(Boolean retVal) {
                if(retVal){
                    Toast.makeText(context, R.string.mood_saved, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, R.string.basic_error, Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(moodObj);
    }

}
