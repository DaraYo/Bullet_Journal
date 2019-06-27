package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reminder", foreignKeys = {
        @ForeignKey(entity = Habit.class,
                parentColumns = "id",
                childColumns = "habit_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "task_id",
                onDelete = ForeignKey.CASCADE)
}, indices = {@Index("habit_id"), @Index("task_id")})
public class Reminder implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "status")
    private boolean status;

    @ColumnInfo(name = "habit_id")
    private Long habitId;

    @ColumnInfo(name = "task_id")
    private Long taskId;

    @ColumnInfo(name = "synced")
    private boolean synced;

    public Reminder() {
    }

    @Ignore
    public Reminder(Long id, String firestoreId, String name, long date, boolean status, Long habitId, Long taskId, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.name = name;
        this.date = date;
        this.status = status;
        this.habitId = habitId;
        this.taskId = taskId;
        this.synced = synced;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
