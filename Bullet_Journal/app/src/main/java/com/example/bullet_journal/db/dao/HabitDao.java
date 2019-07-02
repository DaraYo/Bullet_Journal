package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Habit;

import java.util.List;

@Dao
public interface HabitDao {

    @Query("SELECT * FROM habit WHERE id=:id")
    Habit get(Long id);

    @Query("SELECT * FROM habit WHERE user_id=:id")
    List<Habit> getAllHabitsForUser(Long id);

    @Query("SELECT * FROM habit")
    List<Habit> getAll();

    @Query("SELECT * FROM habit WHERE firestore_id IS NULL AND synced = 0")
    List<Habit> getAllForInsert();

    @Query("SELECT * FROM habit WHERE firestore_id IS NOT NULL AND synced = 0")
    List<Habit> getAllForUpdate();

    @Query("SELECT * FROM habit WHERE deleted = 1")
    List<Habit> getAllForDelete();

    @Insert
    long insert(Habit habit);

    @Delete
    void delete(Habit habit);

    @Update
    void update(Habit habit);

}
