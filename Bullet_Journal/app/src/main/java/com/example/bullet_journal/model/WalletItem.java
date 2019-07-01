package com.example.bullet_journal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.bullet_journal.enums.WalletItemType;

import java.io.Serializable;

@Entity(tableName = "wallet_item", foreignKeys = @ForeignKey(entity = MonthlyBudget.class,
        parentColumns = "id",
        childColumns = "wallet_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("wallet_id")})
public class WalletItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "firestore_id")
    private String firestoreId;

    // Monthly budget entity
    @ColumnInfo(name = "wallet_id")
    private Long walletId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "amount")
    private Double amount;

    @ColumnInfo(name = "type")
    private WalletItemType type;

    public WalletItem() {
    }

    @Ignore
    public WalletItem(Long id, String firestoreId, Long walletId, String name, Double amount, WalletItemType type) {
        this.id = id;
        this.firestoreId = firestoreId;
        this.walletId = walletId;
        this.name = name;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public WalletItemType getType() {
        return type;
    }

    public void setType(WalletItemType type) {
        this.type = type;
    }
}
