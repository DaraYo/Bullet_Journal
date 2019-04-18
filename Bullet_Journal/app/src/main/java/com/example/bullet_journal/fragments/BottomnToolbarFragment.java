package com.example.bullet_journal.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bullet_journal.R;
import com.example.bullet_journal.activities.DiaryActivity;
import com.example.bullet_journal.activities.MoodTrackerActivity;
import com.example.bullet_journal.activities.RatingActivity;
import com.example.bullet_journal.activities.TasksAndEventsActivity;
import com.example.bullet_journal.activities.WalletActivity;

public class BottomnToolbarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomn_toolbar_fragment, container, false);

        ImageButton moodButton = (ImageButton) view.findViewById(R.id.btn_mood);
        moodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), MoodTrackerActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        ImageButton taskButton = (ImageButton) view.findViewById(R.id.btn_tasks);
        taskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), TasksAndEventsActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        ImageButton diaryButton = (ImageButton) view.findViewById(R.id.btn_diary);
        diaryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), DiaryActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        ImageButton ratingsButton = (ImageButton) view.findViewById(R.id.btn_rating);
        ratingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), RatingActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        ImageButton spendingButton = (ImageButton) view.findViewById(R.id.btn_money);
        spendingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), WalletActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        return view;
    }
}
