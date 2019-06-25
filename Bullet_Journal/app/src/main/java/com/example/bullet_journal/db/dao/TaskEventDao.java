package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.Task;

import java.util.List;

@Dao
public interface TaskEventDao {

    @Query("SELECT * FROM task_event WHERE id=:id")
    Task get(Long id);

    @Query("SELECT * FROM task_event WHERE day_id=:id AND type='TASK'")
    List<Task> getAllTasksForDay(Long id);

    @Query("SELECT * FROM task_event WHERE day_id=:id AND type='EVENT'")
    List<Task> getAllEventsForDay(Long id);

    @Query("SELECT * FROM task_event")
    List<Task> getAll();

    @Insert
    long insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);


}
