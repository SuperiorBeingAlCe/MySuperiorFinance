package com.yourname.financetracker.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.yourname.financetracker.core.FinanceXMLWriter;
import com.yourname.financetracker.core.Mode;
import com.yourname.financetracker.model.Debt;
import com.yourname.financetracker.model.Expense;
import com.yourname.financetracker.model.Income;
import com.yourname.financetracker.service.FinanceManager;


public class MainFrame extends JFrame {
    private final FinanceManager manager;
    private Mode currentMode = Mode.INCOME;

    private JTextField amountField = new JTextField();
    private JTextField descriptionField = new JTextField();
    private JTextField dateField = new JTextField();
    private JTextField deadlineField = new JTextField();

    private JComboBox<String> monthCombo;
    private JComboBox<Integer> dayCombo;

    private JLabel incomeLabel = new JLabel();
    private JLabel expenseLabel = new JLabel();
    private JLabel balanceLabel = new JLabel();

    private JButton incomeModeBtn = new JButton("GELİR");
    private JButton expenseModeBtn = new JButton("GİDER");
    private JButton debtModeBtn = new JButton("BORÇ");
    private JButton addBtn = new JButton("EKLE");
    private JButton showDetailsBtn = new JButton("TÜM DETAYLARI GÖSTER");

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
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_MAIN);

        // --- Summary Panel ---
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        summaryPanel.setBackground(BG_MAIN);
        summaryPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        styleLabel(incomeLabel, GREEN);
        styleLabel(expenseLabel, RED);
        styleLabel(balanceLabel, YELLOW);

        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_PANEL);
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TUTAR
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("TUTAR"), gbc);
        gbc.gridx = 1;
        formPanel.add(styleField(amountField, 200), gbc);

        // AÇIKLAMA
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("AÇIKLAMA"), gbc);
        gbc.gridx = 1;
        formPanel.add(styleField(descriptionField, 200), gbc);

        // TARİH
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("TARİH"), gbc);
        dateField.setText(LocalDate.now().toString());
        dateField.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(styleField(dateField, 150), gbc);

        // SON ÖDEME (BORÇ)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("SON ÖDEME (BORÇ)"), gbc);
        deadlineField.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(styleField(deadlineField, 150), gbc);

        // Deadline selectors
        setupDeadlineSelectors(formPanel, gbc);

        // Show Details Button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(styleButton(showDetailsBtn), gbc);

        // --- Bottom Panel ---
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

        // --- Listeners ---
        incomeModeBtn.addActionListener(e -> switchMode(Mode.INCOME));
        expenseModeBtn.addActionListener(e -> switchMode(Mode.EXPENSE));
        debtModeBtn.addActionListener(e -> switchMode(Mode.DEBT));
        addBtn.addActionListener(e -> handleAdd());
        showDetailsBtn.addActionListener(e -> showDetails());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FinanceXMLWriter.save(manager);
            }
        });

        switchMode(Mode.INCOME);
        refresh();
    }

    // --- Modern Field Styling ---
    private JTextField styleField(JTextField field, int width) {
        field.setPreferredSize(new Dimension(width, 28));
        field.setBackground(new Color(40, 40, 40));
        field.setForeground(FG_TEXT);
        field.setCaretColor(FG_TEXT);
        field.setFont(FONT_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return field;
    }

    private void setupDeadlineSelectors(JPanel formPanel, GridBagConstraints gbc) {
        LocalDate today = LocalDate.now();
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            LocalDate monthDate = today.plusMonths(i);
            months[i] = monthDate.getMonth().toString() + " " + monthDate.getYear();
        }
        monthCombo = new JComboBox<>(months);
        dayCombo = new JComboBox<>();
        updateDayCombo(today.getYear(), today.getMonthValue());

        ActionListener updateDeadline = e -> {
            int monthIndex = monthCombo.getSelectedIndex();
            LocalDate selectedMonth = today.plusMonths(monthIndex);
            int selectedDay = (Integer) dayCombo.getSelectedItem();
            int lastDay = selectedMonth.lengthOfMonth();
            if (selectedDay > lastDay) selectedDay = lastDay;
            deadlineField.setText(LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonth(), selectedDay).toString());
        };

        monthCombo.addActionListener(e -> {
            int monthIndex = monthCombo.getSelectedIndex();
            LocalDate selectedMonth = today.plusMonths(monthIndex);
            updateDayCombo(selectedMonth.getYear(), selectedMonth.getMonthValue());
            Integer day = (Integer) dayCombo.getSelectedItem();
            if (day != null) deadlineField.setText(LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonth(), day).toString());
        });
        dayCombo.addActionListener(updateDeadline);

        JPanel deadlinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        deadlinePanel.setBackground(BG_PANEL);
        deadlinePanel.add(monthCombo);
        deadlinePanel.add(dayCombo);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(deadlinePanel, gbc);
    }

    private void updateDayCombo(int year, int month) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        dayCombo.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) dayCombo.addItem(d);
    }

    // --- Styling & Helpers ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(FG_TEXT);
        lbl.setFont(FONT_MAIN);
        return lbl;
    }

    private JButton styleButton(JButton btn) {
        btn.setBackground(GRAY);
        btn.setForeground(FG_TEXT);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        return btn;
    }

    private void styleLabel(JLabel lbl, Color color) {
        lbl.setForeground(color);
        lbl.setFont(FONT_BOLD);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // --- Mode Switching ---
    private void switchMode(Mode mode) {
        currentMode = mode;
        resetModeButtons();

        incomeModeBtn.setBackground(GRAY);
        expenseModeBtn.setBackground(GRAY);
        debtModeBtn.setBackground(GRAY);

        switch (mode) {
            case INCOME -> incomeModeBtn.setBackground(GREEN);
            case EXPENSE -> expenseModeBtn.setBackground(RED);
            case DEBT -> debtModeBtn.setBackground(YELLOW);
        }

        boolean isDebt = mode == Mode.DEBT;
        deadlineField.setEnabled(isDebt);
        monthCombo.setEnabled(isDebt);
        dayCombo.setEnabled(isDebt);
    }

    private void resetModeButtons() {
        incomeModeBtn.setBackground(GRAY);
        expenseModeBtn.setBackground(GRAY);
        debtModeBtn.setBackground(GRAY);
    }

    // --- Add Entry ---
    private void handleAdd() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            LocalDate date = LocalDate.now();

            switch (currentMode) {
                case INCOME -> manager.addIncome(amount, date, description);
                case EXPENSE -> manager.addExpense(amount, date, description);
                case DEBT -> {
                    if (deadlineField.getText().isBlank()) { showError("BORÇ İÇİN SON TARİH ZORUNLU"); return; }
                    manager.addDebt(amount, date, description, LocalDate.parse(deadlineField.getText()));
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
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "HATA", JOptionPane.ERROR_MESSAGE);
    }

    private void showDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("==== GELİRLER ====\n");
        for (Income income : manager.getIncomes())
            sb.append("Tutar: ").append(income.getAmount())
              .append(", Tarih: ").append(income.getDate())
              .append(", Açıklama: ").append(income.getDescription())
              .append("\n");

        sb.append("\n==== GİDERLER ====\n");
        for (Expense expense : manager.getExpenses())
            sb.append("Tutar: ").append(expense.getAmount())
              .append(", Tarih: ").append(expense.getDate())
              .append(", Açıklama: ").append(expense.getDescription())
              .append("\n");

        sb.append("\n==== BORÇLAR ====\n");
        for (Debt debt : manager.getAllDebts())
            sb.append("Tutar: ").append(debt.getAmount())
              .append(", Tarih: ").append(debt.getDate())
              .append(", Açıklama: ").append(debt.getDescription())
              .append(", Son Tarih: ").append(debt.getDeadline())
              .append(", Ödendi mi? ").append(debt.isPaid() ? "Evet" : "Hayır")
              .append("\n");

        sb.append("\n==== ÖZET ====\n");
        sb.append("Toplam Gelir: ").append(manager.getTotalIncome()).append("\n");
        sb.append("Toplam Gider: ").append(manager.getTotalExpense()).append("\n");
        sb.append("Toplam Ödenmemiş Borç: ").append(manager.getTotalDebt()).append("\n");
        sb.append("Net Bakiye: ").append(manager.getNetBalance()).append("\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(40, 40, 40));
        textArea.setForeground(FG_TEXT);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 420));

        JOptionPane.showMessageDialog(this, scrollPane, "TÜM DETAYLAR", JOptionPane.INFORMATION_MESSAGE);
    }}

