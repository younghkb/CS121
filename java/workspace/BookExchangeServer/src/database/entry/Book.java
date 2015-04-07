package database.entry;

import java.util.Date;


public class Book {
	public String isbn;
	
	public String book_title;
	public String author;
	public String book_id;		// Goodreads ID, primary key in DB
	
	public String orig_pub_year;
	public String pub_year;
	
	public String image_url;
	public String small_image_url;
	
	public Date add_date;
	
	public Book() {}
	
	public String toString() {
		String str = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
				isbn, book_title, author, book_id, orig_pub_year, pub_year, image_url, small_image_url, add_date);
		return str; 
	}
}