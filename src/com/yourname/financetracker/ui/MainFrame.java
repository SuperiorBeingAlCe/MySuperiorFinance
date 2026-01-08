package com.yourname.financetracker.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yourname.financetracker.core.FinanceXMLWriter;
import com.yourname.financetracker.core.Mode;
import com.yourname.financetracker.service.FinanceManager;

public class MainFrame extends JFrame {
	private final FinanceManager manager;

    private Mode currentMode = Mode.INCOME;

    // ===== INPUTS =====
    private JTextField amountField = new JTextField();
    private JTextField descriptionField = new JTextField();
    private JTextField dateField = new JTextField();
    private JTextField deadlineField = new JTextField();

    // ===== LABELS =====
    private JLabel incomeLabel = new JLabel();
    private JLabel expenseLabel = new JLabel();
    private JLabel balanceLabel = new JLabel();

    // ===== BUTTONS =====
    private JButton incomeModeBtn = new JButton("GELİR");
    private JButton expenseModeBtn = new JButton("GİDER");
    private JButton debtModeBtn = new JButton("BORÇ");
    private JButton addBtn = new JButton("EKLE");

    // ===== THEME COLORS =====
    private static final Color BG_MAIN = new Color(18, 18, 18);
    private static final Color BG_PANEL = new Color(28, 28, 28);
    private static final Color FG_TEXT = new Color(220, 220, 220);

    private static final Color GREEN = new Color(0, 200, 120);
    private static final Color RED = new Color(220, 80, 80);
    private static final Color YELLOW = new Color(200, 180, 80);
    private static final Color GRAY = new Color(70, 70, 70);

    private static final Font FONT_MAIN = new Font("Consolas", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("Consolas", Font.BOLD, 15);

    public MainFrame(FinanceManager manager) {
        this.manager = manager;

        setTitle("FINANCE TERMINAL");
        setSize(680, 440);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_MAIN);

        // ================= SUMMARY =================
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        summaryPanel.setBackground(BG_MAIN);
        summaryPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        styleLabel(incomeLabel, GREEN);
        styleLabel(expenseLabel, RED);
        styleLabel(balanceLabel, YELLOW);

        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(BG_PANEL);
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        formPanel.add(createLabel("TUTAR"));
        formPanel.add(styleField(amountField));

        formPanel.add(createLabel("AÇIKLAMA"));
        formPanel.add(styleField(descriptionField));

        formPanel.add(createLabel("TARİH"));
        dateField.setText(LocalDate.now().toString());
        dateField.setEnabled(false);
        formPanel.add(styleField(dateField));

        formPanel.add(createLabel("SON ÖDEME (BORÇ)"));
        deadlineField.setEnabled(false);
        formPanel.add(styleField(deadlineField));

        // ================= BOTTOM =================
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_MAIN);
        bottomPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        modePanel.setBackground(BG_MAIN);

        styleButton(incomeModeBtn);
        styleButton(expenseModeBtn);
        styleButton(debtModeBtn);

        modePanel.add(incomeModeBtn);
        modePanel.add(expenseModeBtn);
        modePanel.add(debtModeBtn);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(BG_MAIN);
        styleButton(addBtn);
        addBtn.setForeground(YELLOW);
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FinanceXMLWriter.save(manager);
            }
        });

        switchMode(Mode.INCOME);
        refresh();
    }

    // ================= MODE =================
    private void switchMode(Mode mode) {
        currentMode = mode;

        resetModeButtons();

        if (mode == Mode.INCOME) {
            incomeModeBtn.setBackground(GREEN);
            deadlineField.setEnabled(false);
            deadlineField.setText("");
        } else if (mode == Mode.EXPENSE) {
            expenseModeBtn.setBackground(RED);
            deadlineField.setEnabled(false);
            deadlineField.setText("");
        } else {
            debtModeBtn.setBackground(YELLOW);
            deadlineField.setEnabled(true);
        }
    }

    private void resetModeButtons() {
        incomeModeBtn.setBackground(GRAY);
        expenseModeBtn.setBackground(GRAY);
        debtModeBtn.setBackground(GRAY);
    }

    // ================= LOGIC =================
    private void handleAdd() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            LocalDate date = LocalDate.now();

            switch (currentMode) {
                case INCOME -> manager.addIncome(amount, date, description);
                case EXPENSE -> manager.addExpense(amount, date, description);
                case DEBT -> {
                    if (deadlineField.getText().isBlank()) {
                        showError("BORÇ İÇİN SON TARİH ZORUNLU");
                        return;
                    }
                    manager.addDebt(amount, date, description,
                            LocalDate.parse(deadlineField.getText()));
                }
            }

            refresh();
            clearInputs();

        } catch (Exception e) {
            showError("VERİLER GEÇERSİZ");
        }
    }

    private void refresh() {
        incomeLabel.setText("GELİR : " + manager.getTotalIncome());
        expenseLabel.setText("GİDER : " + manager.getTotalExpense());
        balanceLabel.setText("BAKİYE : " + manager.getNetBalance());
    }

    private void clearInputs() {
        amountField.setText("");
        descriptionField.setText("");
        deadlineField.setText("");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "HATA", JOptionPane.ERROR_MESSAGE);
    }

    // ================= STYLE HELPERS =================
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(FG_TEXT);
        lbl.setFont(FONT_MAIN);
        return lbl;
    }

    private JTextField styleField(JTextField field) {
        field.setBackground(new Color(40, 40, 40));
        field.setForeground(FG_TEXT);
        field.setCaretColor(FG_TEXT);
        field.setFont(FONT_MAIN);
        field.setBorder(BorderFactory.createLineBorder(GRAY));
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(GRAY);
        btn.setForeground(FG_TEXT);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
    }

    private void styleLabel(JLabel lbl, Color color) {
        lbl.setForeground(color);
        lbl.setFont(FONT_BOLD);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
    }
}
