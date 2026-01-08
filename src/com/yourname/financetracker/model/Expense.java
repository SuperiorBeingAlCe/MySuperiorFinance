package com.yourname.financetracker.model;

import java.time.LocalDate;

public class Expense extends FinancialRecord{

	  public Expense(double amount, LocalDate date, String description) {
	        super(amount, date, description);
	    }
	
}
