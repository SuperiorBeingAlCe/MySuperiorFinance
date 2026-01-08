package com.yourname.financetracker.test;



import java.time.LocalDate;

import com.yourname.financetracker.service.FinanceManager;

public class FinanceManagerTest {
	
	public static void main(String[] args) {

        FinanceManager manager = new FinanceManager();

        manager.addIncome(2000, LocalDate.now(), "Maaş");
        manager.addExpense(600, LocalDate.now(), "Kira");
        manager.addDebt(300, LocalDate.now(), "Borç", LocalDate.now().minusDays(1));

        System.out.println("Total Income: " + manager.getTotalIncome());   // 2000
        System.out.println("Total Expense: " + manager.getTotalExpense()); // 600
        System.out.println("Total Debt: " + manager.getTotalDebt());       // 300
        System.out.println("Net Balance: " + manager.getNetBalance());     // 1100
        System.out.println("Overdue Debts: " +
                manager.getOverdueDebts(LocalDate.now()).size());          // 1
    }
}
