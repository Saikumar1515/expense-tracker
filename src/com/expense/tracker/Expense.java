package com.expense.tracker;

class Expense {
	private String description;
	private double amount;

	public Expense(String description, double amount) {
		this.description = description;
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return description + " - ₹" + amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
