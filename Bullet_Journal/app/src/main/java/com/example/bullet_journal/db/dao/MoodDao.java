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

    @Query("SELECT * FROM mood WHERE day_id=:id ORDER BY date")
    List<Mood> getAllMoodsForDay(Long id);

    @Query("SELECT * FROM mood")
    List<Mood> getAll();

    @Query("SELECT * FROM mood WHERE firestore_id IS NULL AND synced = 0")
    List<Mood> getAllForInsert();

    @Query("SELECT * FROM mood WHERE firestore_id IS NOT NULL AND synced = 0")
    List<Mood> getAllForUpdate();

    @Insert
    long insert(Mood mood);

    @Delete
    void delete(Mood mood);

    @Update
    void update(Mood mood);
}
