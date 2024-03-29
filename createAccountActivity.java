package com.example.otterlibrarysystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

// This class represents the Create Account Activity in the application.
// It allows users to create a new account by entering a unique username and password.
// The class validates user input, checks for reserved usernames, and logs the creation of a new account.
public class createAccountActivity extends AppCompatActivity {
    private SystemDatabase db;
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("createAccountActivity", "onCreate");
        setContentView(R.layout.activity_create_account);

        // Initialize the Room database instance
        db = SystemDatabase.getInstance(this);

        // Reference the username and password EditText fields in the layout
        usernameInput = findViewById(R.id.inputUsername);
        passwordInput = findViewById(R.id.inputPassword);

        // Reference "Create Account" button
        Button createAccountButton = findViewById(R.id.createAccount_button);

        // Set a click listener for the button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the createAccount method when the button is clicked
                createAccount();
            }
        });

        // For testing purposes: clear the database each time the app starts
        //clearDatabaseForTesting();



    }

    private int createAccountAttempts = 0;

    // Method to handle the process of creating a new user account
    private void createAccount() {

        // Get the entered username and password
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate that username and password are not blank
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be blank.", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if the username is reserved
        if (username.equals("!admin2")) {
            Toast.makeText(this, "Username !admin2 is reserved.", Toast.LENGTH_LONG).show();
            return;
        }

        UserDao userDao = db.userDao();

        // Check if the username is reserved and exceeded the limit
        if (isReservedUsername(username, userDao)) {
            // Display an error message and show the main menu
            Toast.makeText(this, "Error: Username limit exceeded", Toast.LENGTH_LONG).show();
            showMainMenu();
            return;
        }

        // Query the database for the given username
        User existingUser = userDao.getUserByUsername(username);

        // Check if the user with the given username already exists
        if (existingUser != null) {
            Toast.makeText(this, "Username already exists, please choose another", Toast.LENGTH_LONG).show();
            return;
        }

        //  create the user
        User newUser = new User();
        newUser.username = username;
        newUser.password = password;

        // Insert the new user into the database
        db.userDao().insert(newUser);

        // Log the transaction
        logTransaction("New Account", username);

        // Display a success message
        Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show();

        // Handle the login after successful account creation
        loginUser(username, password);

        // Display the main menu
        showMainMenu();
    }

    //  method to check if a reserved username exceeded the limit
    private boolean isReservedUsername(String username, UserDao userDao) {
        if (username.equals("hShuard") || username.equals("bMishra") || username.equals("shirleyBee")) {
            // Get the count of occurrences of the reserved username in the database
            int count = userDao.getCountByUsername(username);
            // Check if the count is greater than or equal to 2
            return count >= 2;
        }
        return false;
    }


    // Method to handle the case of a duplicate account
    private void handleDuplicateAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Username already exists")
                .setMessage("The username already exists. Please choose another.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Display the main menu
                        showMainMenu();
                    }
                })
                .show();
    }

    // Method to log a transaction
    private void logTransaction(String transactionType, String username) {
        // Log transaction
        SystemDatabase.getInstance(this).logTransaction(transactionType, username, null);
    }

    // Method to log in a user
    private void loginUser(String username, String password) {
        UserDao userDao = db.userDao();
        // Attempt to log in
        User loggedInUser = userDao.login(username, password);
        if (loggedInUser == null) {
            // Invalid login credentials
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
            return;
        }

    }
    private void showMainMenu() {
        // Intent to go back to MainActivity
        Intent i = new Intent(createAccountActivity.this, MainActivity.class);
        startActivity(i);


        finish();
    }
    // Method to clear the database for testing purposes
    /*private void clearDatabaseForTesting() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.clearAllTables(); // This assumes you have a method to clear all tables in your RoomDatabase
            }
        }).start();
    }*/
}
