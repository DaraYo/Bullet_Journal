package com.example.bullet_journal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bullet_journal.model.WalletItem;

import java.util.List;

@Dao
public interface WalletItemDao {

    @Query("SELECT * FROM wallet_item WHERE id=:id")
    WalletItem get(Long id);

    @Query("SELECT * FROM wallet_item WHERE wallet_id=:id")
    List<WalletItem> getAllItemsForMonthlyBudget(Long id);

    @Query("SELECT * FROM wallet_item")
    List<WalletItem> getAll();

    @Insert
    long insert(WalletItem walletItem);

    @Delete
    void delete(WalletItem walletItem);

    @Update
    void update(WalletItem walletItem);
}