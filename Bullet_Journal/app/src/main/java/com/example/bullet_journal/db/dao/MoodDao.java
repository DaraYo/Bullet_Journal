package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Mood;

import java.util.List;

@Dao
public interface MoodDao {

    @Query("SELECT * FROM mood WHERE id=:id")
    Mood get(Long id);

    @Query("SELECT * FROM mood WHERE day_id=:id")
    List<Mood> getAllMoodsForDay(Long id);

    @Query("SELECT * FROM mood")
    List<Mood> getAll();

    @Insert
    long insert(Mood mood);

    @Delete
    void delete(Mood mood);

    @Update
    void update(Mood mood);
}
