package com.expense.tracker;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

class Expense {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final int id;
    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    public Expense(String description, double amount, String category, LocalDate date) {
        this.id = COUNTER.getAndIncrement();
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = (date != null) ? date : LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%d. %s - Rs.%.2f [%s] (%s)", id, description, amount, category, date);
    }
}
