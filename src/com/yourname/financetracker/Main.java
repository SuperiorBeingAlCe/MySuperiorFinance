package com.yourname.financetracker;

import javax.swing.SwingUtilities;

import com.yourname.financetracker.core.FinanceXMLReader;
import com.yourname.financetracker.service.FinanceManager;
import com.yourname.financetracker.ui.MainFrame;

public class Main {

	public static void main(String[] args) {
		FinanceManager manager = FinanceXMLReader.load();
		 SwingUtilities.invokeLater(() -> {
	            MainFrame frame = new MainFrame(manager);
	            frame.setVisible(true);
	        });
    }
	}
