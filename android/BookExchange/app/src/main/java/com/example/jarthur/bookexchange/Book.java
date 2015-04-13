package com.example.jarthur.bookexchange;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

// IMPORTANT! We don't want to change the fields in this class because it might mess up
// Serializable. Be careful!

/* Object representing a book in the database. */
public class Book implements Serializable{

    public String isbn;

    public String bookTitle;
    public String author;
    public String bookId;		// Goodreads ID, primary key in DB

    public String origPubYear;
    public String pubYear;

    public String imageUrl;
    public String smallImageUrl;

    public ExchangeStatus status;
    public Date addDate;

    public Book() {}

    public String toString() {
        String str = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
                isbn, bookTitle, author, bookId, pubYear, origPubYear, imageUrl, smallImageUrl, status, addDate);
        return str;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(this.toString());
    }


    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // TODO populate the fields of 'this' from the data in 'in'...
        // Use regexes to extract data
    }
}