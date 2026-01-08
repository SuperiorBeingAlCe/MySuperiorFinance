package com.yourname.financetracker.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.UIManager;

import com.yourname.financetracker.core.Mode;
import com.yourname.financetracker.service.FinanceManager;

public class MainFrame extends JFrame{
	
	private final FinanceManager manager = new FinanceManager();

    // ===== STATE =====
    private Mode currentMode = Mode.INCOME;

    // ===== INPUTS =====
    private JTextField amountField = new JTextField();
    private JTextField descriptionField = new JTextField();
    private JTextField dateField = new JTextField();
    private JTextField deadlineField = new JTextField();

    // ===== LABELS =====
    private JLabel incomeLabel = new JLabel("Toplam Gelir: 0");
    private JLabel expenseLabel = new JLabel("Toplam Gider: 0");
    private JLabel balanceLabel = new JLabel("Net Bakiye: 0");

    // ===== MODE BUTTONS =====
    private JButton incomeModeBtn = new JButton("Gelir");
    private JButton expenseModeBtn = new JButton("Gider");
    private JButton debtModeBtn = new JButton("Borç");

    // ===== COLORS =====
    private final Color defaultColor = UIManager.getColor("Button.background");
    private final Color selectedColor = new Color(120, 200, 120);

    public MainFrame() {
        setTitle("Finans Takip Uygulaması");
        setSize(650, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ================= ÜST PANEL =================
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        Font summaryFont = new Font("Arial", Font.BOLD, 14);

        incomeLabel.setFont(summaryFont);
        expenseLabel.setFont(summaryFont);
        balanceLabel.setFont(summaryFont);

        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);

        // ================= ORTA PANEL =================
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Tutar"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Açıklama"));
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Tarih"));
        dateField.setEnabled(false);
        dateField.setText(LocalDate.now().toString());
        formPanel.add(dateField);

        formPanel.add(new JLabel("Son Ödeme Tarihi (Borç)"));
        deadlineField.setEnabled(false);
        formPanel.add(deadlineField);

        // ================= ALT PANEL =================
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        modePanel.add(incomeModeBtn);
        modePanel.add(expenseModeBtn);
        modePanel.add(debtModeBtn);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Ekle");
        actionPanel.add(addBtn);

        bottomPanel.add(modePanel, BorderLayout.NORTH);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        add(summaryPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ================= EVENTS =================
        incomeModeBtn.addActionListener(e -> switchMode(Mode.INCOME));
        expenseModeBtn.addActionListener(e -> switchMode(Mode.EXPENSE));
        debtModeBtn.addActionListener(e -> switchMode(Mode.DEBT));

        addBtn.addActionListener(e -> handleAdd());

        // DEFAULT MODE
        switchMode(Mode.INCOME);
    }

    // ================= MODE SWITCH =================
    private void switchMode(Mode mode) {
        currentMode = mode;

        incomeModeBtn.setBackground(defaultColor);
        expenseModeBtn.setBackground(defaultColor);
        debtModeBtn.setBackground(defaultColor);

        if (mode == Mode.INCOME) {
            incomeModeBtn.setBackground(selectedColor);
            deadlineField.setEnabled(false);
            deadlineField.setText("");
        } else if (mode == Mode.EXPENSE) {
            expenseModeBtn.setBackground(selectedColor);
            deadlineField.setEnabled(false);
            deadlineField.setText("");
        } else {
            debtModeBtn.setBackground(selectedColor);
            deadlineField.setEnabled(true);
        }
    }

    // ================= ADD LOGIC =================
    private void handleAdd() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            LocalDate date = LocalDate.now();

            dateField.setText(date.toString());

            switch (currentMode) {
                case INCOME -> manager.addIncome(amount, date, description);
                case EXPENSE -> manager.addExpense(amount, date, description);
                case DEBT -> {
                    if (deadlineField.getText().isBlank()) {
                        showError("Borç için son ödeme tarihi zorunludur.");
                        return;
                    }
                    LocalDate deadline = LocalDate.parse(deadlineField.getText());
                    manager.addDebt(amount, date, description, deadline);
                }
            }

            refresh();
            clearInputs();

        } catch (Exception e) {
            showError("Tutar formatı veya tarih hatalı.");
        }
    }

    // ================= HELPERS =================
    private void refresh() {
        incomeLabel.setText("Toplam Gelir: " + manager.getTotalIncome());
        expenseLabel.setText("Toplam Gider: " + manager.getTotalExpense());
        balanceLabel.setText("Net Bakiye: " + manager.getNetBalance());
    }

    private void clearInputs() {
        amountField.setText("");
        descriptionField.setText("");
        if (currentMode != Mode.DEBT) {
            deadlineField.setText("");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Hata", JOptionPane.ERROR_MESSAGE);
    }
}
