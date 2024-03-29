package com.example.otterlibrarysystem;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This class represents the TransactionLog entity in the Room Database, storing information about various transactions.
@Entity(tableName = "transaction_logs")
public class TransactionLog {
    @PrimaryKey(autoGenerate = true)
    private int logId;

    @ColumnInfo(name = "transaction_type")
    private String transactionType;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "details")
    private String details;

    // Constructors, getters, and setters

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
