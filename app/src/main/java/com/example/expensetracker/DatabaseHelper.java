package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TransactionDB";
    private static final int DATABASE_ID = 1;

    private static final String TABLE_TRANSACTION = "transactions";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TRANSACTION_TYPE = "transaction_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_ID);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TRANSACTION + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT, " +
                KEY_DATE + " TEXT, " +
                KEY_TIME + " TEXT, " +
                KEY_AMOUNT + " INTEGER, " +
                KEY_TRANSACTION_TYPE + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(db);
    }

    public void addTransaction(String title, String date, String time, int amount, String transaction_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME, time);
        contentValues.put(KEY_AMOUNT, amount);
        contentValues.put(KEY_TRANSACTION_TYPE, transaction_type);

        db.insert(TABLE_TRANSACTION, null, contentValues);
        db.close();
    }

    public ArrayList<TransactionModel> fetchTransaction() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION, null);

        ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                TransactionModel transactionModel = new TransactionModel();
                transactionModel.id = cursor.getInt(0);
                transactionModel.notes = cursor.getString(1);
                transactionModel.date = cursor.getString(2);
                transactionModel.time = cursor.getString(3);
                transactionModel.amount = cursor.getInt(4);
                transactionModel.type = cursor.getString(5);

                transactionModelArrayList.add(transactionModel);
            }
            cursor.close();
        }

        db.close();
        return transactionModelArrayList;
    }

    public int fetchTotalCredit() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_TRANSACTION + " WHERE " + KEY_TRANSACTION_TYPE + " = ?", new String[]{"Credit"});

        int totalCreditAmount = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                totalCreditAmount = cursor.getInt(0);
            }
            cursor.close();
        }

        db.close();
        return totalCreditAmount;
    }

    public int fetchTotalDebit() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_TRANSACTION + " WHERE " + KEY_TRANSACTION_TYPE + " = ?", new String[]{"Debit"});
        int totalDebitAmount = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                totalDebitAmount = cursor.getInt(0);
            }
            cursor.close();
        }

        db.close();
        return totalDebitAmount;
    }








}
