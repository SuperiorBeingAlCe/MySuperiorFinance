package com.yourname.financetracker.model;

import java.time.LocalDate;

public class Debt extends FinancialRecord{
	 private final LocalDate deadline;
	    private boolean paid;

	    public Debt(
	        double amount,
	        LocalDate date,
	        String description,
	        LocalDate deadline
	    ) {
	        super(amount, date, description);
	        this.deadline = deadline;
	        this.paid = false;
	    }

	    public LocalDate getDeadline() { return deadline; }

	    public boolean isPaid() { return paid; }

	    public boolean isOverdue(LocalDate today) {
	        return !paid && deadline.isBefore(today);
	    }

	    public void markAsPaid() {
	        this.paid = true;
	    }
    
}
