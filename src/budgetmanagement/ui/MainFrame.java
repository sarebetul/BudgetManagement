package budgetmanagement.ui;

import budgetmanagement.dao.ExpenseDAO;
import budgetmanagement.dao.IncomeDAO;
import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class MainFrame extends JFrame {

    private final User currentUser;
    private final IncomeDAO incomeDao = new IncomeDAO();
    private final ExpenseDAO expenseDao = new ExpenseDAO();

    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel remainingLabel;

    public MainFrame(User user) {
        this.currentUser = user;

        setTitle("Bütçe Yönetim Sistemi - Ana Ekran");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel welcomeLabel = new JLabel("Hoş geldin, " + currentUser.getFirstName() + "!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(3, 1, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Bu Ayın Özeti"));

        incomeLabel = new JLabel();
        expenseLabel = new JLabel();
        remainingLabel = new JLabel();

        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(remainingLabel);

        mainPanel.add(summaryPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));

        JButton addIncomeButton = new JButton("Gelir Ekle");
        JButton addExpenseButton = new JButton("Gider Ekle");
        JButton analysisButton = new JButton("Kategori Analizi");
        JButton logoutButton = new JButton("Çıkış Yap");

        buttonPanel.add(addIncomeButton);
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(analysisButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        addIncomeButton.addActionListener(e -> {
            new AddIncomeDialog(this, currentUser.getId()).setVisible(true);
            refreshSummary();
        });

        addExpenseButton.addActionListener(e -> {
            new AddExpenseDialog(this, currentUser.getId()).setVisible(true);
            refreshSummary();
        });

        analysisButton.addActionListener(e ->
                new CategoryAnalysisDialog(this, currentUser.getId()).setVisible(true));

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        refreshSummary();
    }

    private void refreshSummary() {
        YearMonth thisMonth = YearMonth.now();
        LocalDate start = thisMonth.atDay(1);
        LocalDate end = thisMonth.atEndOfMonth();

        try {
            BigDecimal totalIncome = incomeDao.sumIncomeByUserAndDateRange(currentUser.getId(), start, end);
            BigDecimal totalExpense = expenseDao.sumExpenseByUserAndDateRange(currentUser.getId(), start, end);
            BigDecimal remaining = totalIncome.subtract(totalExpense);

            incomeLabel.setText("Toplam Gelir: " + totalIncome);
            expenseLabel.setText("Toplam Gider: " + totalExpense);
            remainingLabel.setText("Kalan: " + remaining);
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
}