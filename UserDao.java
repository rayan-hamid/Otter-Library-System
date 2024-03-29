package com.example.otterlibrarysystem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// This interface defines Data Access Object methods for the User entity.
// It includes methods for inserting a new user, logging in, getting a user by username, and getting the count of users by username.

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int getCountByUsername(String username);

    @Query("DELETE FROM users")
    void deleteAllUsers();



}

