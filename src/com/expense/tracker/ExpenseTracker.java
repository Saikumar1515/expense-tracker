package com.expense.tracker;

import java.util.Scanner;

public class ExpenseTracker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseServiceImpl service = new ExpenseServiceImpl();

        System.out.println("Welcome to Expense Tracker");

        while (true) {
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
                    9, Export Expenses to CSV
                    10. Exit
                    """);

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> service.addExpense(scanner);
                case 2 -> service.viewExpenses();
                case 3 -> service.updateExpense(scanner);
                case 4 -> service.deleteExpense(scanner);
                case 5 -> service.viewSummaryAll();
                case 6 -> service.viewSummaryByMonth(scanner);
                case 7 -> service.filterByCategory(scanner);
                case 8 -> service.setMonthlyBudget(scanner);
                case 9 -> service.exportToCSV();
                case 10 -> {
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
