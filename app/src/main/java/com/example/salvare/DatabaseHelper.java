package com.example.salvare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Salvare.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "amount REAL, " +
                "category TEXT, " +
                "description TEXT, " +
                "date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }

    public boolean addTransaction(String type, double amount,
                                  String category,
                                  String description, String date) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("type", type);
        values.put("amount", amount);
        values.put("category", category);
        values.put("description", description);
        values.put("date", date);

        long result = db.insert("transactions", null, values);

        return result != -1;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM transactions ORDER BY id DESC",
                null
        );
    }

    public double getTotal(String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactions WHERE type=?",
                new String[]{type}
        );

        double total = 0;

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        return total;
    }
}