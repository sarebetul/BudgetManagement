package budgetmanagement.ui;

import budgetmanagement.exception.DatabaseException;
import budgetmanagement.service.ExpenseAnalysisService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

public class CategoryAnalysisDialog extends JDialog {

    private final ExpenseAnalysisService analysisService = new ExpenseAnalysisService();

    public CategoryAnalysisDialog(JFrame parent, int userId) {
        super(parent, "Kategori Bazlı Harcama Analizi", true);

        setSize(parent.getSize());
        setLocationRelativeTo(parent);

        YearMonth thisMonth = YearMonth.now();
        LocalDate start = thisMonth.atDay(1);
        LocalDate end = thisMonth.atEndOfMonth();

        Map<String, BigDecimal> breakdown;
        try {
            breakdown = analysisService.getCategoryBreakdown(userId, start, end);
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            breakdown = new LinkedHashMap<>();
        }

        String[] columnNames = {"Kategori", "Toplam Harcama"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Map.Entry<String, BigDecimal> entry : breakdown.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JLabel titleLabel = new JLabel("  " + start + " - " + end);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        if (breakdown.isEmpty()) {
            JLabel emptyLabel = new JLabel("Bu ay henüz gider eklenmemiş.", SwingConstants.CENTER);
            add(emptyLabel, BorderLayout.SOUTH);
        }
    }
}