package com.example.otterlibrarysystem;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// This interface defines Data Access Object methods for the Book entity.
// It includes methods for inserting a new book, saving a new book, getting books by genre, and getting a list of genres.
@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Insert
    void saveNewBook(Book book);

    @Query("SELECT * FROM books WHERE genre = :genre")
    List<Book> getBooksByGenre(String genre);

    @Query("SELECT genre FROM books")
    List<String> getGenres();

    @Query("DELETE FROM books")
    void deleteAllBooks();
}


