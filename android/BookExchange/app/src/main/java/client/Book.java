package client;

import java.io.Serializable;

import java.io.Serializable;
import java.util.Date;


public class Book implements Serializable {
    public int book_id;		// Goodreads ID, primary key in DB

    public String book_title;
    public String author;
    public String isbn;

    public String pub_year;
    public String orig_pub_year;

    public String image_url;
    public String small_image_url;

    public Date add_date;

    public Book() {}

    public String toString() {
        String str = String.format("book_id: %d, book_title: %s, author: %s, isbn: %s, pub_year: %s, orig_pub_year: %s, image_url: %s, small_image_url: %s, add_date: %s",
                book_id, book_title, author, isbn, pub_year, orig_pub_year, image_url, small_image_url, add_date);
        return str;
    }
}