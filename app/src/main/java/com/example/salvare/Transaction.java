package com.example.salvare;


public class Transaction {

    private String description;
    private String category;
    private String date;
    private double amount;

    public Transaction(String description,
                       String category,
                       String date,
                       double amount) {

        this.description = description;
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
