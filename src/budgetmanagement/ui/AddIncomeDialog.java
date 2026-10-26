package budgetmanagement.ui;

import budgetmanagement.dao.IncomeDAO;
import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Income;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AddIncomeDialog extends JDialog {

    private JTextField nameField;
    private JTextField amountField;
    private JTextField dateField;
    private final IncomeDAO incomeDao = new IncomeDAO();
    private final int userId;

    public AddIncomeDialog(JFrame parent, int userId) {
        super(parent, "Gelir Ekle", true);
        this.userId = userId;

        setSize(parent.getSize());
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Gelir Adı:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tutar:"), gbc);
        amountField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Tarih (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(LocalDate.now().toString(), 15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(dateField, gbc);

        JButton saveButton = new JButton("Kaydet");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> handleSave());

        add(panel);
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String amountText = amountField.getText().trim();
        String dateText = dateField.getText().trim();

        if (name.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanları doldur.", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal amount;
        LocalDate date;
        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tutar geçerli bir sayı olmalı.", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            date = LocalDate.parse(dateText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Tarih formatı YYYY-MM-DD olmalı.", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            incomeDao.addIncome(new Income(0, name, amount, date), userId);
            dispose();
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
}