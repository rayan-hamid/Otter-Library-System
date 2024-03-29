package com.example.otterlibrarysystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

// Activity class responsible for managing the library system, including librarian authentication,
// viewing transaction logs, and adding new books to the system

public class ManageSystemActivity extends AppCompatActivity {
    private SystemDatabase db;
    private EditText inputUsername;
    private EditText inputPassword;
    private boolean showConfirmation = false;
    private Book newBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);

        // Initialize the Room database instance
        db = SystemDatabase.getInstance(this);

        // Reference the EditText fields in the layout
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);

        // Reference the "OK" button and set a click listener
        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate librarian credentials when the "OK" button is clicked
                validateLibrarian();
            }
        });
    }
    // onResume method is called when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        if (showConfirmation) {
            // Show the confirmation dialog
            ConfirmationDialog();

            showConfirmation = false;
        }
    }

    // Method to create a new Book object with the provided details
    private Book createNewBook(String title, String author, String genre) {
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setGenre(genre);
        return newBook;
    }

    // Method to display a dialog for entering new book details
    private void DataEntryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Book Details");

        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_data_entry, null);
        builder.setView(dialogView);

        // Reference the EditText fields in the dialog layout
        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText authorInput = dialogView.findViewById(R.id.authorInput);
        EditText genreInput = dialogView.findViewById(R.id.genreInput);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Retrieve entered data and save to the database
                String title = titleInput.getText().toString();
                String author = authorInput.getText().toString();
                String genre = genreInput.getText().toString();

                //new Book object
                 newBook = createNewBook(title, author, genre);

                // Save to the database
                db.saveNewBook(newBook);


                NewBookConfirmation();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Librarian canceled data entry, end use case
                finish();
            }
        });

        builder.show();
    }

    // Method to validate librarian credentials
    private void validateLibrarian() {
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();


        if (username.equals("!admin2") && password.equals("!admin2")) {
            // Librarian credentials are valid, display transaction logs
            TransactionLogs();
        } else {
            // Invalid credentials, show a message
            Toast.makeText(this, "Invalid librarian credentials", Toast.LENGTH_LONG).show();
        }
    }

    // Method to display transaction logs
    private void TransactionLogs() {
        // Retrieve transaction logs from the database

        List<TransactionLog> transactionLogs = db.getTransactionLogs();

        Log.d("ManageSystemActivity", "Transaction Logs Size: " + transactionLogs.size());

        // dialog to display transaction logs
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transaction Logs");

        if (!transactionLogs.isEmpty()) {
            // If transaction logs are available, create a message
            StringBuilder logsText = new StringBuilder();
            for (TransactionLog log : transactionLogs) {
                if ("New Account".equals(log.getTransactionType())) {
                    // Display logs for "New Account" transactions
                    logsText.append("Transaction type: ").append(log.getTransactionType()).append("\n")
                            .append("Customer’s username: ").append(log.getUsername()).append("\n\n");
                }
            }

            if (logsText.length() > 0) {
                builder.setMessage(logsText.toString());
            } else {
                builder.setMessage("No 'New Account' transactions available.");
            }
        } else {
            builder.setMessage("No transaction logs available.");
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                ConfirmationDialog();  // Call showConfirmationDialog method

            }
        });
        builder.show();
    }

    // Method to display a confirmation dialog
    private void ConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Book Confirmation");
        builder.setMessage("Do you have a new book to add to the system?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Librarian has a new book, show dialog for data entry
                DataEntryDialog();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Librarian doesn't have a new book, end use case

                // Navigate back to the MainActivity
                Intent intent = new Intent(ManageSystemActivity.this, MainActivity.class);
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
        builder.show();
    }

    // Method to display a confirmation dialog for a new book added to the system
    private void NewBookConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Book Added");

        String confirmationMessage = "Title: " + newBook.getTitle() + "\n"
                + "Author: " + newBook.getAuthor() + "\n"
                + "Genre: " + newBook.getGenre() + "\n\n"
                + "Confirm?";

        builder.setMessage("The new book has been added to the library system.\n" + confirmationMessage);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Librarian confirmed the information

                // Save the new book to the database
                db.saveNewBook(newBook);

                // Display a confirmation message
                Toast.makeText(ManageSystemActivity.this, "Book information confirmed!", Toast.LENGTH_LONG).show();

                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Librarian canceled the confirmation, end use case

                finish();
            }
        });

        builder.show();
    }


}
