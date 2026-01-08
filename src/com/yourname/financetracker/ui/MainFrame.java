package com.yourname.financetracker.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.yourname.financetracker.service.FinanceManager;

public class MainFrame extends JFrame{
	
	  private final FinanceManager manager = new FinanceManager();

	    private JTextField amountField = new JTextField();
	    private JTextField descriptionField = new JTextField();
	    private JTextField dateField = new JTextField();
	    private JTextField deadlineField = new JTextField();

	    private JLabel incomeLabel = new JLabel("Toplam Gelir: 0");
	    private JLabel expenseLabel = new JLabel("Toplam Gider: 0");
	    private JLabel balanceLabel = new JLabel("Net Bakiye: 0");

	    public MainFrame() {
	        setTitle("Finans Takip Uygulaması");
	        setSize(600, 400);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout(10, 10));

	        // === ÜST PANEL (ÖZET) ===
	        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
	        Font summaryFont = new Font("Arial", Font.BOLD, 14);

	        incomeLabel.setFont(summaryFont);
	        expenseLabel.setFont(summaryFont);
	        balanceLabel.setFont(summaryFont);

	        summaryPanel.add(incomeLabel);
	        summaryPanel.add(expenseLabel);
	        summaryPanel.add(balanceLabel);

	        // === ORTA PANEL (FORM) ===
	        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

	        formPanel.add(new JLabel("Tutar"));
	        formPanel.add(amountField);

	        formPanel.add(new JLabel("Açıklama"));
	        formPanel.add(descriptionField);

	        formPanel.add(new JLabel("Tarih (yyyy-MM-dd)"));
	        formPanel.add(dateField);

	        formPanel.add(new JLabel("Son Ödeme Tarihi (Borç)"));
	        formPanel.add(deadlineField);

	        // Başlangıçta deadline kapalı
	        deadlineField.setEnabled(false);

	        // === ALT PANEL (BUTONLAR) ===
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

	        JButton incomeBtn = new JButton("Gelir Ekle");
	        JButton expenseBtn = new JButton("Gider Ekle");
	        JButton debtBtn = new JButton("Borç Ekle");

	        buttonPanel.add(incomeBtn);
	        buttonPanel.add(expenseBtn);
	        buttonPanel.add(debtBtn);

	        add(summaryPanel, BorderLayout.NORTH);
	        add(formPanel, BorderLayout.CENTER);
	        add(buttonPanel, BorderLayout.SOUTH);

	        // === EVENTS ===

	        incomeBtn.addActionListener(e -> {
	            try {
	                deadlineField.setEnabled(false);
	                deadlineField.setText("");

	                manager.addIncome(
	                        Double.parseDouble(amountField.getText()),
	                        LocalDate.parse(dateField.getText()),
	                        descriptionField.getText()
	                );
	                refresh();
	            } catch (Exception ex) {
	                showError("Gelir eklenirken hata oluştu.");
	            }
	        });

	        expenseBtn.addActionListener(e -> {
	            try {
	                deadlineField.setEnabled(false);
	                deadlineField.setText("");

	                manager.addExpense(
	                        Double.parseDouble(amountField.getText()),
	                        LocalDate.parse(dateField.getText()),
	                        descriptionField.getText()
	                );
	                refresh();
	            } catch (Exception ex) {
	                showError("Gider eklenirken hata oluştu.");
	            }
	        });

	        debtBtn.addActionListener(e -> {
	            try {
	                deadlineField.setEnabled(true);

	                if (deadlineField.getText().isBlank()) {
	                    showError("Borç için son ödeme tarihi zorunludur.");
	                    return;
	                }

	                manager.addDebt(
	                        Double.parseDouble(amountField.getText()),
	                        LocalDate.parse(dateField.getText()),
	                        descriptionField.getText(),
	                        LocalDate.parse(deadlineField.getText())
	                );
	                refresh();
	            } catch (Exception ex) {
	                showError("Borç eklenirken hata oluştu.");
	            }
	        });
	    }

	    private void refresh() {
	        incomeLabel.setText("Toplam Gelir: " + manager.getTotalIncome());
	        expenseLabel.setText("Toplam Gider: " + manager.getTotalExpense());
	        balanceLabel.setText("Net Bakiye: " + manager.getNetBalance());
	    }

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(
	                this,
	                message,
	                "Hata",
	                JOptionPane.ERROR_MESSAGE
	        );
	    }

	   
}
