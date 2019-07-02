package com.example.bullet_journal.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bullet_journal.async.AsyncResponse;
import com.example.bullet_journal.db.DatabaseClient;
import com.example.bullet_journal.db.MainDatabase;
import com.example.bullet_journal.helpClasses.FirebaseUserDTO;
import com.example.bullet_journal.helpClasses.PreferencesHelper;
import com.example.bullet_journal.model.Day;
import com.example.bullet_journal.model.Habit;
import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.model.Mood;
import com.example.bullet_journal.model.Rating;
import com.example.bullet_journal.model.Reminder;
import com.example.bullet_journal.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PullFromFirestoreAsyncTask extends AsyncTask<Void, Void, User> {

    public AsyncResponse delegate = null;
    private Context context;
    private MainDatabase database = DatabaseClient.getInstance(null).getDatabase();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference dayCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Day");
    private CollectionReference ratingsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Ratings");
    private CollectionReference habitsCollectionRef = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Habits");

    private static boolean isSuccessful = true;

    public PullFromFirestoreAsyncTask(Context context, AsyncResponse delegate){
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected User doInBackground(Void... voids) {
        User currentUser = null;
        database.clearAllTables();

        try{
            Task<DocumentSnapshot> getUserTask = firestore.collection("Users").document(fAuth.getCurrentUser().getUid()).get();
            FirebaseUserDTO currentUserData = Tasks.await(getUserTask).toObject(FirebaseUserDTO.class);

            currentUser= new User();
            currentUser.setFirestoreId(currentUserData.getId());
            currentUser.setFirstName(currentUserData.getFirstName());
            currentUser.setLastName(currentUserData.getLastName());
            currentUser.setEmail(currentUserData.getEmail());
            currentUser.setPassword(currentUserData.getPassword());
            database.getUserDao().insert(currentUser);

            fetchAllData();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return currentUser;
    }

    @Override
    protected void onPostExecute(User user) {

        delegate.taskFinished(user);
    }

    private void fetchAllData(){

        fetchAllDays();
        fetchAllRatings();
    }

    private void fetchAllDays(){
        try {
            Task<QuerySnapshot> getDaysTask = dayCollectionRef.get();
            QuerySnapshot daysResult = Tasks.await(getDaysTask);

            for(QueryDocumentSnapshot snapshot : daysResult){
                Day day = snapshot.toObject(Day.class);
                database.getDayDao().insert(day);
                fetchAllMoodsForDay(day);
                fetchAllTasksForDay(day);
            }

            Log.i("DAYS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("DAYS FETCH", "FAILED");
        }
    }

    private void fetchAllRatings(){
        try {
            Task<QuerySnapshot> getRatingsTask = ratingsCollectionRef.get();
            QuerySnapshot ratingsResult = Tasks.await(getRatingsTask);

            for(QueryDocumentSnapshot snapshot : ratingsResult){
                Rating rating = snapshot.toObject(Rating.class);
                database.getRatingDao().insert(rating);
            }

            Log.i("RATINGS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("RATINGS FETCH", "FAILED");
        }
    }

    private void fetchAllMoodsForDay(Day day){
        try {
            Task<QuerySnapshot> getMoodsTask = dayCollectionRef.document(day.getFirestoreId()).collection("Moods").get();
            QuerySnapshot moodsResult = Tasks.await(getMoodsTask);

            for(QueryDocumentSnapshot snapshot : moodsResult){
                Mood mood = snapshot.toObject(Mood.class);
                mood.setDayId(day.getId());
                database.getMoodDao().insert(mood);
            }

            Log.i("MOODS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("MOODS FETCH", "FAILED");
        }
    }

    private void fetchAllTasksForDay(Day day){
        try {
            Task<QuerySnapshot> getTasksTask = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks").get();
            QuerySnapshot tasksResult = Tasks.await(getTasksTask);

            for(QueryDocumentSnapshot snapshot : tasksResult){
                com.example.bullet_journal.model.Task task = snapshot.toObject(com.example.bullet_journal.model.Task.class);
                task.setDayId(day.getId());
                database.getTaskEventDao().insert(task);
                fetchAllRemindersForTasks(day, task);
            }

            Log.i("TASKS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("TASKS FETCH", "FAILED");
        }
    }

    private void fetchAllHabits(){
        try {
            Task<QuerySnapshot> getHabitsTask = habitsCollectionRef.get();
            QuerySnapshot habitsResult = Tasks.await(getHabitsTask);

            for(QueryDocumentSnapshot snapshot : habitsResult){
                Habit habit = snapshot.toObject(Habit.class);
                database.getHabitDao().insert(habit);
                fetchAllRemindersForHabit(habit);
                fetchAllDaysForHabit(habit);
            }

            Log.i("HABITS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("HABITS FETCH", "FAILED");
        }
    }

    private void fetchAllRemindersForTasks(Day day, com.example.bullet_journal.model.Task task){

        try {
            Task<QuerySnapshot> getRemindersTask = dayCollectionRef.document(day.getFirestoreId()).collection("Tasks").document(task.getFirestoreId()).collection("Reminders").get();
            QuerySnapshot remindersResult = Tasks.await(getRemindersTask);

            for(QueryDocumentSnapshot snapshot : remindersResult){
                Reminder reminder = snapshot.toObject(Reminder.class);
                reminder.setTaskId(task.getId());
                database.getReminderDao().insert(reminder);
            }

            Log.i("REMINDERS T FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("REMINDERS T FETCH", "FAILED");
        }
    }

    private void fetchAllRemindersForHabit(Habit habit){

        try {
            Task<QuerySnapshot> getRemindersTask = habitsCollectionRef.document(habit.getFirestoreId()).collection("Reminders").get();
            QuerySnapshot remindersResult = Tasks.await(getRemindersTask);

            for(QueryDocumentSnapshot snapshot : remindersResult){
                Reminder reminder = snapshot.toObject(Reminder.class);
                reminder.setHabitId(habit.getId());
                database.getReminderDao().insert(reminder);
            }

            Log.i("REMINDERS H FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("REMINDERS H  FETCH", "FAILED");
        }
    }

    private void fetchAllDaysForHabit(Habit habit){

        try {
            Task<QuerySnapshot> getHabitDaysTask = habitsCollectionRef.document(habit.getFirestoreId()).collection("HabitDay").get();
            QuerySnapshot habitDaysResult = Tasks.await(getHabitDaysTask);

            for(QueryDocumentSnapshot snapshot : habitDaysResult){
                HabitDay habDay = snapshot.toObject(HabitDay.class);
                habDay.setHabitId(habit.getId());
                database.getHabitDayDao().insert(habDay);
            }

            Log.i("HABDAYS FETCH", "SUCCESS");
        }catch (Exception e){
            Log.i("HABDAYS H  FETCH", "FAILED");
        }
    }


}
