package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Rating;

import java.util.List;

@Dao
public interface RatingDao {

    @Query("SELECT * FROM rating WHERE id=:id")
    Rating get(Long id);

    @Query("SELECT * FROM rating WHERE deleted = 0 ORDER BY date DESC")
    List<Rating> getAll();

    @Query("SELECT * FROM rating WHERE firestore_id IS NULL AND synced = 0")
    List<Rating> getAllForInsert();

    @Query("SELECT * FROM rating WHERE firestore_id IS NOT NULL AND synced = 0")
    List<Rating> getAllForUpdate();

    @Query("SELECT * FROM rating WHERE deleted = 1")
    List<Rating> getAllForDelete();

    @Insert
    long insert(Rating rating);

    @Delete
    void delete(Rating rating);

    @Update
    void update(Rating rating);

}
