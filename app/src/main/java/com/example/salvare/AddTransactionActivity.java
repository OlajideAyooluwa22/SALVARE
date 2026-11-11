package com.example.salvare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    EditText amountInput, descriptionInput;
    Spinner typeSpinner, categorySpinner;
    ImageView receiptImage;

    ActivityResultLauncher<Void> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        databaseHelper = new DatabaseHelper(this);

        amountInput = findViewById(R.id.amountInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        typeSpinner = findViewById(R.id.typeSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);
        receiptImage = findViewById(R.id.receiptImage);

        Button saveButton = findViewById(R.id.saveButton);
        Button cameraButton = findViewById(R.id.cameraButton);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                (Bitmap bitmap) -> {
                    if (bitmap != null) {
                        receiptImage.setImageBitmap(bitmap);
                        Toast.makeText(this, "Receipt photo captured",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraButton.setOnClickListener(v -> {
            cameraLauncher.launch(null);
        });

        saveButton.setOnClickListener(v -> saveTransaction());
    }

    private void saveTransaction() {

        String amountText = amountInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (amountText.isEmpty()) {
            amountInput.setError("Enter an amount");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            amountInput.setError("Enter a valid amount");
            return;
        }

        if (amount <= 0) {
            amountInput.setError("Amount must be greater than zero");
            return;
        }

        String type = typeSpinner.getSelectedItem().toString();
        String category = categorySpinner.getSelectedItem().toString();

        String date = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.UK
        ).format(new Date());

        boolean saved = databaseHelper.addTransaction(
                type, amount, category, description, date
        );

        if (saved) {
            Toast.makeText(this, "Transaction saved!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Could not save transaction",
                    Toast.LENGTH_SHORT).show();
        }
    }
}