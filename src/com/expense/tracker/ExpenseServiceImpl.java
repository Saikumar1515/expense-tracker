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
		System.out.println(" Expense added successfully!");

	}

	@Override
	public void viewExpenses() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses found.");
			return;
		}
		System.out.println("\nID\tDate\t\tDescription\tAmount");
		for (Expense e : expenses) {
			System.out.println(e);
		}
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
		System.out.printf("Total expenses: $%.2f%n", total);
		System.out.printf("Average expense: $%.2f", average);
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
		System.out.printf("Total expenses for %s: $%.2f%n", month, total);
	}

	@Override
	public void updateExpense(Scanner scanner) {
		if (expenses.isEmpty()) {
			System.out.println("No expenses to update.");
			return;
		}

		System.out.print("Enter the description of the expense you want to update: ");
		String searchDescription = scanner.nextLine().trim();

		List<Expense> matched = expenses.stream().filter(e -> e.getDescription().equalsIgnoreCase(searchDescription))
				.toList();

		if (matched.isEmpty()) {
			System.out.println("No expenses found with description: " + searchDescription);
			return;
		}

		Expense expenseToUpdate;

		if (matched.size() == 1) {
			expenseToUpdate = matched.get(0);
		} else {
			System.out.println("Multiple expenses found with that description:");
			System.out.println("\nID\tDate\t\tDescription\tAmount");
			matched.forEach(System.out::println);

			System.out.print("Enter the ID of the expense you want to update: ");
			int id = scanner.nextInt();
			scanner.nextLine();

			expenseToUpdate = matched.stream().filter(e -> e.getId() == id).findFirst().orElse(null);

			if (expenseToUpdate == null) {
				System.out.println("Invalid ID selected. No expense updated.");
				return;
			}
		}

		System.out.print("Enter new description (leave blank to keep same): ");
		String newDesc = scanner.nextLine().trim();

		System.out.print("Enter new amount (or -1 to keep same): ");
		double newAmount = scanner.nextDouble();
		scanner.nextLine();

		System.out.println("Enter new category: ");
		String newCategory = scanner.nextLine();

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
		System.out.println("Enter category to filter by: ");
		String category = scanner.nextLine();

		List<Expense> filtered = expenses.stream().filter(e -> e.getCategory().equalsIgnoreCase(category)).toList();

		if (filtered.isEmpty()) {
			System.out.println("No expenses found for category: " + category);

		}

		System.out.println("\n Expenses in category '" + category + "':");
		filtered.forEach(e -> System.out.println(e.toString()));

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
				writer.write(String.format("%s,%.2f,%s,%s\n", e.getDescription(), e.getAmount(), e.getCategory(),
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
