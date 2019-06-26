package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "diary_image", foreignKeys = @ForeignKey(entity = Day.class,
        parentColumns = "id",
        childColumns = "day_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("day_id")})
public class DiaryImage implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "day_id")
    private Long dayId;

    @ColumnInfo(name = "synced")
    private boolean synced;

    public DiaryImage(){

    }

    @Ignore
    public DiaryImage(Long id, String firestoreId, String path, Long dayId, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.path = path;
        this.dayId = dayId;
        this.synced = synced;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
