package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerTransactionAdapter extends RecyclerView.Adapter<RecyclerTransactionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransactionModel> transactionList;

    public RecyclerTransactionAdapter(Context context, ArrayList<TransactionModel> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the transaction_item layout
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transaction = transactionList.get(position);

        // Set the transaction details in the views
        holder.txt_notes.setText(transaction.notes);
        holder.txt_date.setText(transaction.date);
        holder.txt_time.setText(transaction.time);

        // Check the transaction type and set the amount accordingly
        if ("Credit".equals(transaction.type)) {
            holder.txt_cashIn.setText(String.valueOf(transaction.amount));
            holder.txt_cashOut.setText(""); // Empty for Credit
        } else if ("Debit".equals(transaction.type)) {
            holder.txt_cashOut.setText(String.valueOf(transaction.amount));
            holder.txt_cashIn.setText(""); // Empty for Debit
        } else {
            holder.txt_cashIn.setText("");
            holder.txt_cashOut.setText("");
        }





    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    // ViewHolder class that holds references to the views in each item
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_notes, txt_date, txt_time, txt_cashIn, txt_cashOut;
        LinearLayout transactionRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views from the layout
            txt_notes = itemView.findViewById(R.id.notes);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_cashIn = itemView.findViewById(R.id.cashIn);
            txt_cashOut = itemView.findViewById(R.id.cashOut);
            transactionRow = itemView.findViewById(R.id.transactionRow);
        }
    }
}
