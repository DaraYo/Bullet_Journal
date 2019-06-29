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

    @Query("SELECT * FROM reminder WHERE task_id=:id ORDER BY date")
    List<Reminder> getAllRemindersForTask(Long id);

    @Query("SELECT COUNT(*) FROM reminder WHERE task_id=:id")
    int getRemindersCountForTask(Long id);

    @Query("SELECT * FROM reminder WHERE habit_id=:id")
    List<Reminder> getAllRemindersForHabit(Long id);

    @Query("SELECT * FROM reminder")
    List<Reminder> getAll();

    @Query("SELECT * FROM reminder WHERE firestore_id IS NULL AND synced = 0")
    List<Reminder> getAllForInsert();

    @Query("SELECT * FROM reminder WHERE firestore_id IS NOT NULL AND synced = 0")
    List<Reminder> getAllForUpdate();

    @Insert
    long insert(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Update
    void update(Reminder reminder);
}
