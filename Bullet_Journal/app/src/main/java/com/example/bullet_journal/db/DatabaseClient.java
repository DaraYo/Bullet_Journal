package com.example.bullet_journal.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context context;

    private static DatabaseClient instance;
    private volatile MainDatabase database;
    public static String DB_NAME = "bu_jo_db";

    private DatabaseClient(Context context, MainDatabase database){
        this.context = context;
        this.database = database;
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context, Room.databaseBuilder(context, MainDatabase.class, DB_NAME).fallbackToDestructiveMigration().build());
        }
        return instance;
    }

    public MainDatabase getDatabase(){
        return this.database;
    }

}
