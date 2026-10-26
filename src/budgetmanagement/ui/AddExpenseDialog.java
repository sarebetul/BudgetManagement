package budgetmanagement.ui;

import budgetmanagement.dao.CategoryDAO;
import budgetmanagement.dao.ExpenseDAO;
import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.Category;
import budgetmanagement.model.Expense;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AddExpenseDialog extends JDialog {

    private JComboBox<Category> categoryCombo;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;

    private final CategoryDAO categoryDao = new CategoryDAO();
    private final ExpenseDAO expenseDao = new ExpenseDAO();
    private final int userId;

    public AddExpenseDialog(JFrame parent, int userId) {
        super(parent, "Gider Ekle", true);
        this.userId = userId;

        setSize(parent.getSize());
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Kategori:"), gbc);
        categoryCombo = new JComboBox<>();
        loadCategories();
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(categoryCombo, gbc);

        JButton newCategoryButton = new JButton("Yeni Kategori Ekle");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(newCategoryButton, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Açıklama:"), gbc);
        descriptionField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tutar:"), gbc);
        amountField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Tarih (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(LocalDate.now().toString(), 15);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(dateField, gbc);

        JButton saveButton = new JButton("Kaydet");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        newCategoryButton.addActionListener(e -> handleAddCategory());
        saveButton.addActionListener(e -> handleSave());

        add(panel);
    }

    private void loadCategories() {
        try {
            categoryCombo.removeAllItems();
            List<Category> categories = categoryDao.getCategoriesByUserId(userId);
            for (Category category : categories) {
                categoryCombo.addItem(category);
            }
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddCategory() {
        String name = JOptionPane.showInputDialog(this, "Yeni kategori adı:");
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        try {
            Category newCategory = new Category(0, userId, name.trim(), false);
            categoryDao.addCategory(newCategory, userId);
            loadCategories();
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSave() {
        Category selectedCategory = (Category) categoryCombo.getSelectedItem();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String dateText = dateField.getText().trim();

        if (selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Önce bir kategori ekle veya seç.", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (description.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
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
            expenseDao.addExpense(new Expense(0, selectedCategory.getId(), amount, description, date), userId);
            dispose();
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
}