package com.expense.tracker;

import java.util.Scanner;

public interface ExpenseService {

	void addExpense(Scanner scanner);
    void viewExpenses();
    void deleteExpense(Scanner scanner);
    void viewSummaryAll();
    void viewSummaryByMonth(Scanner scanner);
	void updateExpense(Scanner scanner);
	void filterByCategory(Scanner scanner);
	void exportToCSV();
	void setMonthlyBudget(Scanner scanner);
	void checkBudgetExceeded(Expense expense);
}
