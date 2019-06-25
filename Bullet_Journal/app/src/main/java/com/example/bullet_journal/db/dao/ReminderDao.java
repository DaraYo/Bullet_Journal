package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Query("SELECT * FROM reminder WHERE id=:id")
    Reminder get(Long id);

    @Query("SELECT * FROM reminder WHERE task_id=:id")
    List<Reminder> getAllRemindersForTask(Long id);

    @Query("SELECT * FROM reminder WHERE habit_id=:id")
    List<Reminder> getAllRemindersForHabit(Long id);

    @Query("SELECT * FROM reminder")
    List<Reminder> getAll();

    @Insert
    long insert(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Update
    void update(Reminder reminder);
}
