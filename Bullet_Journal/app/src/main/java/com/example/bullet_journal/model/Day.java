package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "day", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("user_id")})
public class Day implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "user_id")
    private Long userId;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "avg_mood")
    private double avgMood;

    @ColumnInfo(name = "diary_input")
    private String diaryInput;

    @ColumnInfo(name = "location_title")
    private String locationTitle;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "synced")
    private boolean synced;

    public Day() { }

    @Ignore
    public Day(Long id, String firestoreId, Long userId, long date, double avgMood, String diaryInput, String locationTitle, Double latitude, Double longitude, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.userId = userId;
        this.date = date;
        this.avgMood = avgMood;
        this.diaryInput = diaryInput;
        this.locationTitle = locationTitle;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getAvgMood() {
        return avgMood;
    }

    public void setAvgMood(double avgMood) {
        this.avgMood = avgMood;
    }

    public String getDiaryInput() {
        return diaryInput;
    }

    public void setDiaryInput(String diaryInput) {
        this.diaryInput = diaryInput;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", firestoreId='" + firestoreId + '\'' +
                ", userId=" + userId +
                ", date=" + date +
                ", avgMood=" + avgMood +
                ", diaryInput='" + diaryInput + '\'' +
                ", synced=" + synced +
                '}';
    }
}
