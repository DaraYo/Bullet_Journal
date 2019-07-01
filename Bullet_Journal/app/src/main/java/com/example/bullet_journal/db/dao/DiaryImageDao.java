package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.DiaryImage;

import java.util.List;

@Dao
public interface DiaryImageDao {
    @Query("SELECT * FROM diary_image WHERE id=:id")
    DiaryImage get(Long id);

    @Query("SELECT * FROM diary_image")
    List<DiaryImage> getAll();

    @Query("SELECT * FROM diary_image WHERE day_id=:dayId AND deleted = 0")
    List<DiaryImage> getByDiaryImagesId(long dayId);

    @Insert
    long insert(DiaryImage image);

    @Delete
    void delete(DiaryImage image);

    @Update
    void update(DiaryImage image);
}
