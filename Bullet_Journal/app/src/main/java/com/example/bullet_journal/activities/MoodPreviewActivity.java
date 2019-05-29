package com.example.bullet_journal.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bullet_journal.R;
import com.example.bullet_journal.RootActivity;
import com.example.bullet_journal.adapters.MoodDisplayAdapter;
import com.example.bullet_journal.model.Mood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MoodPreviewActivity extends RootActivity {

    private ListView listView;
    private MoodDisplayAdapter moodsAdapter;
    private long dateMillis;

    private List<Mood> moods;

    private FirebaseFirestore firestore;
    private FirebaseAuth fAuth;
    private CollectionReference moodsCollectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_mood_preview);

        Bundle bundle = getIntent().getExtras();
        dateMillis = bundle.getLong("date");

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        moodsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day").document(""+dateMillis).collection("Mood");

        moods = new ArrayList<>();

        moodsAdapter = new MoodDisplayAdapter(this, moods);
        listView = (ListView) findViewById(R.id.moods_preview_list);
        listView.setAdapter(moodsAdapter);

        fetchMoods();
    }

    private void fetchMoods(){
        moodsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    moods.clear();
                    if(task.getResult().size() > 0){
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Mood tempMood = snapshot.toObject(Mood.class);
                            moods.add(tempMood);
                        }
                        Log.i("IMAAAAAA", "KOMADAAAAAAAA "+moods.size());
                        moodsAdapter.notifyDataSetChanged();
                    }else{
                        Log.i("NEMAAAAAAAAAAAAA", "KOMADAAAAAAAA "+moods.size()+" "+dateMillis);
                    }
                }else{
                    Toast.makeText(MoodPreviewActivity.this, R.string.basic_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
