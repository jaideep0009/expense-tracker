package service;

import model.Transaction;
import storage.JsonStorage;

import java.time.LocalDate;
import java.util.List;

public class ExpenseService {

    // Add income
    public void addIncome(String category, double amount, LocalDate date, String note) {
        Transaction t = new Transaction("INCOME", category, amount, date, note);
        JsonStorage.saveTransaction(t);
    }

    // Add expense
    public void addExpense(String category, double amount, LocalDate date, String note) {
        Transaction t = new Transaction("EXPENSE", category, amount, date, note);
        JsonStorage.saveTransaction(t);
    }

    // Load all transactions
    public List<Transaction> getAllTransactions() {
        return JsonStorage.loadAllTransactions();
    }
}
