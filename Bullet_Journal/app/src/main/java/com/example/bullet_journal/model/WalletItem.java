package com.example.bullet_journal.model;

import java.util.Map;
import java.util.Date;

public class Spending {

    private String itemName;
    private Double itemPrice;

    public Spending() {
    }

    public Spending(String itemName, Double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
