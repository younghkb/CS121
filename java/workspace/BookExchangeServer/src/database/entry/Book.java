package database.entry;

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
		String str = String.format("book_id: %d\nbook_title: %s\nauthor: %s\nisbn: %s\npub_year: %s\norig_pub_year: %s\nimage_url: %s\nsmall_image_url: %s\nadd_date: %s\n",
				book_id, book_title, author, isbn, pub_year, orig_pub_year, image_url, small_image_url, add_date);
		return str; 
	}
}