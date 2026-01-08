package com.yourname.financetracker.model;

import java.time.LocalDate;

public abstract class FinancialRecord {

    protected final double amount;
    protected final LocalDate date;
    protected final String description;

    protected FinancialRecord(double amount, LocalDate date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
}