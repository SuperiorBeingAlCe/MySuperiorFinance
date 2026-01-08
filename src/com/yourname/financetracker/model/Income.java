package com.yourname.financetracker.model;

import java.time.LocalDate;

public class Income extends FinancialRecord{

	 public Income(double amount, LocalDate date, String description) {
	        super(amount, date, description);
	    }
	
}
