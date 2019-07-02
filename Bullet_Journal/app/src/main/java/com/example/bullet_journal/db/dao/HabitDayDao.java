package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.HabitDay;
import com.example.bullet_journal.model.Rating;

import java.util.List;

@Dao
public interface HabitDayDao {

    @Query("SELECT * FROM habit_day WHERE id=:id")
    HabitDay get(Long id);

    @Query("SELECT * FROM habit_day WHERE day_id=:id")
    List<HabitDay> getAllHabitsDayByDay(Long id);

    @Query("SELECT * FROM habit_day WHERE habit_id=:id")
    List<HabitDay> getAllHabitsDayByHabit(Long id);

    @Query("SELECT * FROM habit_day WHERE habit_id=:id and day_id=:id2")
    List<HabitDay> getAllHabitsDayByHabitAndDay(Long id, Long id2);

    @Query("SELECT * FROM habit_day WHERE firestore_id IS NULL AND synced = 0")
    List<HabitDay> getAllForInsert();

    @Query("SELECT * FROM habit_day WHERE firestore_id IS NOT NULL AND synced = 0")
    List<HabitDay> getAllForUpdate();

    @Query("SELECT * FROM habit_day WHERE deleted = 1")
    List<HabitDay> getAllForDelete();

    @Query("SELECT * FROM habit_day")
    List<HabitDay> getAll();

    @Insert
    long insert(HabitDay habit_day);

    @Delete
    void delete(HabitDay habit_day);

    @Update
    void update(HabitDay habit_day);

}
