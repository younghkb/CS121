package database.entry;

import java.io.Serializable;
import java.util.Date;

public class Exchange implements Serializable {
	// exchange data
	public int exchange_id;
	public Type exchange_type;
	
	// id data
	public int loaner_id;
	public int borrower_id;
	public int book_id;
	
	public String book_title;

	public Date create_date;
	public Date start_date;
	public Date end_date;
	
	public Status status;

	public Exchange() {}
	
	public String toString() {
		return String.format("%d\n%s\n%d\n%d\n%d\n%s\n%s\n%s\n%s\n%s\n",
				exchange_id, exchange_type, loaner_id, borrower_id, book_id, book_title, create_date, start_date, end_date, status);
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
