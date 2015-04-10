package com.example.jarthur.bookexchange;

import java.util.Date;

/* Object representing a book in the database. */
public class Book {

    public String isbn;

    public String book_title;
    public String author;
    public String bookId;		// Goodreads ID, primary key in DB

    public String origPubYear;
    public String pubYear;

    public String imageUrl;
    public String smallImageUrl;

    public ExchangeStatus status;   // NEW!
    public Date addDate;

    public Book() {}

    public String toString() {
        String str = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
                isbn, book_title, author, bookId, pubYear, origPubYear, imageUrl, smallImageUrl, status, addDate);
        return str;
    }
}