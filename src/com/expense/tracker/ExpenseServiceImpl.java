package com.expense.tracker;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseServiceImpl implements ExpenseService {

	private final List<Expense> expenses = new ArrayList<>();
	private static final Map<Month, Double> monthlyBudgets = new HashMap<>();

	@Override
	public void addExpense(Scanner scanner) {

		System.out.println("Enter expense description: ");
		String description = scanner.nextLine().trim();

		System.out.print("Enter expense amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();

		System.out.println("Enter category (e.g., Food, Travel, Bills, Shopping): ");
		String category = scanner.nextLine();

		Expense newExpenses = new Expense(description, amount, category);
		expenses.add(newExpenses);
		System.out.println("Expense added successfully!");

	}

	@Override
	public void viewExpenses() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses added yet");
			return;
		}

		System.out.println("\n================================================================");
		System.out.println(
				String.format("%-5s %-12s %-20s %-15s %-10s", "ID", "Date", "Description", "Category", "Amount"));
		System.out.println("==================================================================");

		int id = 1;
		for (Expense e : expenses) {
			System.out.println(String.format("%-5d %-12s %-20s %-15s Rs.%.2f", id++, e.getDate(), e.getDescription(),
					e.getCategory(), e.getAmount()));
		}

		System.out.println("==================================================================");
	}

	@Override
	public void deleteExpense(Scanner scanner) {
		if (expenses.isEmpty()) {
			System.out.println("No expenses to delete.");
			return;
		}

		System.out.println("\nYour Expenses:");
		for (int i = 0; i < expenses.size(); i++) {
			Expense e = expenses.get(i);
			System.out.printf("%d. %s 	| Rs.%.2f 	| %s%n", i + 1, e.getDescription(), e.getAmount(), e.getDate());
		}

		System.out.print("\nEnter the id of the expense you want to delete: ");
		int choice;
		try {
			choice = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid number.");
			return;
		}

		if (choice < 1 || choice > expenses.size()) {
			System.out.println("Invalid selection. Please choose a number between 1 and " + expenses.size());
			return;
		}

		Expense removed = expenses.remove(choice - 1);
		System.out.printf("Expense '%s' deleted successfully!%n", removed.getDescription());
	}

	@Override
	public void viewSummaryAll() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses recorded.");
			return;
		}
		double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
		double average = total / expenses.size();
		System.out.printf("Total expenses: Rs.%.2f\n", total);
		System.out.printf("Average expense:Rs.%.2f\n", average);
	}

	@Override
	public void viewSummaryByMonth(Scanner scanner) {
		if (expenses.isEmpty()) {
			System.out.println("No expenses recorded.");
			return;
		}

		System.out.print("Enter month number (1 to 12): ");
		int monthNumber = scanner.nextInt();
		scanner.nextLine();

		if (monthNumber < 1 || monthNumber > 12) {
			System.out.println("Invalid month number.");
			return;
		}

		Month month = Month.of(monthNumber);
		int currentYear = LocalDate.now().getYear();

		List<Expense> monthly = expenses.stream()
				.filter(e -> e.getDate().getMonth() == month && e.getDate().getYear() == currentYear)
				.collect(Collectors.toList());

		if (monthly.isEmpty()) {
			System.out.printf("No expenses found for %s %d%n", month, currentYear);
			return;
		}

		double total = monthly.stream().mapToDouble(Expense::getAmount).sum();
		System.out.printf("Total expenses for %s: Rs.%.2f", month, total);
	}

	@Override
	public void updateExpense(Scanner scanner) {
		if (expenses.isEmpty()) {
			System.out.println(" No expenses to update.");
			return;
		}

		System.out.print("Enter the description of the expense you want to update: ");
		String searchDescription = scanner.nextLine().trim();

		List<Expense> matched = new ArrayList<>();
		for (Expense e : expenses) {
			if (e.getDescription().equalsIgnoreCase(searchDescription)) {
				matched.add(e);
			}
		}

		if (matched.isEmpty()) {
			System.out.println(" No expenses found with description: " + searchDescription);
			return;
		}

		Expense expenseToUpdate;

		if (matched.size() == 1) {
			expenseToUpdate = matched.get(0);
		} else {
			System.out.println("\nMultiple expenses found with that description:\n");
			System.out.println("--------------------------------------------------------------");
			System.out.printf("%-5s %-12s %-20s %-15s %-10s%n", "ID", "Date", "Description", "Category", "Amount");
			System.out.println("--------------------------------------------------------------");

			for (int i = 0; i < matched.size(); i++) {
				Expense e = matched.get(i);
				System.out.printf("%-5d %-12s %-20s %-15s  Rs.%.2f\n", (i + 1), e.getDate(), e.getDescription(),
						e.getCategory(), e.getAmount());
			}

			System.out.println("--------------------------------------------------------------");
			System.out.print("Enter the ID of the expense you want to update: ");
			while (!scanner.hasNextInt()) {
				System.out.print(" Please enter a valid numeric ID: ");
				scanner.next();
			}

			int id = scanner.nextInt();
			scanner.nextLine();

			if (id < 1 || id > matched.size()) {
				System.out.println("Invalid ID selected. No expense updated.");
				return;
			}
			expenseToUpdate = matched.get(id - 1);
		}

		System.out.print("Enter new description (leave blank to keep same): ");
		String newDesc = scanner.nextLine().trim();

		System.out.print("Enter new amount (or -1 to keep same): ");
		while (!scanner.hasNextDouble()) {
			System.out.print(" Please enter a valid number for amount: ");
			scanner.next();
		}
		double newAmount = scanner.nextDouble();
		scanner.nextLine();

		System.out.print("Enter new category (leave blank to keep same): ");
		String newCategory = scanner.nextLine().trim();

		if (!newDesc.isEmpty()) {
			expenseToUpdate.setDescription(newDesc);
		}
		if (newAmount >= 0) {
			expenseToUpdate.setAmount(newAmount);
		}
		if (!newCategory.isEmpty()) {
			expenseToUpdate.setCategory(newCategory);
		}

		System.out.println("Expense updated successfully!");
	}

	@Override
	public void filterByCategory(Scanner scanner) {
		if (expenses.isEmpty()) {
			System.out.println("No expenses available.");
			return;
		}

		System.out.print("Enter category to filter by: ");
		String category = scanner.nextLine().trim();

		List<Expense> filtered = expenses.stream().filter(e -> e.getCategory().equalsIgnoreCase(category)).toList();

		if (filtered.isEmpty()) {
			System.out.println("No expenses found in category '" + category + "'.");
			return;
		}

		System.out.println("\nExpenses in category '" + category + "':\n");
		System.out.println("--------------------------------------------------------------");
		System.out.printf("%-5s %-12s %-20s %-15s %-10s%n", "ID", "Date", "Description", "Category", "Amount");
		System.out.println("--------------------------------------------------------------");

		for (Expense e : filtered) {
			System.out.printf("%-5d %-12s %-20s %-15s Rs.%-10.2f%n", e.getId(), e.getDate(), e.getDescription(),
					e.getCategory(), e.getAmount());
		}

		System.out.println("--------------------------------------------------------------");

	}

	@Override
	public void setMonthlyBudget(Scanner scanner) {
		System.out.println("Enter month number (1-12) to set budget for: ");
		int monthNum = scanner.nextInt();
		scanner.nextLine();
		Month month = Month.of(monthNum);

		System.out.println("Enter budget amount: ");
		double budget = scanner.nextDouble();
		scanner.nextLine();

		monthlyBudgets.put(month, budget);
		System.out.println("Budget set for " + month + ": Rs." + budget);

	}

	@Override
	public void exportToCSV() {
		if (expenses.isEmpty()) {
			System.out.println(" No expenses to export.");
			return;
		}

		try (FileWriter writer = new FileWriter("expenses.csv")) {
			writer.write("Description,Amount,Category,Date\\n");
			for (Expense e : expenses) {
				writer.write(String.format("%s,Rs.%.2f,%s,%s\n", e.getDescription(), e.getAmount(), e.getCategory(),
						e.getDate()));
			}
			System.out.println("Expenses exported successfully to 'expenses.csv");
		} catch (IOException e) {
			System.out.println("Error exporting to CSV: " + e.getMessage());
		}
	}

	@Override
	public void checkBudgetExceeded(Expense expense) {
		Month month = expense.getDate().getMonth();
		if (monthlyBudgets.containsKey(month)) {
			double budget = monthlyBudgets.get(month);
			double totalThisMonth = expenses.stream().filter(e -> e.getDate().getMonth() == month)
					.mapToDouble(Expense::getAmount).sum();

			if (totalThisMonth > budget) {
				System.out.println("Budget exceeded for " + month + "! (Rs" + totalThisMonth + " / Rs" + budget + ")");
			}
		}

	}

}
