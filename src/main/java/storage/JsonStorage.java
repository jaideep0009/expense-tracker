package storage;

import model.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {

    private static final String FILE_PATH = "expenses.json";

    // Load all transactions from the JSON file
    public static List<Transaction> loadAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                // Create empty file with JSON array
                FileWriter fw = new FileWriter(FILE_PATH);
                fw.write("[]");
                fw.close();
            }

            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                transactions.add(Transaction.fromJSON(obj));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    // Append a new transaction to the JSON file
    public static void saveTransaction(Transaction t) {
        try {
            List<Transaction> transactions = loadAllTransactions();
            transactions.add(t);
            saveAll(transactions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save entire list back to file
    private static void saveAll(List<Transaction> transactions) {
        JSONArray array = new JSONArray();
        for (Transaction t : transactions) {
            array.put(t.toJSON());
        }

        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            fw.write(array.toString(4)); // formatted
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
