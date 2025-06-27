package com.example.expensetracker;

public class TransactionModel {

    int amount, id;
    String notes, time, date, type;

    public  TransactionModel(){

    }

    public TransactionModel(int amount, String notes, String date, String time, String type) {
        this.amount = amount;
        this.notes = notes;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    // Getter methods (optional, if needed)
    public int getAmount() {
        return amount;
    }

    public String getNotes() {
        return notes;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
