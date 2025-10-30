package com.expense.tracker;

import java.time.LocalDate;

class Expense {
	private static int counter = 1;
	private final int id;
	private String description;
	private double amount;
	private LocalDate date;

	public Expense(String description, double amount) {
		this.id = counter++;
		this.description = description;
		this.amount = amount;
		this.date = LocalDate.now();
	}

	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public double getAmount() {
		return amount;
	}

	@Override
    public String toString() {
        return String.format("%d\t%s\t%s\t$%.2f", id, date, description, amount);
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
