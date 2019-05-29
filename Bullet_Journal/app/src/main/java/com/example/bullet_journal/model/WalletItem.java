package com.example.bullet_journal.model;

import com.example.bullet_journal.enums.WalletItemType;

public class WalletItem {

    private String name;
    private Double amount;
    private WalletItemType type;

    public WalletItem() {
    }

    public WalletItem(String name, Double amount, WalletItemType type) {
        this.name = name;
        this.amount = amount;
        this.type = type;
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
