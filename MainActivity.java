package com.example.otterlibrarysystem;


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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import com.example.otterlibrarysystem.databinding.ActivityMainBinding;

// This class represents the main activity of the application.
// It is the entry point, providing buttons to navigate to different functionalities, such as creating an account,
// placing a hold on a book, and managing the system.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button createAccountButton = findViewById(R.id.createAccount_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to handle create account
                createAccount();
            }
        });

        Button placeHoldButton = findViewById(R.id.hold_button);
        placeHoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               placeHold();
            }
        });

        Button manageSystemButton = findViewById(R.id.manage_button);
        manageSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSystem();
            }
        });

    }

    private void createAccount() {
        // launch CreateAccountActivity
        Intent i = new Intent(MainActivity.this, createAccountActivity.class);
        startActivity(i);
    }

    private void placeHold() {
        // Launch PlaceHoldActivity
        Intent i = new Intent(MainActivity.this, placeHoldActivity.class);
        startActivity(i);
    }

    private void manageSystem() {
        // Launch manageSystemActivity
        Intent i = new Intent(MainActivity.this, ManageSystemActivity.class);
        startActivity(i);
    }





}
