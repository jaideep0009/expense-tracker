package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("Expense Tracker");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false); // fixed size

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addButton = new JButton("Add Transaction");
        JButton historyButton = new JButton("View History");
        JButton reportButton = new JButton("View Reports");
        JButton exitButton = new JButton("Exit");

        // Add listeners (navigation placeholders for now)
        addButton.addActionListener(e -> openAddTransactionUI());
        historyButton.addActionListener(e -> openTransactionHistoryUI());
        reportButton.addActionListener(e -> openReportUI());
        exitButton.addActionListener(e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(addButton, gbc);

        gbc.gridy = 1;
        panel.add(historyButton, gbc);

        gbc.gridy = 2;
        panel.add(reportButton, gbc);

        gbc.gridy = 3;
        panel.add(exitButton, gbc);

        add(panel);
        setVisible(true);
    }

    private void openAddTransactionUI() {
        new AddTransactionUI();
    }

    private void openTransactionHistoryUI() {
        new TransactionHistoryUI();
    }

    private void openReportUI() {
        new ReportUI();
    }

    // MAIN ENTRY POINT
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard());
    }
}
