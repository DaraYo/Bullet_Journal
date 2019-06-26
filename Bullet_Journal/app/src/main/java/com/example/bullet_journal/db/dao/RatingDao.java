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

    @Query("SELECT * FROM rating ORDER BY date DESC")
    List<Rating> getAll();

    @Insert
    long insert(Rating rating);

    @Delete
    void delete(Rating rating);

    @Update
    void update(Rating rating);

}
