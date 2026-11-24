
package com.example.salvare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

        // Display dashboard information
        updateDashboard();
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
                // Open your transaction screen
                return true;
            }

            if (itemId == R.id.nav_statistics) {
                // Open your statistics screen
                return true;
            }

            if (itemId == R.id.nav_profile) {
                // Open your profile screen
                return true;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            updateDashboard();
        }
    }

    // Update income, expenses and balance
    private void updateDashboard() {

        double income = databaseHelper.getTotal("Income");
        double expenses = databaseHelper.getTotal("Expense");
        double balance = income - expenses;

        incomeText.setText(String.format(
                Locale.UK,
                "Total Income: €%.2f",
                income
        ));

        expenseText.setText(String.format(
                Locale.UK,
                "Total Expenses: €%.2f",
                expenses
        ));

        balanceText.setText(String.format(
                Locale.UK,
                "Balance: €%.2f",
                balance
        ));
    }
}
