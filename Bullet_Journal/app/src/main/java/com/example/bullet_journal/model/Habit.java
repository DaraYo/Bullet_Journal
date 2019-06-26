package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "habit", foreignKeys = @ForeignKey(entity = Day.class,
        parentColumns = "id",
        childColumns = "day_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("day_id")})
public class Habit implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "day_id")
    private Long dayId;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "status")
    private boolean status;

    @ColumnInfo(name = "synced")
    private boolean synced;

    public Habit() {
    }

    @Ignore
    public Habit(Long id, String firestoreId, String title, String text, Long dayId, long date, boolean status, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.title = title;
        this.text = text;
        this.dayId = dayId;
        this.date = date;
        this.status = status;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
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

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
