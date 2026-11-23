package com.example.salvare;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ListView transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        databaseHelper = new DatabaseHelper(this);
        transactionList = findViewById(R.id.transactionList);

        displayTransactions();
    }

    private void displayTransactions() {

        ArrayList<String> transactions = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllTransactions();

        try {
            while (cursor.moveToNext()) {

                String type = cursor.getString(
                        cursor.getColumnIndexOrThrow("type"));

                double amount = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("amount"));

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow("category"));

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description"));

                String date = cursor.getString(
                        cursor.getColumnIndexOrThrow("date"));

                String item = String.format(
                        Locale.UK,
                        "%s: €%.2f\n%s - %s\n%s",
                        type, amount, category, description, date
                );

                transactions.add(item);
            }
        } finally {
            cursor.close();
        }

        if (transactions.isEmpty()) {
            transactions.add("No transactions saved yet.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                transactions
        );

        transactionList.setAdapter(adapter);
    }
}
