package com.expense.tracker;

public interface ExpenseService {

	void addExpense();

	void viewExpenses();

	void deleteExpense();

	void viewSummaryAll();

	void viewSummaryByMonth();

	void updateExpense();

	void filterByCategory();

	void exportToCSV();

	void setMonthlyBudget();
}
