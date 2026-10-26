package budgetmanagement.ui;

import budgetmanagement.dao.UserDAO;
import budgetmanagement.exception.DatabaseException;
import budgetmanagement.model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private final UserDAO userDao = new UserDAO();

    public RegisterFrame() {
        setTitle("Bütçe Yönetim Sistemi - Kayıt Ol");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Kayıt Ol");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Ad:"), gbc);
        firstNameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Soyad:"), gbc);
        lastNameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Şifre:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Şifre (Tekrar):"), gbc);
        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("Kayıt Ol");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        JButton backToLoginButton = new JButton("Zaten hesabım var, giriş yap");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(backToLoginButton, gbc);

        registerButton.addActionListener(e -> handleRegister());
        backToLoginButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(panel);
    }

    private void handleRegister() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanları doldur.", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Şifreler eşleşmiyor.", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (userDao.getUserByEmail(email) != null) {
                JOptionPane.showMessageDialog(this, "Bu email zaten kayıtlı.", "Hata", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User newUser = new User(0, firstName, lastName, password, email);
            userDao.addUser(newUser);

            JOptionPane.showMessageDialog(this, "Kayıt başarılı! Şimdi giriş yapabilirsin.",
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new LoginFrame().setVisible(true);
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
}