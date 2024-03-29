package com.example.otterlibrarysystem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// This interface defines Data Access Object (DAO) methods for the Hold entity.
// It includes methods for inserting a new hold, getting all holds, and getting a hold by bookId, username, and genre.
@Dao
public interface HoldDao {
    @Insert
    void insert(Hold hold);

    @Query("SELECT * FROM holds")
    List<Hold> getAllHolds();

    @Query("SELECT * FROM holds WHERE bookId = :bookId AND username = :username AND genre = :genre")
    Hold getHoldByUsernameAndBookId(int bookId, String username, String genre);

    @Query("DELETE FROM holds")
    void deleteAllHolds();
}
