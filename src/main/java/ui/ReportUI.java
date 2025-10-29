package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.Transaction;
import service.ExpenseService;

public class ReportUI extends JFrame {
	private ExpenseService service=new ExpenseService();
    private JTabbedPane tabbedPane;

    public ReportUI() {
        setTitle("Expense Reports");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        tabbedPane.add("Daily", createDailyTab());
        tabbedPane.add("Monthly", createMonthlyTab());
        tabbedPane.add("Yearly", createYearlyTab());

        add(tabbedPane);
        setVisible(true);
    }

    // Daily Tab (UI skeleton)
 // ================= DAILY TAB =================

    private JPanel createDailyTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Date Picker
        UtilDateModel model = new UtilDateModel();
        model.setSelected(false);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());

        // Generate Button
        JButton generateBtn = new JButton("Generate Report");

        // Summary Labels
        JLabel incomeLabel = new JLabel("Total Income: ₹ 0");
        JLabel expenseLabel = new JLabel("Total Expense: ₹ 0");
        JLabel balanceLabel = new JLabel("Balance: ₹ 0");

        // Add components to layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Date:"), gbc);

        gbc.gridx = 1;
        panel.add(datePicker, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(generateBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(incomeLabel, gbc);

        gbc.gridy = 3;
        panel.add(expenseLabel, gbc);

        gbc.gridy = 4;
        panel.add(balanceLabel, gbc);

        // Button logic
        generateBtn.addActionListener(e -> {
            if (!model.isSelected()) {
                JOptionPane.showMessageDialog(panel, "Please select a date.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Date selected = (Date) model.getValue();
            LocalDate localDate = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Get list of transactions from service
            List<Transaction> transactions = service.getAllTransactions();

            double totalIncome = 0, totalExpense = 0;
            for (Transaction t : transactions) {
                if (t.getDate().equals(localDate)) {
                    if (t.getType().equals("INCOME")) {
                        totalIncome += t.getAmount();
                    } else {
                        totalExpense += t.getAmount();
                    }
                }
            }
            double balance = totalIncome - totalExpense;

            // Update labels
            incomeLabel.setText("Total Income: ₹ " + totalIncome);
            expenseLabel.setText("Total Expense: ₹ " + totalExpense);
            balanceLabel.setText("Balance: ₹ " + balance);
        });

        return panel;
    }

    // Monthly Tab (UI skeleton)
 // ================= MONTHLY TAB =================

    private JPanel createMonthlyTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Month dropdown
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        JComboBox<String> monthCombo = new JComboBox<>(months);

        // Year dropdown (let's list years from current year back to 2000)
        JComboBox<Integer> yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y >= 2000; y--) {
            yearCombo.addItem(y);
        }

        JButton generateBtn = new JButton("Generate Report");

        JLabel incomeLabel = new JLabel("Total Income: ₹ 0");
        JLabel expenseLabel = new JLabel("Total Expense: ₹ 0");
        JLabel balanceLabel = new JLabel("Balance: ₹ 0");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Month:"), gbc);

        gbc.gridx = 1;
        panel.add(monthCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Select Year:"), gbc);

        gbc.gridx = 1;
        panel.add(yearCombo, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(generateBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(incomeLabel, gbc);

        gbc.gridy = 4;
        panel.add(expenseLabel, gbc);

        gbc.gridy = 5;
        panel.add(balanceLabel, gbc);

        // Logic on button click
        generateBtn.addActionListener(e -> {
            int selectedMonth = monthCombo.getSelectedIndex() + 1;  // month: 1-12
            int selectedYear = (int) yearCombo.getSelectedItem();

            List<Transaction> transactions = service.getAllTransactions();

            double totalIncome = 0, totalExpense = 0;

            for (Transaction t : transactions) {
                LocalDate d = t.getDate();
                if (d.getYear() == selectedYear && d.getMonthValue() == selectedMonth) {
                    if (t.getType().equals("INCOME")) {
                        totalIncome += t.getAmount();
                    } else {
                        totalExpense += t.getAmount();
                    }
                }
            }

            double balance = totalIncome - totalExpense;

            incomeLabel.setText("Total Income: ₹ " + totalIncome);
            expenseLabel.setText("Total Expense: ₹ " + totalExpense);
            balanceLabel.setText("Balance: ₹ " + balance);
        });

        return panel;
    }
 // ================= YEARLY TAB =================

    private JPanel createYearlyTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Year dropdown
        JComboBox<Integer> yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y >= 2000; y--) {
            yearCombo.addItem(y);
        }

        JButton generateBtn = new JButton("Generate Report");

        JLabel incomeLabel = new JLabel("Total Income: ₹ 0");
        JLabel expenseLabel = new JLabel("Total Expense: ₹ 0");
        JLabel balanceLabel = new JLabel("Balance: ₹ 0");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Year:"), gbc);

        gbc.gridx = 1;
        panel.add(yearCombo, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(generateBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(incomeLabel, gbc);

        gbc.gridy = 3;
        panel.add(expenseLabel, gbc);

        gbc.gridy = 4;
        panel.add(balanceLabel, gbc);

        // Logic for yearly summary
        generateBtn.addActionListener(e -> {
            int selectedYear = (int) yearCombo.getSelectedItem();

            List<Transaction> transactions = service.getAllTransactions();

            double totalIncome = 0, totalExpense = 0;

            for (Transaction t : transactions) {
                LocalDate d = t.getDate();
                if (d.getYear() == selectedYear) {
                    if (t.getType().equals("INCOME")) {
                        totalIncome += t.getAmount();
                    } else {
                        totalExpense += t.getAmount();
                    }
                }
            }

            double balance = totalIncome - totalExpense;

            incomeLabel.setText("Total Income: ₹ " + totalIncome);
            expenseLabel.setText("Total Expense: ₹ " + totalExpense);
            balanceLabel.setText("Balance: ₹ " + balance);
        });

        return panel;
    }


}

