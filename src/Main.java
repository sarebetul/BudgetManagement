import budgetmanagement.ui.LoginFrame;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        FontUIResource bigFont = new FontUIResource(new Font("SansSerif", Font.PLAIN, 18));
        for (Object key : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, bigFont);
            }
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}