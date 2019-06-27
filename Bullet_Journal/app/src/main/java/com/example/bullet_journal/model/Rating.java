package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.bullet_journal.enums.RatingCategory;

import java.io.Serializable;

@Entity(tableName = "rating", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("user_id")})
public class Rating implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "user_id")
    private Long userId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "category")
    private RatingCategory category;

    @ColumnInfo(name = "synced")
    private boolean synced;

    public Rating() {
    }

    @Ignore
    public Rating(Long id, String firestoreId, int rating, long date, Long userId, String title, String text, RatingCategory category, boolean synced) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.rating = rating;
        this.date = date;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.category = category;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public RatingCategory getCategory() {
        return category;
    }

    public void setCategory(RatingCategory category) {
        this.category = category;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
