# Alephys Expense Tracker

A comprehensive Java application for tracking personal income and expenses with categorization and monthly reporting capabilities.

## 🚀 Features

- **Add Income & Expenses**: Easy-to-use interface for recording transactions
- **Categorization**: Predefined categories for both income and expenses
- **Monthly Summaries**: Detailed monthly reports with breakdowns
- **File Operations**: Load transactions from CSV files and auto-save functionality
- **Data Persistence**: All data is automatically saved to `transactions.csv`
- **Input Validation**: Robust error handling and input validation

## 📁 Project Structure

```
expense-tracker/
├── src/
│   └── com/
│       └── alephys/
│           └── expensetracker/
│               └── ExpenseTracker.java
├── sample_transactions.csv
├── transactions.csv (generated at runtime)
└── README.md
```

## 🛠️ Prerequisites

- Java 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

## 🏗️ Compilation and Execution

### Using Command Line:

```bash
# Compile the program
javac -d . ExpenseTracker.java

# Run without file input
java com.alephys.expensetracker.ExpenseTracker

# Run with file input
java com.alephys.expensetracker.ExpenseTracker sample_transactions.csv
```

### Using IDE:
1. Import the project into your IDE
2. Ensure the package structure is correct
3. Run the `ExpenseTracker` class

## 📋 Usage Instructions

### 1. **Main Menu Options**
- **Add Income**: Record salary, business income, freelancing, etc.
- **Add Expense**: Record rent, food, travel, utilities, etc.
- **View Monthly Summary**: Get detailed monthly reports
- **View All Transactions**: List all recorded transactions
- **Load from File**: Import transactions from a CSV file
- **Save and Exit**: Save all data and close the application

### 2. **Adding Transactions**
- Select transaction type (Income/Expense)
- Enter the amount
- Choose from predefined categories
- Add a description
- Enter date (or use today's date)

### 3. **File Format**
The application accepts CSV files with the following format:
```
TYPE,AMOUNT,CATEGORY,DESCRIPTION,DATE
```

**Example:**
```csv
INCOME,5000.00,SALARY,Monthly salary,2024-01-01
EXPENSE,1200.00,RENT,Monthly rent payment,2024-01-01
```

### 4. **Categories**

**Income Categories:**
- SALARY
- BUSINESS
- FREELANCING
- INVESTMENT
- OTHER

**Expense Categories:**
- FOOD
- RENT
- TRAVEL
- UTILITIES
- ENTERTAINMENT
- HEALTHCARE
- OTHER

## 📊 Monthly Summary Features

The monthly summary provides:
- Total income and expenses for the month
- Net amount (savings/deficit)
- Category-wise breakdown
- Recent transactions list
- Visual indicators for financial health

## 🔧 Technical Implementation

### Key Classes:
- **ExpenseTracker**: Main application class with UI and business logic
- **Transaction**: Data model for individual transactions
- **TransactionType**: Enum for INCOME/EXPENSE types

### Key Features:
- **Data Persistence**: Automatic saving to CSV file
- **Input Validation**: Comprehensive error handling
- **Date Handling**: Flexible date input with defaults
- **Stream Processing**: Modern Java 8+ features for data processing
- **File I/O**: Robust file reading and writing capabilities

## 📸 Sample Screenshots

### 1. Main Menu
```
=================================
   ALEPHYS EXPENSE TRACKER
=================================

--- MAIN MENU ---
1. Add Income
2. Add Expense
3. View Monthly Summary
4. View All Transactions
5. Load from File
6. Save and Exit
Choice: 
```

### 2. Adding Income
```
--- ADD INCOME ---
Enter amount: $5000

Available categories:
1. SALARY
2. BUSINESS
3. FREELANCING
4. INVESTMENT
5. OTHER
Select category (1-5): 1
Enter description: Monthly salary
Enter date (YYYY-MM-DD) or press Enter for today: 

✅ Transaction added successfully!
Details: 💰 [2024-01-15] $5000.00 - SALARY (Monthly salary) - INCOME
```

### 3. Monthly Summary
```
📊 SUMMARY FOR 1/2024 📊
==================================================
💰 Total Income:  $7500.00
💸 Total Expense: $2650.00
📈 Net Amount:    $4850.00
✅ You saved money this month!

--- INCOME BREAKDOWN ---
  SALARY: $5000.00
  FREELANCING: $2000.00
  INVESTMENT: $500.00

--- EXPENSE BREAKDOWN ---
  RENT: $1200.00
  FOOD: $400.00
  UTILITIES: $225.00
  ENTERTAINMENT: $200.00
```

## 🧪 Testing

### Test Cases Covered:
1. **Input Validation**: Invalid amounts, dates, menu choices
2. **File Operations**: Loading valid/invalid CSV files
3. **Data Persistence**: Saving and loading data across sessions
4. **Category Selection**: Valid category choices for income/expense
5. **Monthly Filtering**: Correct calculation of monthly summaries
6. **Edge Cases**: Empty files, malformed data, future dates

### Sample Test Data:
Use the provided `sample_transactions.csv` file to test the file loading functionality.

## 🚀 Future Enhancements

- **Export Functionality**: Export summaries to PDF/Excel
- **Budget Tracking**: Set and monitor monthly budgets
- **Data Visualization**: Charts and graphs for spending patterns
- **Multi-user Support**: User accounts and authentication
- **Mobile App**: Android/iOS companion app
- **Cloud Sync**: Backup and sync across devices

## 📝 Code Quality

- **Clean Code**: Well-structured, readable, and maintainable
- **Documentation**: Comprehensive comments and JavaDoc
- **Error Handling**: Robust exception handling throughout
- **Modular Design**: Separation of concerns and reusable components
- **Modern Java**: Utilizes Java 8+ features like Streams and Lambda expressions


## 📄 License

This project is developed for Alephys technical assessment purposes.

---

**Contact Information:**
- Developer: Javvaji Jyoshna
- Email: jyoshnajavvaji2004@gmail.com
- GitHub: 
- LinkedIn: https://www.linkedin.com/in/javvaji-jyoshna-564343243/

**Position:** Java Developer - Alephys
