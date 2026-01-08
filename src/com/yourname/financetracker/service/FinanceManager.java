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
	    // Delete / Remove
	    // ======================
	    public boolean removeIncome(Income income) {
	        return incomes.remove(income);
	    }

	    public boolean removeExpense(Expense expense) {
	        return expenses.remove(expense);
	    }

	    public boolean removeDebt(Debt debt) {
	        return debts.remove(debt);
	    }
	    
	    public boolean removeIncomeAt(int index) {
	        if (index >= 0 && index < incomes.size()) {
	            incomes.remove(index);
	            return true;
	        }
	        return false;
	    }

	    public boolean removeExpenseAt(int index) {
	        if (index >= 0 && index < expenses.size()) {
	            expenses.remove(index);
	            return true;
	        }
	        return false;
	    }

	    public boolean removeDebtAt(int index) {
	        if (index >= 0 && index < debts.size()) {
	            debts.remove(index);
	            return true;
	        }
	        return false;
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
	    
	 // ======================
	 // Listing / Display
	 // ======================

	 public void printAllIncomes() {
	     System.out.println("==== GELİRLER ====");
	     if (incomes.isEmpty()) {
	         System.out.println("Gelir yok.");
	     } else {
	         for (Income income : incomes) {
	             System.out.println("Tutar: " + income.getAmount() +
	                                ", Tarih: " + income.getDate() +
	                                ", Açıklama: " + income.getDescription());
	         }
	     }
	 }

	 public void printAllExpenses() {
	     System.out.println("==== GİDERLER ====");
	     if (expenses.isEmpty()) {
	         System.out.println("Gider yok.");
	     } else {
	         for (Expense expense : expenses) {
	             System.out.println("Tutar: " + expense.getAmount() +
	                                ", Tarih: " + expense.getDate() +
	                                ", Açıklama: " + expense.getDescription());
	         }
	     }
	 }

	 public void printAllDebts() {
	     System.out.println("==== BORÇLAR ====");
	     if (debts.isEmpty()) {
	         System.out.println("Borç yok.");
	     } else {
	         for (Debt debt : debts) {
	             System.out.println("Tutar: " + debt.getAmount() +
	                                ", Tarih: " + debt.getDate() +
	                                ", Açıklama: " + debt.getDescription() +
	                                ", Son Tarih: " + debt.getDeadline() +
	                                ", Ödendi mi? " + (debt.isPaid() ? "Evet" : "Hayır"));
	         }
	     }
	 }

	 // Opsiyonel: tek metotla hepsini göstermek
	 public void printFullReport() {
	     printAllIncomes();
	     printAllExpenses();
	     printAllDebts();
	     System.out.println("==== ÖZET ====");
	     System.out.println("Toplam Gelir: " + getTotalIncome());
	     System.out.println("Toplam Gider: " + getTotalExpense());
	     System.out.println("Toplam Ödenmemiş Borç: " + getTotalDebt());
	     System.out.println("Net Bakiye: " + getNetBalance());
	 }
}
