package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "habit_day", foreignKeys = {
        @ForeignKey(entity = Day.class,
                parentColumns = "id",
                childColumns = "day_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Habit.class,
                parentColumns = "id",
                childColumns = "habit_id",
                onDelete = ForeignKey.CASCADE)
},
        indices = {@Index("day_id"), @Index("habit_id") })
public class HabitDay implements Serializable {

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

    @ColumnInfo(name = "synced")
    private boolean deleted;

    public HabitDay() {
    }

    @Ignore
    public HabitDay(Long id, String firestoreId, Long dayId, Long habitId, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.dayId=dayId;
        this.habitId=habitId;
        this.synced = synced;
        this.deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public Long getDayId() {
        return dayId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
