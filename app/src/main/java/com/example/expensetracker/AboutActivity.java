package com.example.expensetracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        // Toolbar setup
        toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back); // Custom back arrow
            getSupportActionBar().setTitle("About App"); // Toolbar title set
        }

        // Email TextView setup
        email = findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open email app when email TextView clicked
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:ankit@example.com")); // Yahan apna email daalna
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query about Expense Tracker App");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    // No email client installed
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back button clicked
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
