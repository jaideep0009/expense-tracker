package model;

import java.time.LocalDate;
import java.util.UUID;
import org.json.JSONObject;

public class Transaction {
    private String id;
    private String type;         // INCOME or EXPENSE
    private String category;
    private double amount;
    private LocalDate date;
    private String note;         

    // Constructor for new transaction (ID auto-generated)
    public Transaction(String type, String category, double amount, LocalDate date, String note) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    // Constructor for loading from JSON 
    public Transaction(String id, String type, String category, double amount, LocalDate date, String note) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    // Getters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getNote() { return note; }

    // Convert to JSON object for saving
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", type);
        json.put("category", category);
        json.put("amount", amount);
        json.put("date", date.toString()); // yyyy-MM-dd
        json.put("note", note == null ? "" : note);
        return json;
    }

    // Convert from JSON object to Transaction object
    public static Transaction fromJSON(JSONObject json) {
        return new Transaction(
            json.getString("id"),
            json.getString("type"),
            json.getString("category"),
            json.getDouble("amount"),
            LocalDate.parse(json.getString("date")),
            json.optString("note", "")
        );
    }
}
