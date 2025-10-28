package com.expense.tracker;

import java.util.*;
import java.util.logging.*;

public class ExpenseTracker {
	private static List<Expense> expenses = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(ExpenseTracker.class.getName());

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		logger.log(Level.INFO, " Welcome to Expense Tracker");

		while (true) {
			logger.log(Level.INFO, "\nChoose an option:");
			logger.log(Level.INFO, "1. Add Expense");
			logger.log(Level.INFO, "2. View Expenses");
			logger.log(Level.INFO, "3. update Expenses");
			logger.log(Level.INFO, "4. Exit");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1 -> addExpense(scanner);
			case 2 -> viewExpenses();
			case 3 -> updateExpenses(scanner);
			case 4 -> {
				logger.log(Level.INFO, "Exiting... Goodbye!");
				return;
			}
			default -> logger.log(Level.INFO, "Invalid choice. Try again.");
			}
		}
	}

	private static void updateExpenses(Scanner scanner) {
		logger.log(Level.INFO, "Enter the description of the expense you want to update: ");
		String searchDescription = scanner.nextLine();

		List<Expense> matchingExpenses = new ArrayList<>();
		for (Expense e : expenses) {
			if (e.getDescription().equalsIgnoreCase(searchDescription)) {
				matchingExpenses.add(e);
			}
		}
		if (matchingExpenses.isEmpty()) {
			logger.log(Level.INFO,
					() -> "Expense with description " + searchDescription + " not found in your expenses.");
			return;
		}

		Expense expenseToUpdate;
		if (matchingExpenses.size() == 1) {
			expenseToUpdate = matchingExpenses.get(0);
		} else {
			logger.log(Level.INFO, "Multiple expenses found with same description: ");
			for (int i = 0; i < matchingExpenses.size(); i++) {
				logger.log(Level.INFO, (i + 1) + ". " + matchingExpenses.get(i).toString());
			}
			
			logger.log(Level.INFO, "Please choose one by typing its number (e.g., 1 or 2): ");
			int index = scanner.nextInt();
			scanner.nextLine();

			if (index < 1 || index > matchingExpenses.size()) {
				logger.log(Level.WARNING, "Invalid selection. Please enter a number between 1 and "+ matchingExpenses.size());
				return;
			}

			expenseToUpdate = matchingExpenses.get(index - 1);
		}
		
		logger.log(Level.INFO, "Enter new description: ");
		String newDescription = scanner.nextLine();

		logger.log(Level.INFO, "Enter new amount: ");
		double newAmount = scanner.nextDouble();
		scanner.nextLine();

		expenseToUpdate.setDescription(newDescription);
		expenseToUpdate.setAmount(newAmount);

		logger.log(Level.INFO, " Expenses updated successfully");
	}

	private static void addExpense(Scanner scanner) {
		logger.log(Level.INFO, "Enter expense description: ");
		String description = scanner.nextLine();

		logger.log(Level.INFO, "Enter expense amount: ");
		double amount = scanner.nextDouble();

		expenses.add(new Expense(description, amount));
		logger.log(Level.INFO, " Expense added successfully!");
	}

	private static void viewExpenses() {
		if (expenses.isEmpty()) {
			logger.log(Level.INFO, "No expenses added yet.");
		} else {
			logger.log(Level.INFO, "\nYour Expenses: ");
			for (Expense e : expenses) {
				logger.log(Level.INFO, () -> "expenses " + e);
			}
		}
	}
}
