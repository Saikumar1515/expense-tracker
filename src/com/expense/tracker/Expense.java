package com.expense.tracker;

import java.time.LocalDate;

class Expense {
	private static int counter = 1;
	private final int id;
	private String description;
	private double amount;
	private LocalDate date;
	private String category;

	public Expense(String description, double amount, String category) {
		this.id = counter++;
		this.description = description;
		this.amount = amount;
		this.date = LocalDate.now();
		this.category = category;
	}
	
	public String getCategory() {
        return category;
    }
	
	public void setCategory(String category) {
        this.category = category;
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
        return description + " - ₹" + amount + " [" + category + "] (" + date + ")";
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
