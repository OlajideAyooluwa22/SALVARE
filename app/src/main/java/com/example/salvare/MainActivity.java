package com.example.salvare;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    TextView balanceText, incomeText, expenseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise database
        databaseHelper = new DatabaseHelper(this);

        // Connect TextViews to XML
        balanceText = findViewById(R.id.balanceText);
        incomeText = findViewById(R.id.incomeText);
        expenseText = findViewById(R.id.expenseText);

        // Connect buttons to XML
        Button addButton = findViewById(R.id.addButton);
        Button historyButton = findViewById(R.id.historyButton);

        // Add Transaction button
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddTransactionActivity.class
            );
            startActivity(intent);
        });

        // Transaction History button
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    HistoryActivity.class
            );
            startActivity(intent);
        });

        // Bottom Navigation Bar
        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            }

            if (itemId == R.id.nav_transactions) {
                startActivity(new Intent(
                        MainActivity.this,
                        HistoryActivity.class
                ));
                return true;
            }

            if (itemId == R.id.nav_statistics) {
                // Statistics screen to be connected later
                return true;
            }

            if (itemId == R.id.nav_profile) {
                // Profile screen to be connected later
                return true;
            }

            return false;
        });

        // Display dashboard information
        updateDashboard();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            updateDashboard();
        }
    }

    // Update income, expenses, balance and transactions
    private void updateDashboard() {

        double income = databaseHelper.getTotal("Income");
        double expenses = databaseHelper.getTotal("Expense");

        double balance = income - expenses;

        // Display total income
        incomeText.setText(String.format(
                Locale.UK,
                "Total Income: €%.2f",
                income
        ));

        // Display total expenses
        expenseText.setText(String.format(
                Locale.UK,
                "Total Expenses: €%.2f",
                expenses
        ));

        // Display balance
        balanceText.setText(String.format(
                Locale.UK,
                "Balance: €%.2f",
                balance
        ));

        // Retrieve saved transactions from SQLite
        Cursor cursor = databaseHelper.getAllTransactions();

        // Display recent transactions
        displayRecentTransactions(cursor);
    }

    // Display the latest 5 transactions
    private void displayRecentTransactions(Cursor cursor) {

        LinearLayout container =
                findViewById(R.id.recentTransactionsContainer);

        container.removeAllViews();

        try {

            int count = cursor.getCount();

            // Show latest 5 transactions
            int start = Math.max(0, count - 5);

            if (cursor.moveToPosition(start)) {

                int displayed = 0;

                do {

                    // Load transaction card layout
                    View card = LayoutInflater.from(this)
                            .inflate(
                                    R.layout.item_transaction,
                                    container,
                                    false
                            );

                    // Connect card TextViews
                    TextView description =
                            card.findViewById(
                                    R.id.transactionDescription
                            );

                    TextView details =
                            card.findViewById(
                                    R.id.transactionDetails
                            );

                    TextView amount =
                            card.findViewById(
                                    R.id.transactionAmount
                            );

                    // Get transaction description
                    description.setText(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "description"
                                    )
                            )
                    );

                    // Get category and date
                    String category = cursor.getString(
                            cursor.getColumnIndexOrThrow("category")
                    );

                    String date = cursor.getString(
                            cursor.getColumnIndexOrThrow("date")
                    );

                    details.setText(category + " • " + date);

                    // Get transaction amount
                    double value = cursor.getDouble(
                            cursor.getColumnIndexOrThrow("amount")
                    );

                    // Get transaction type
                    String type = cursor.getString(
                            cursor.getColumnIndexOrThrow("type")
                    );

                    // Display income or expense
                    if ("Expense".equalsIgnoreCase(type)) {

                        amount.setText(String.format(
                                Locale.UK,
                                "-€%.2f",
                                value
                        ));

                        amount.setTextColor(
                                Color.parseColor("#FF8277")
                        );

                    } else {

                        amount.setText(String.format(
                                Locale.UK,
                                "+€%.2f",
                                value
                        ));

                        amount.setTextColor(
                                Color.parseColor("#C4F500")
                        );
                    }

                    // Add transaction card to dashboard
                    container.addView(card);

                    displayed++;

                } while (cursor.moveToNext() && displayed < 5);
            }

        } finally {
            cursor.close();
        }
    }
}

