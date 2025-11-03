package com.expense.tracker;

import java.awt.FlowLayout;
import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseServiceImpl implements ExpenseService {

	private final List<Expense> expenses = new ArrayList<>();
	private static final Map<YearMonth, Double> monthlyBudgets = new HashMap<>();
	private final Scanner scanner;

	public ExpenseServiceImpl(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public void addExpense() {

		try {
			String description = readString("Enter expense description: ");
			double amount = readDouble("Enter expense amount: ");
			String category = readString("Enter category: ");
			LocalDate date = LocalDate.now();
			YearMonth currentMonth = YearMonth.from(date);

			double currentTotal = expenses.stream().filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
					.mapToDouble(Expense::getAmount).sum();

			double monthlyBudget = monthlyBudgets.getOrDefault(currentMonth, Double.MAX_VALUE);
			if (monthlyBudgets.containsKey(currentMonth) && currentTotal + amount > monthlyBudget) {
				System.out.printf("Warning: Adding this expense (Rs.%.2f) will exceed your budget of Rs.%.2f%n", amount,
						monthlyBudget);
				String choice = readString("Do you still want to continue? (yes/no): ").toLowerCase();
				if (!choice.equals("yes")) {
					System.out.println("Expense not added.");
					return;
				}
			}

			Expense newExpenses = new Expense(description, amount, category, date);
			expenses.add(newExpenses);
			System.out.println("Expense added successfully!");
		} catch (Exception e) {
			System.out.println("Error adding expense: " + e.getMessage());
		}

	}

	@Override
	public void viewExpenses() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses added yet");
			return;
		}
		printExpenseTable(expenses);

	}

	@Override
	public void updateExpense() {
		if (expenses.isEmpty()) {
			System.out.println(" No expenses to update.");
			return;
		}

		String searchDescription = readString("Enter the description of the expense to update: ");
		List<Expense> matched = expenses.stream().filter(e -> e.getDescription().equalsIgnoreCase(searchDescription))
				.collect(Collectors.toList());

		if (matched.isEmpty()) {
			System.out.println(" No expenses found with description: " + searchDescription);
			return;
		}

		Expense expenseToUpdate;

		if (matched.size() == 1) {
			expenseToUpdate = matched.get(0);
		} else {
			System.out.println("\nMultiple expenses found with that description:\n");
			printExpenseTable(matched);
			int id = readInt("Enter the ID of the expense to update: ");
			expenseToUpdate = findExpenseById(id);
			if (expenseToUpdate == null) {
				System.out.println("Invalid ID selected. No expense updated.");
				return;
			}
		}

		String newDesc = readString("Enter new description (leave blank to keep same): ");
		double newAmount = readDoubleOptional("Enter new amount (or -1 to keep same): ");
		String newCategory = readString("Enter new category (leave blank to keep same): ");

		if (!newDesc.isEmpty())
			expenseToUpdate.setDescription(newDesc);
		if (newAmount >= 0)
			expenseToUpdate.setAmount(newAmount);
		if (!newCategory.isEmpty())
			expenseToUpdate.setCategory(newCategory);

		System.out.println("Expense updated successfully!");
	}

	@Override
	public void deleteExpense() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses to delete.");
			return;
		}

		printExpenseTable(expenses);

		int id = readInt("Enter the ID of the expense to delete: ");
		Expense toRemove = findExpenseById(id);
		if (toRemove != null) {
			expenses.remove(toRemove);
			System.out.printf("Expense '%s' deleted successfully!%n", toRemove.getDescription());
		} else {
			System.out.println("Expense with the given ID not found.");
		}
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
	public void viewSummaryByMonth() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses recorded.");
			return;
		}

		int month = readInt("enter Month (1-12): ", 1, 12);
		int year = readInt("Enter year (e.g., 2025): ", 1900, 3000);

		List<Expense> monthly = expenses.stream()
				.filter(e -> e.getDate().getMonthValue() == month && e.getDate().getYear() == year)
				.collect(Collectors.toList());

		if (monthly.isEmpty()) {
			 System.out.printf("No expenses found for %02d-%d%n", month, year);
			return;
		}

		double total = monthly.stream().mapToDouble(Expense::getAmount).sum();
	    System.out.printf("Total expenses for %02d-%d: Rs.%.2f%n", month, year, total);
	}

	@Override
	public void filterByCategory() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses available.");
			return;
		}

		String category = readString("Enter category to filter by: ");
		List<Expense> filtered = expenses.stream().filter(e -> e.getCategory().equalsIgnoreCase(category))
				.collect(Collectors.toList());

		if (filtered.isEmpty()) {
			System.out.println("No expenses found in category '" + category + "'.");
			return;
		}

		printExpenseTable(filtered);
	}

	@Override
	public void setMonthlyBudget() {
		int month = readInt("Enter month (1-12): ", 1, 12);
		int year = readInt("\"Enter year (e.g., 2025): ", 1900, 3000);
		double amount = readDouble("Entet budget amount: ");

		YearMonth ym = YearMonth.of(year, month);
		monthlyBudgets.put(ym, amount);

		System.out.printf(" Monthly budget set: Rs.%.2f for %s%n", amount, ym);

	}

	@Override
	public void exportToCSV() {
		if (expenses.isEmpty()) {
			System.out.println(" No expenses to export.");
			return;
		}

		try (FileWriter writer = new FileWriter("expenses.csv")) {
			writer.write("Description,Amount,Category,Date\n");
			for (Expense e : expenses) {
				writer.write(String.format("%s,Rs.%.2f,%s,%s\n", e.getDescription(), e.getAmount(), e.getCategory(),
						e.getDate()));
			}
			System.out.println("Expenses exported successfully to 'expenses.csv");
		} catch (IOException e) {
			System.out.println("Error exporting to CSV: " + e.getMessage());
		}
	}

//	Helper methods

	private double readDouble(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				double value = Double.parseDouble(scanner.nextLine().trim());
				if (value < 0)
					throw new NumberFormatException();
				return value;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a positive number.");
			}
		}

	}

	private String readString(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}

	private void printExpenseTable(List<Expense> list) {
		System.out.println("--------------------------------------------------------------");
		System.out.printf("%-5s %-12s %-20s %-15s %-10s%n", "ID", "Date", "Description", "Category", "Amount");
		System.out.println("--------------------------------------------------------------");
		for (Expense e : list) {
			System.out.printf("%-5d %-12s %-20s %-15s Rs.%-10.2f%n", e.getId(), e.getDate(), e.getDescription(),
					e.getCategory(), e.getAmount());
		}
		System.out.println("--------------------------------------------------------------");
	}

	private Expense findExpenseById(int id) {
		return expenses.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}

	private int readInt(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				return Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter an integer.");
			}
		}

	}

	private double readDoubleOptional(String prompt) {
		while (true) {
			System.out.println(prompt);
			try {
				double value = Double.parseDouble(scanner.nextLine().trim());
				return value;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}

	}

	private int readInt(String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);
			String input = scanner.nextLine().trim();
			try {
				int value = Integer.parseInt(input);
				if (value >= min && value <= max)
					return value;
				System.out.printf("Please enter a number between %d and %d.%n", min, max);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}

}
