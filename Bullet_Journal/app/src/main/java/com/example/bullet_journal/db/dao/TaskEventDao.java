package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Mood;
import com.example.bullet_journal.model.Task;

import java.util.List;

@Dao
public interface TaskEventDao {

    @Query("SELECT * FROM task_event WHERE id=:id")
    Task get(Long id);

    @Query("SELECT * FROM task_event WHERE day_id=:id AND type='TASK'")
    List<Mood> getAllTasksForDay(Long id);

    @Query("SELECT * FROM task_event WHERE day_id=:id AND type='EVENT'")
    List<Mood> getAllEventsForDay(Long id);

    @Query("SELECT * FROM task_event")
    List<Mood> getAll();

    @Insert
    long insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);


}
