package com.example.otterlibrarysystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;
import java.util.ArrayList;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.room.Room;

import androidx.appcompat.app.AppCompatActivity;

// This class represents the activity for placing holds on books in different genres.
// It provides buttons for selecting genres (Computer Science, Memoir, Fiction),
// and upon selection, it navigates to the AvailableBooksActivity to display available books in the chosen genre.

public class placeHoldActivity extends AppCompatActivity {
    private SystemDatabase db;
    private Button buttonComputerScience;
    private Button buttonMemoir;
    private Button buttonFiction;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_hold);

        // Initialize the Room database instance
        db = SystemDatabase.getInstance(this);

        // Reference the genre selection buttons in the layout
        buttonComputerScience = findViewById(R.id.buttonComputerScience);
        buttonMemoir = findViewById(R.id.buttonMemoir);
        buttonFiction = findViewById(R.id.buttonFiction);

        buttonComputerScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AvailableBooksActivity with Computer Science genre
                startAvailableBooksActivity("Computer Science");
            }
        });

        buttonMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AvailableBooksActivity with Memoir genre
                startAvailableBooksActivity("Memoir");
            }
        });

        buttonFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AvailableBooksActivity with Fiction genre
                startAvailableBooksActivity("Fiction");
            }
        });
    }

    // Method to start AvailableBooksActivity with the selected genre
    private void startAvailableBooksActivity(String genre) {
        Intent i = new Intent(placeHoldActivity.this, AvailableBooksActivity.class);
        i.putExtra("genre", genre);
        startActivity(i);
    }
}
