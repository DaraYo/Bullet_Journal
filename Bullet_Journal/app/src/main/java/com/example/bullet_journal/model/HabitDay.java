package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "habit_day", foreignKeys = {
        @ForeignKey(entity = Day.class,
                parentColumns = "id",
                childColumns = "day_id"),
        @ForeignKey(entity = Habit.class,
                parentColumns = "id",
                childColumns = "habit_id")
        },
        indices = {@Index("day_id"), @Index("habit_id")})
public class HabitDay {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "day_id")
    private Long dayId;

    @ColumnInfo(name = "habit_id")
    private Long habitId;

    @ColumnInfo(name = "synced")
    private boolean synced;
}
