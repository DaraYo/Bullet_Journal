package com.example.bullet_journal.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bullet_journal.R;
import com.example.bullet_journal.dialogs.AddEditMoodDialog;
import com.example.bullet_journal.helpClasses.CalendarCalculationsUtils;
import com.example.bullet_journal.model.Mood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MoodDisplayAdapter extends ArrayAdapter<Mood> {

    private Context context;
    private DocumentReference dayRef;


    public MoodDisplayAdapter(Context context, List<Mood> objects, DocumentReference dayRef) {
        super(context, R.layout.mood_preview_adapter, objects);
        this.context = context;
        this.dayRef = dayRef;
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
                        calculateNewAverage();
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
                dayRef.collection("Mood").document(moodObj.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            remove(moodObj);
                            calculateNewAverage();
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        return view;
    }

    private void calculateNewAverage(){
        dayRef.collection("Mood").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().size() > 0){
                        double sum = 1;
                        double num = 0;

                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Mood tempMood = snapshot.toObject(Mood.class);
                            sum = sum*tempMood.getRating();
                            num++;
                        }

                        dayRef.update("avgMood", sum/num);
                    }else{
                        dayRef.update("avgMood", 0);
                    }
                }
            }
        });
    }

}
