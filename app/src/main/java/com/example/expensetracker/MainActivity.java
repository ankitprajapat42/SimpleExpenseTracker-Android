package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton addTransaction_btn;

    private TextView totalCreditAmountText, totalDebitAmountText, currentBalance;

    private RecyclerView recyclerView_transaction;
    private ArrayList<TransactionModel> transaction_list = new ArrayList<>();
    private RecyclerTransactionAdapter recyclerTransactionAdapter;

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up window insets to avoid overlapping system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        // Set Up Toolbar
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);






        // Button to add transaction
        addTransaction_btn = findViewById(R.id.addTransaction_btn);
        addTransaction_btn.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        addTransaction_btn.setOnClickListener(view -> {
            // Create and show the dialog
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.add_transaction_dialog);

            // Get references to the views inside the dialog
            EditText editTextNotes = dialog.findViewById(R.id.edit_text_notes);
            EditText editTextAmount = dialog.findViewById(R.id.edit_text_amount);
            EditText editTextDate = dialog.findViewById(R.id.edit_text_date);
            editTextDate.setOnClickListener(view1 -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, // Correct context
                        (view12, year1, monthOfYear, dayOfMonth) -> {
                            // Format selected date
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(year1, monthOfYear, dayOfMonth);

                            // Use your desired date format
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String dateString = dateFormat.format(selectedDate.getTime());

                            // Set selected date in EditText
                            editTextDate.setText(dateString);
                        },
                        year, month, day);

                // Show DatePickerDialog
                datePickerDialog.show();

                // Get current mode (Dark or Light)
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                // Set OK button text color based on the mode
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    // Dark Mode: set white text color
                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    // Light Mode: set black text color
                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
                }



            });

            EditText editTextTime = dialog.findViewById(R.id.edit_text_time);
            editTextTime.setOnClickListener(view12 -> {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MainActivity.this, // Correct context
                        (view1, hourOfDay, minute1) -> {
                            // Format selected time
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String timeString = timeFormat.format(calendar.getTime());

                            // Set selected time in EditText
                            editTextTime.setText(timeString);
                        },
                        hour, minute, true); // 24-hour format

                // Show TimePickerDialog
                timePickerDialog.show();

                // Get current mode (Dark or Light)
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;


                // Set OK button text color based on the mode
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    // Dark Mode: set white text color
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    // Light Mode: set black text color
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
                }

            });

            RadioGroup radiotransactionType = dialog.findViewById(R.id.transaction_type);
            // Set default selection to the first radio button (index 0)
            radiotransactionType.check(radiotransactionType.getChildAt(0).getId());

            Button actionBtn = dialog.findViewById(R.id.action_btn);

            actionBtn.setOnClickListener(view13 -> {
                int selectedTransactionId = radiotransactionType.getCheckedRadioButtonId();
                String transactionType = "Not specified";

                if (selectedTransactionId != -1) {
                    RadioButton selectedRadioButton = dialog.findViewById(selectedTransactionId);
                    transactionType = selectedRadioButton.getText().toString();
                }

                if (!editTextNotes.getText().toString().equals("") &&
                        !editTextAmount.getText().toString().equals("") &&
                        !editTextDate.getText().toString().equals("") &&
                        !editTextTime.getText().toString().equals("")) {

                    String notes = editTextNotes.getText().toString();
                    int amount = 0;

                    try {
                        amount = Integer.parseInt(editTextAmount.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid amount!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String date = editTextDate.getText().toString();
                    String time = editTextTime.getText().toString();

                    // Add transaction to database
                    databaseHelper.addTransaction(notes, date, time, amount, transactionType);

                    // Create new TransactionModel
                    TransactionModel newTransaction = new TransactionModel();
                    newTransaction.notes = notes;
                    newTransaction.amount = amount;
                    newTransaction.date = date;
                    newTransaction.time = time;
                    newTransaction.type = transactionType;

                    // Add to the list and notify adapter
                    transaction_list.add(0, newTransaction);
                    recyclerTransactionAdapter.notifyItemInserted(0);  // Notify the adapter about the item at position 0
                    recyclerView_transaction.scrollToPosition(0);  // Scroll to the top (position 0)

                    // Close dialog
                    dialog.dismiss();

                    // Update Total Credit and Debit Amounts
                    updateTotalAmounts();
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all details!", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        });

        // Initialize Total Amount Text Views
        totalCreditAmountText = findViewById(R.id.totalCreditAmount_txt);
        totalDebitAmountText = findViewById(R.id.totalDebitAmount_txt);
        currentBalance = findViewById(R.id.balanceAmount_txt);

        // Initialize RecyclerView and adapter
        recyclerView_transaction = findViewById(R.id.transation_recyclerView);
        recyclerView_transaction.setLayoutManager(new LinearLayoutManager(this));

        // Fetch transactions from the database
        transaction_list.addAll(databaseHelper.fetchTransaction());

        // Set up adapter
        recyclerTransactionAdapter = new RecyclerTransactionAdapter(this, transaction_list);
        recyclerView_transaction.setAdapter(recyclerTransactionAdapter);

        // Update the initial total amounts
        updateTotalAmounts();
    }

    // Method to update Total Credit and Debit amounts
    private void updateTotalAmounts() {
        int totalCredit = databaseHelper.fetchTotalCredit();
        int totalDebit = databaseHelper.fetchTotalDebit();

        // Ensure default value if there are no transactions
        if (totalCredit == -1) {
            totalCredit = 0;  // Set default value if fetching failed
        }

        if (totalDebit == -1) {
            totalDebit = 0;  // Set default value if fetching failed
        }

        totalCreditAmountText.setText(String.valueOf(totalCredit));
        totalDebitAmountText.setText(String.valueOf(totalDebit));

        int balance = totalCredit - totalDebit;
        currentBalance.setText(String.valueOf(balance));

        if (balance < 0) {
            currentBalance.setTextColor(Color.RED);  // Red for negative balance
        } else {
            currentBalance.setTextColor(Color.GREEN);  // Green for positive balance
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh transaction list and update UI after data deletion or changes
        transaction_list.clear();
        transaction_list.addAll(databaseHelper.fetchTransaction());
        recyclerTransactionAdapter.notifyDataSetChanged();
        updateTotalAmounts(); // Refresh the totals
    }

    // Menu ko inflate karne ke liye
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Menu item ko select karne ke liye
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }




}
