package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Day;

import java.util.List;

@Dao
public interface DayDao {

    @Query("SELECT * FROM day WHERE id=:id")
    Day get(Long id);

    @Query("SELECT * FROM day")
    List<Day> getAll();

    @Query("SELECT * FROM day WHERE date=:date")
    Day getByDate(long date);

    @Query("SELECT * FROM day WHERE date >= :startDate AND date <= :endDate")
    List<Day> getDaysBetween(long startDate, long endDate);

    @Query("SELECT * FROM day WHERE firestore_id IS NULL AND synced = 0")
    List<Day> getAllForInsert();

    @Query("SELECT * FROM day WHERE firestore_id IS NOT NULL AND synced = 0")
    List<Day> getAllForUpdate();

    @Insert
    long insert(Day day);

    @Delete
    void delete(Day day);

    @Update
    void update(Day day);
}
