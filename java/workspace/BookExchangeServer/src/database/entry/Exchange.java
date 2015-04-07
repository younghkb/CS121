package database.entry;

import java.util.Date;

public class Exchange {
	public int exchange_id;
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
		return String.format("%d\n%d\n%d\n%d\n%s\n%s\n%s\n%s\n%s\n",
				exchange_id, loaner_id, borrower_id, book_id, book_title, create_date, start_date, end_date, status);
	}
}
