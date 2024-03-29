package com.example.otterlibrarysystem;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

// This class represents the Available Books Activity in the application.
// It allows users to view and potentially place holds on available books in a selected genre.
public class AvailableBooksActivity extends AppCompatActivity {
    private SystemDatabase db;
    private String selectedGenre;
    private int loginAttempts = 0;
    private HoldDao holdDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_books);

        // Initialize the database and HoldDao
        db = SystemDatabase.getInstance(this);
        holdDao = db.holdDao();

        // Retrieve the selected genre from the intent
        selectedGenre = getIntent().getStringExtra("genre");

        // Display the selected genre
        TextView genreTextView = findViewById(R.id.textGenre);
        genreTextView.setText("Selected Genre: " + selectedGenre);

        // Access the buttons in the XML layout
        Button book1Button = findViewById(R.id.book1Button);
        Button book2Button = findViewById(R.id.book2Button);
        Button book3Button = findViewById(R.id.book3Button);

        // Retrieve the books from the database
        List<Book> books = db.bookDao().getBooksByGenre(selectedGenre);

        // Check if there are available books
        if (!books.isEmpty()) {
            // Set text for each button based on book details
            book1Button.setText(books.get(0).title + " by " + books.get(0).author);
            book2Button.setText(books.get(1).title + " by " + books.get(1).author);
            book3Button.setText(books.get(2).title + " by " + books.get(2).author);

            // Set click listeners for the buttons
            book1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show confirmation dialog for the selected book
                    ConfirmationDialog(books.get(0));
                }
            });

            book2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show confirmation dialog for the selected book
                    ConfirmationDialog(books.get(1));
                }
            });

            book3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show confirmation dialog for the selected book
                    ConfirmationDialog(books.get(2));

                }
            });

        }
    }

    // Method to show confirmation dialog for placing a hold on a book
    private void ConfirmationDialog(Book selectedBook) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        // Check if the selected book is available for placing a hold
        if (isBookAvailableForHold(selectedBook)) {

            View dialogView = inflater.inflate(R.layout.confirmation_dialog_layout, null);
            builder.setView(dialogView);

            // Find the EditText views in the custom layout
            final EditText inputUsername = dialogView.findViewById(R.id.inputUsername);
            final EditText inputPassword = dialogView.findViewById(R.id.inputPassword);

            builder.setTitle("Confirm Reservation");
            builder.setMessage("Enter your username and password to confirm reservation");


            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    String username = inputUsername.getText().toString();
                    String password = inputPassword.getText().toString();

                    // Validate the user information
                    if (validateUser(username, password, selectedBook)) {
                        // Show the user information, prompt for confirmation, and log the transaction
                        showUserInfoAndConfirm(selectedBook, username);
                    } else {
                        // Handle invalid user credentials
                        handleInvalidUser();
                    }
                }
            });


            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
        } else {
            // Book is not available for hold, show a message
            builder.setTitle("Book Not Available");
            builder.setMessage("This book is not available for hold in the selected genre.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
        }

        builder.show();
    }
    // Method to check if the selected book is available for placing a hold
    private boolean isBookAvailableForHold(Book selectedBook) {
        // Check if the book is already on hold by another user in the same genre
        Hold existingHold = isBookOnHoldInGenre(selectedBook, selectedGenre, "");
        return existingHold == null;
    }

    // Method to validate user credentials
    private boolean validateUser(String username, String password, Book selectedBook) {
        // Check if the book is already on hold by another user in the same genre
        Hold existingHold = isBookOnHoldInGenre(selectedBook, selectedGenre, username);
        if (existingHold != null) {
            // Inform the user and exit
            NoBookAvailableDialog();
            return false;
        }

        UserDao userDao = db.userDao();
        User loggedInUser = userDao.login(username, password);
        return loggedInUser != null;
    }

    // Method to check if the selected book is on hold in the specified genre for another user
    private Hold isBookOnHoldInGenre(Book selectedBook, String genre, String username) {
        return holdDao.getHoldByUsernameAndBookId(selectedBook.bookId, username, genre);

    }

    // Method to handle the case when a selected book is not available for hold
    private void NoBookAvailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Book Available");
        builder.setMessage("This book is already on hold in the selected genre. Exiting the program.");


        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Display the main menu
                showMainMenu();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    // Method to handle invalid user credentials
    private void handleInvalidUser() {
        // Prompt user to re-enter username and password
        Toast.makeText(AvailableBooksActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_LONG).show();
        loginAttempts++;

        // Check if login attempts exceed the limit
        if (loginAttempts >= 2) {
            // Display an error message and show the main menu
            Toast.makeText(AvailableBooksActivity.this, "Invalid credentials. Returning to main menu.", Toast.LENGTH_LONG).show();
            showMainMenu();
        }
    }

    // Method to display user information and prompt for confirmation
    private void showUserInfoAndConfirm(Book selectedBook, String username) {
        // Display the user's username, book title, and reservation number
        String userInfo = "Username: " + username + "\nBook Title: " + selectedBook.title + "\nReservation Number: " + generateReservationNumber();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Reservation");
        builder.setMessage(userInfo);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // Log the transaction
                logTransaction("Place Hold", username, generateReservationNumber());

                // Handle the confirmation
                Toast.makeText(AvailableBooksActivity.this, "Reservation Confirmed!", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Method to generate a random reservation number
    private String generateReservationNumber() {
        // Generate a random reservation number
        int length = 6;
        String reservationNumber = "";

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0 to 9)
            reservationNumber += digit;
        }

        return reservationNumber;
    }

    private void logTransaction(String transactionType, String username, String reservationNumber) {
        // Log transaction
        String transactionInfo = "Transaction Type: " + transactionType
                + "\nUsername: " + username
                + "\nReservation Number: " + reservationNumber;
        Log.d("Transaction Log", transactionInfo);

        // Show the main menu
        showMainMenu();
    }

    private void showMainMenu() {
        // Intent to go back to MainActivity
        Intent i = new Intent(AvailableBooksActivity.this, MainActivity.class);
        startActivity(i);

        // Finish the current activity
        finish();
    }

}