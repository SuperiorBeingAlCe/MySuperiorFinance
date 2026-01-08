package com.yourname.financetracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.yourname.financetracker.model.Debt;
import com.yourname.financetracker.model.Expense;
import com.yourname.financetracker.model.Income;

public class FinanceManager {

	 private final List<Income> incomes = new ArrayList<>();
	    private final List<Expense> expenses = new ArrayList<>();
	    private final List<Debt> debts = new ArrayList<>();

	    // ======================
	    // Add (UI / Domain)
	    // ======================

	    public void addIncome(double amount, LocalDate date, String description) {
	        incomes.add(new Income(amount, date, description));
	    }

	    public void addExpense(double amount, LocalDate date, String description) {
	        expenses.add(new Expense(amount, date, description));
	    }

	    public void addDebt(double amount, LocalDate date, String description, LocalDate deadline) {
	        debts.add(new Debt(amount, date, description, deadline));
	    }

	    // ======================
	    // Add (Persistence only)
	    // ======================

	     public void addIncome(Income income) {
	        incomes.add(income);
	    }

	    public void addExpense(Expense expense) {
	        expenses.add(expense);
	    }

	    public void addDebt(Debt debt) {
	        debts.add(debt);
	    }

	    // ======================
	    // Totals
	    // ======================

	    public double getTotalIncome() {
	        return incomes.stream()
	                .mapToDouble(Income::getAmount)
	                .sum();
	    }

	    public double getTotalExpense() {
	        return expenses.stream()
	                .mapToDouble(Expense::getAmount)
	                .sum();
	    }

	    public double getTotalDebt() {
	        return debts.stream()
	                .filter(d -> !d.isPaid())
	                .mapToDouble(Debt::getAmount)
	                .sum();
	    }

	    // ======================
	    // Calculation
	    // ======================

	    public double getNetBalance() {
	        return getTotalIncome() - getTotalExpense() - getTotalDebt();
	    }

	    // ======================
	    // Queries (Safe copies)
	    // ======================

	    public List<Income> getIncomes() {
	        return new ArrayList<>(incomes);
	    }

	    public List<Expense> getExpenses() {
	        return new ArrayList<>(expenses);
	    }

	    public List<Debt> getAllDebts() {
	        return new ArrayList<>(debts);
	    }

	    public List<Debt> getOverdueDebts(LocalDate today) {
	        return debts.stream()
	                .filter(d -> d.isOverdue(today))
	                .toList();
	    }
}
