package com.example.bullet_journal.model;

import java.time.ZonedDateTime;

public class MonthlyBudget {

    private String currentMonth;
    private Double amount;
    private Double balance;

    public MonthlyBudget() {
    }

    public MonthlyBudget(String currentMonth, Double amount, Double balance) {
        this.currentMonth = currentMonth;
        this.amount = amount;
        this.balance = 0.0;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
