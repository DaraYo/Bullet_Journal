package com.example.bullet_journal.model;

public class MonthlyBudget {

    private String currentMonth;
    private Double balance;

    public MonthlyBudget() {
    }

    public MonthlyBudget(String currentMonth, Double amount, Double balance) {
        this.currentMonth = currentMonth;
        this.balance = 0.0;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
