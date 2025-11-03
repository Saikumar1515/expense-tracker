package com.expense.tracker;

import java.util.Scanner;

public class ExpenseTracker {

    private static final int ADD_EXPENSE = 1;
    private static final int VIEW_ALL = 2;
    private static final int UPDATE = 3;
    private static final int DELETE = 4;
    private static final int SUMMARY_ALL = 5;
    private static final int SUMMARY_MONTH = 6;
    private static final int FILTER_CATEGORY = 7;
    private static final int SET_BUDGET = 8;
    private static final int EXPORT_CSV = 9;
    private static final int EXIT = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseServiceImpl service = new ExpenseServiceImpl(scanner);

        System.out.println("Welcome to Expense Tracker");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(scanner, "Enter your choice: ", ADD_EXPENSE, EXIT);

            try {
                switch (choice) {
                    case ADD_EXPENSE -> service.addExpense();
                    case VIEW_ALL -> service.viewExpenses();
                    case UPDATE -> service.updateExpense();
                    case DELETE -> service.deleteExpense();
                    case SUMMARY_ALL -> service.viewSummaryAll();
                    case SUMMARY_MONTH -> service.viewSummaryByMonth();
                    case FILTER_CATEGORY -> service.filterByCategory();
                    case SET_BUDGET -> service.setMonthlyBudget();
                    case EXPORT_CSV -> service.exportToCSV();
                    case EXIT -> running = false;
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

        System.out.println("Exiting... Goodbye!");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("""
            \nChoose an option:
            1. Add Expense
            2. View All Expenses
            3. Update Expense
            4. Delete Expense
            5. View Summary (All Expenses)
            6. View Summary by Month
            7. Filter By Category
            8. Set Monthly Budget
            9. Export Expenses to CSV
            10. Exit
            """);
    }

    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
