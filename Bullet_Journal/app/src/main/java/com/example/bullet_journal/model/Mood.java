package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mood", foreignKeys = @ForeignKey(entity = Day.class,
        parentColumns = "id",
        childColumns = "day_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("day_id")})
public class Mood implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "day_id")
    private Long dayId;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "synced")
    private boolean synced;

    @ColumnInfo(name = "deleted")
    private boolean deleted;

    public Mood() {
    }

    @Ignore
    public Mood(Long id, String firestoreId, Long dayId, long date, int rating, String description, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.dayId = dayId;
        this.date = date;
        this.rating = rating;
        this.description = description;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
