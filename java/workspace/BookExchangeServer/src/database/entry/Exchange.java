package database.entry;

import java.io.Serializable;
import java.util.Date;

public class Exchange implements Serializable {
	// exchange data
	public int exchange_id;
	public Type exchange_type;
	
	// id data
	public int borrower_id;
	public int loaner_id;
	public int book_id;
	
	public String book_title;

	public Date create_date;
	public Date start_date;
	public Date end_date;
	
	public Status status;

	public Exchange() {}
	
	public String toString() {
		return String.format("exchange_id: %d, exchange_type: %s, borrower_id: %d, loaner_id: %d, book_id: %d, book_title: %s, create_date: %s, start_date: %s, end_date: %s, status: %s",
				exchange_id, exchange_type, borrower_id, loaner_id, book_id, book_title, create_date, start_date, end_date, status);
	}
	
	public static enum Status {
		INITIAL, 
		RESPONSE, 
		ACCEPTED, 
		COMPLETED;
		
		public static Status parse(String str) {
			switch (str) {
			case "INITIAL":
				return INITIAL;
			case "RESPONSE":
				return RESPONSE;
			case "ACCEPTED":
				return ACCEPTED;
			case "COMPLETED":
				return COMPLETED;
			}
			return null; // throw exception
		}
	}
	
	public static enum Type {
		BORROW,
		LOAN;
		
		public static Type parse(String str) {
			switch (str) {
			case "BORROW":
				return BORROW;
			case "LOAN":
				return LOAN;
			}
			return null; // throw exception
		}
	}
}
