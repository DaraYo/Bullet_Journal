package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.MonthlyBudget;

import java.util.List;

@Dao
public interface MonthlyBudgetDao {

    @Query("SELECT * FROM monthly_buget WHERE id=:id")
    MonthlyBudget get(Long id);

    @Query("SELECT * FROM monthly_buget")
    List<MonthlyBudget> getAll();

    @Query("SELECT * FROM monthly_buget WHERE month=:month AND year=:year")
    MonthlyBudget getByUserAndDate(int month, int year);

    @Insert
    long insert(MonthlyBudget monthlyBudget);

    @Delete
    void delete(MonthlyBudget monthlyBudget);

    @Update
    void update(MonthlyBudget monthlyBudget);
}
