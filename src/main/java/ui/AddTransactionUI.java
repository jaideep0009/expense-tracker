package ui;

import service.ExpenseService;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

public class AddTransactionUI extends JFrame {

    private final ExpenseService expenseService = new ExpenseService();

    private JRadioButton incomeRadio;
    private JRadioButton expenseRadio;
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JDatePickerImpl datePicker;
    private JTextArea noteArea;
    private JButton saveButton;
    private JButton clearButton;

    // Categories
    private final String[] incomeCategories = { "Salary", "Freelance", "Investment", "Other Income" };
    private final String[] expenseCategories = { "Food", "Rent", "Bills", "Travel", "Shopping", "Entertainment", "Other Expense" };

    public AddTransactionUI() {
        setTitle("Add Transaction - Expense Tracker");
        setSize(520, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutForm();
        setVisible(true);
    }

    private void initComponents() {
        // Radio buttons for type
        incomeRadio = new JRadioButton("Income");
        expenseRadio = new JRadioButton("Expense", true); // default selected
        ButtonGroup bg = new ButtonGroup();
        bg.add(incomeRadio);
        bg.add(expenseRadio);

        incomeRadio.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateCategoryList(true);
            }
        });

        expenseRadio.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateCategoryList(false);
            }
        });

        // Category combo (starts with expense categories)
        categoryCombo = new JComboBox<>(expenseCategories);

        // Amount field
        amountField = new JTextField();
        amountField.setColumns(15);

        // Note area
        noteArea = new JTextArea(4, 20);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);

        // JDatePicker (popup calendar)
        UtilDateModel model = new UtilDateModel();
        // no default date selected; user must pick manually
        model.setSelected(false);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());

        // Buttons
        saveButton = new JButton("Save (" + "\u20B9" + ")"); // show INR symbol
        clearButton = new JButton("Clear");

        saveButton.addActionListener(e -> onSave());
        clearButton.addActionListener(e -> clearForm());
    }

    private void layoutForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Type label + radios
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Type:"), gbc);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.add(incomeRadio);
        radioPanel.add(expenseRadio);

        gbc.gridx = 1;
        panel.add(radioPanel, gbc);
        row++;

        // Category
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);
        row++;

        // Amount
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Amount (₹):"), gbc);

        gbc.gridx = 1;
        panel.add(amountField, gbc);
        row++;

        // Date (JDatePicker)
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        panel.add(datePicker, gbc);
        row++;

        // Note
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Note (optional):"), gbc);

        gbc.gridx = 1;
        JScrollPane noteScroll = new JScrollPane(noteArea);
        noteScroll.setPreferredSize(new Dimension(260, 90));
        panel.add(noteScroll, gbc);
        row++;

        // Buttons at bottom (centered)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(saveButton);
        btnPanel.add(clearButton);

        panel.add(btnPanel, gbc);

        add(panel);
    }

    private void updateCategoryList(boolean isIncome) {
        categoryCombo.removeAllItems();
        if (isIncome) {
            for (String c : incomeCategories) categoryCombo.addItem(c);
        } else {
            for (String c : expenseCategories) categoryCombo.addItem(c);
        }
    }

    private void onSave() {
        // Validate type
        String type = incomeRadio.isSelected() ? "INCOME" : "EXPENSE";

        // Validate category
        String category = (String) categoryCombo.getSelectedItem();
        if (category == null || category.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate amount
        String amountText = amountField.getText().trim();
        double amount;
        try {
            if (amountText.isEmpty()) throw new NumberFormatException();
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate date (manual selection required)
        UtilDateModel model = (UtilDateModel) datePicker.getModel();
        if (!model.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please pick a date from the calendar.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date selectedDate = (Date) model.getValue();
        LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String note = noteArea.getText() == null ? "" : noteArea.getText().trim();

        // Save using service
        try {
            if ("INCOME".equals(type)) {
                expenseService.addIncome(category, amount, localDate, note);
            } else {
                expenseService.addExpense(category, amount, localDate, note);
            }

            JOptionPane.showMessageDialog(this, "Transaction saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm(); // stay open and clear fields

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        // reset to default (Expense)
        expenseRadio.setSelected(true);
        updateCategoryList(false);
        amountField.setText("");
        noteArea.setText("");
        // clear date selection
        UtilDateModel model = (UtilDateModel) datePicker.getModel();
        model.setSelected(false);
    }

    // For testing individually
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddTransactionUI());
    }
}
