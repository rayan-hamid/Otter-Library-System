package com.example.otterlibrarysystem;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

// This class represents the Room Database for the Android application, managing entities User, Book, Hold, and TransactionLog.
// It provides data access objects for each entity and includes methods for database operations, transaction logging, and data population.
@Database(entities = {User.class, Book.class, Hold.class, TransactionLog.class}, version = 2, exportSchema = false)
public abstract class SystemDatabase extends RoomDatabase {

    // Data access object methods for each entity
    public abstract UserDao userDao();
    public abstract BookDao bookDao();
    public abstract HoldDao holdDao();
    public abstract TransactionLogDao transactionLogDao();




    private static SystemDatabase sInstance;

    // Method to get or create a database instance
    public static synchronized SystemDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            SystemDatabase.class, "system.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

            // Call the method to populate initial data
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    // Method to save a new user account to the database
    public void saveNewAccount(User user) {
        UserDao userDao = userDao();
        userDao.insert(user);

        // Log the "New Account" transaction
        logTransaction("New Account", user.username, "Account created: " + user.username);
    }

    // Method to save a new book to the database
    public void saveNewBook(Book book) {
        bookDao().saveNewBook(book);
    }

    // Method to log a transaction to the TransactionLog entity
    public void logTransaction(String transactionType, String username, String details) {
        TransactionLogDao transactionLogDao = transactionLogDao();
        TransactionLog log = new TransactionLog();
        log.setTransactionType(transactionType);
        log.setUsername(username);
        log.setDetails(details);
        transactionLogDao.insert(log);
    }

    // Method to retrieve all transaction logs from the database
    public List<TransactionLog> getTransactionLogs() {
        TransactionLogDao transactionLogDao = transactionLogDao();
        return transactionLogDao.getAllLogs();
    }


    // Method to populate the database with initial user and book data
    public void populateInitialData() {
        UserDao userDao = userDao();
        BookDao bookDao = bookDao();
        HoldDao holdDao = holdDao();

        //users
        User user1 = new User();
        user1.username = "hShuard";
        user1.password = "m@thl3t3";

        User user2 = new User();
        user2.username = "bMishra";
        user2.password = "bioN@no";

        User user3 = new User();
        user3.username = "shirleyBee";
        user3.password = "Carmel2Chicago";

        userDao.insert(user1);
        userDao.insert(user2);
        userDao.insert(user3);

        //books
        Book book1 = new Book();
        book1.title = "A Heartbreaking Work of Staggering Genius";
        book1.author = "Dave Eggers";
        book1.genre = "Memoir";

        Book book2 = new Book();
        book2.title = "The IDA Pro Book";
        book2.author = "Chris Eagle";
        book2.genre = "Computer Science";

        Book book3 = new Book();
        book3.title = "Frankenstein";
        book3.author = "Mary Shelley";
        book3.genre = "Fiction";

        bookDao.insert(book1);
        bookDao.insert(book2);
        bookDao.insert(book3);


    }

    // Method to clear all tables in the database
    public void clearAllTables() {
        clearUsers();
        clearBooks();
        clearHolds();
        clearTransactionLogs();
    }

    // Method to clear the User table
    private void clearUsers() {
        userDao().deleteAllUsers();
    }

    // Method to clear the Book table
    private void clearBooks() {
        bookDao().deleteAllBooks();
    }

    // Method to clear the Hold table
    private void clearHolds() {
        holdDao().deleteAllHolds();
    }

    // Method to clear the TransactionLog table
    private void clearTransactionLogs() {
        transactionLogDao().deleteAllLogs();
    }

}
