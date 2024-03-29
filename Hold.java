package com.example.otterlibrarysystem;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This class represents the Hold entity in the Room Database, storing information about each book hold.
@Entity(tableName = "holds")
public class Hold {
    @PrimaryKey(autoGenerate = true)
    public int holdId;

    //column information
    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "bookId")
    public int bookId;

    @ColumnInfo(name = "genre")
    public String genre;

}
