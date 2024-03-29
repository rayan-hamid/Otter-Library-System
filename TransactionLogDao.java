package com.example.otterlibrarysystem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// This interface defines Data Access Object methods for the TransactionLog entity.
// It includes methods for inserting a new transaction log and retrieving a list of all transaction logs.

@Dao
public interface TransactionLogDao {
    @Insert
    void insert(TransactionLog transactionLog);

    @Query("SELECT * FROM transaction_logs")
    List<TransactionLog> getAllLogs();

    @Query("DELETE FROM transaction_logs")
    void deleteAllLogs();
}
