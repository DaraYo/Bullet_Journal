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

    @Insert
    long insert(Habit habit);

    @Delete
    void delete(Habit habit);

    @Update
    void update(Habit habit);

}
