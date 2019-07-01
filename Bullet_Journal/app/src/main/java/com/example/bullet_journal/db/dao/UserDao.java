package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE firestore_id=:firestore_id")
    User getByFirestoreId(String firestore_id);

    @Insert
    long insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);
}
