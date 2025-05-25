package com.alephys.expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Expense Tracker Application
 * A comprehensive solution for tracking income and expenses with categorization
 * and monthly reporting capabilities.
 *
 * @author Javvaji Jyoshna
 * @version 1.0
 */
public class Main {

    private static final String DATA_FILE = "transactions.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Transaction> transactions = new ArrayList<>();

    // Transaction categories
    private static final Map<TransactionType, List<String>> CATEGORIES = Map.of(
            TransactionType.INCOME, Arrays.asList("SALARY", "BUSINESS", "FREELANCING", "INVESTMENT", "OTHER"),
            TransactionType.EXPENSE, Arrays.asList("FOOD", "RENT", "TRAVEL", "UTILITIES", "ENTERTAINMENT", "HEALTHCARE", "OTHER")
    );

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   ALEPHYS EXPENSE TRACKER");
        System.out.println("=================================");

        // Load existing data
        loadDataFromFile();

        // Check if file input is provided
        if (args.length > 0) {
            System.out.println("Loading transactions from file: " + args[0]);
            loadTransactionsFromFile(args[0]);
        }

        // Main application loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addTransaction(TransactionType.INCOME);
                    break;
                case 2:
                    addTransaction(TransactionType.EXPENSE);
                    break;
                case 3:
                    viewMonthlySummary();
                    break;
                case 4:
                    viewAllTransactions();
                    break;
                case 5:
                    loadFromCustomFile();
                    break;
                case 6:
                    saveAndExit();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. View Monthly Summary");
        System.out.println("4. View All Transactions");
        System.out.println("5. Load from File");
        System.out.println("6. Save and Exit");
        System.out.print("Choice: ");
    }

    private static void addTransaction(TransactionType type) {
        System.out.println("\n--- ADD " + type.toString() + " ---");

        // Get amount
        double amount = getDoubleInput("Enter amount: $");

        // Display categories
        List<String> categories = CATEGORIES.get(type);
        System.out.println("\nAvailable categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        int categoryChoice = getIntInputInRange("Select category (1-" + categories.size() + "): ", 1, categories.size());
        String category = categories.get(categoryChoice - 1);

        // Get description
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        // Get date (optional)
        LocalDate date = getDateInput("Enter date (YYYY-MM-DD) or press Enter for today: ");

        // Create and add transaction
        Transaction transaction = new Transaction(type, amount, category, description, date);
        transactions.add(transaction);

        System.out.println("✅ Transaction added successfully!");
        System.out.println("Details: " + transaction);

        // Auto-save after each transaction
        saveDataToFile();
    }

    private static void viewMonthlySummary() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n--- MONTHLY SUMMARY ---");

        // Get month and year input
        System.out.print("Enter month (1-12) or press Enter for current month: ");
        String monthInput = scanner.nextLine().trim();

        System.out.print("Enter year (YYYY) or press Enter for current year: ");
        String yearInput = scanner.nextLine().trim();

        LocalDate now = LocalDate.now();
        int month = monthInput.isEmpty() ? now.getMonthValue() : Integer.parseInt(monthInput);
        int year = yearInput.isEmpty() ? now.getYear() : Integer.parseInt(yearInput);

        // Filter transactions for the specified month
        List<Transaction> monthlyTransactions = transactions.stream()
                .filter(t -> t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
                .collect(Collectors.toList());

        if (monthlyTransactions.isEmpty()) {
            System.out.println("No transactions found for " + month + "/" + year);
            return;
        }

        System.out.println("\n📊 SUMMARY FOR " + month + "/" + year + " 📊");
        System.out.println("=".repeat(50));

        // Calculate totals
        double totalIncome = monthlyTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = monthlyTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double netAmount = totalIncome - totalExpense;

        System.out.printf("💰 Total Income:  $%.2f%n", totalIncome);
        System.out.printf("💸 Total Expense: $%.2f%n", totalExpense);
        System.out.printf("📈 Net Amount:    $%.2f%n", netAmount);

        if (netAmount >= 0) {
            System.out.println("✅ You saved money this month!");
        } else {
            System.out.println("⚠️  You spent more than you earned this month.");
        }

        // Category-wise breakdown
        System.out.println("\n--- INCOME BREAKDOWN ---");
        displayCategoryBreakdown(monthlyTransactions, TransactionType.INCOME);

        System.out.println("\n--- EXPENSE BREAKDOWN ---");
        displayCategoryBreakdown(monthlyTransactions, TransactionType.EXPENSE);

        // Recent transactions
        System.out.println("\n--- RECENT TRANSACTIONS ---");
        monthlyTransactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .limit(10)
                .forEach(System.out::println);
    }

    private static void displayCategoryBreakdown(List<Transaction> transactions, TransactionType type) {
        Map<String, Double> categoryTotals = transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        if (categoryTotals.isEmpty()) {
            System.out.println("No " + type.toString().toLowerCase() + " transactions found.");
            return;
        }

        categoryTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> System.out.printf("  %s: $%.2f%n", entry.getKey(), entry.getValue()));
    }

    private static void viewAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n--- ALL TRANSACTIONS ---");
        System.out.println("=".repeat(80));

        transactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .forEach(System.out::println);

        System.out.println("=".repeat(80));
        System.out.println("Total transactions: " + transactions.size());
    }

    private static void loadFromCustomFile() {
        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine().trim();
        loadTransactionsFromFile(filePath);
    }

    private static void loadTransactionsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;

            System.out.println("Loading transactions from: " + filePath);

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }

                try {
                    Transaction transaction = parseTransactionFromLine(line);
                    transactions.add(transaction);
                    count++;
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }

            System.out.println("✅ Successfully loaded " + count + " transactions from file.");
            saveDataToFile(); // Save to main data file

        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
    }

    private static Transaction parseTransactionFromLine(String line) {
        // Expected format: TYPE,AMOUNT,CATEGORY,DESCRIPTION,DATE
        // Example: INCOME,5000.00,SALARY,Monthly salary,2024-01-15
        String[] parts = line.split(",", 5);

        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid format. Expected: TYPE,AMOUNT,CATEGORY,DESCRIPTION,DATE");
        }

        TransactionType type = TransactionType.valueOf(parts[0].trim().toUpperCase());
        double amount = Double.parseDouble(parts[1].trim());
        String category = parts[2].trim().toUpperCase();
        String description = parts[3].trim();
        LocalDate date = LocalDate.parse(parts[4].trim());

        return new Transaction(type, amount, category, description, date);
    }

    private static void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    Transaction transaction = parseTransactionFromLine(line);
                    transactions.add(transaction);
                } catch (Exception e) {
                    System.out.println("Error loading transaction: " + e.getMessage());
                }
            }
            System.out.println("📁 Loaded " + transactions.size() + " existing transactions.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(transaction.toCsvString());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void saveAndExit() {
        saveDataToFile();
        System.out.println("\n💾 Data saved successfully!");
        System.out.println("Thank you for using Alephys Expense Tracker!");
        System.out.println("=".repeat(40));
    }

    // Utility methods for input validation
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static int getIntInputInRange(String prompt, int min, int max) {
        while (true) {
            int value = getIntInput(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return LocalDate.now();
                }
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Please enter date in YYYY-MM-DD format or press Enter for today.");
            }
        }
    }
}

// Enum for transaction types
enum TransactionType {
    INCOME, EXPENSE
}

// Transaction class
class Transaction {
    private TransactionType type;
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Transaction(TransactionType type, double amount, String category, String description, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    // Getters
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    public String toCsvString() {
        return String.format("%s,%.2f,%s,%s,%s",
                type, amount, category, description, date);
    }

    @Override
    public String toString() {
        String symbol = type == TransactionType.INCOME ? "💰" : "💸";
        return String.format("%s [%s] $%.2f - %s (%s) - %s",
                symbol, date, amount, category, description, type);
    }
}
