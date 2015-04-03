

// SQL Builder
public abstract class SQLB { //TODO Sanitize

	// Does not guarantee that user is allowed to view the exchange.
	static String getExchange(Integer exchangeId) {
		return "select * from exchanges where exchange_id = " + exchangeId;
	}
	
	static String getPublicExchanges() {
		return "select * from exchanges where status = 'INITIAL' or status = 'RESPONSE';";
	}
	
	static String getPrivateExchanges(Integer userId) {
		return "select * from exchanges where loaner_id = " + userId + " or borrower_id = " + userId + ";";
	}
	
	static String createExchangeLoan(Integer loanerId, Integer bookId, String title) {
		String command = "insert into exchanges (loaner_id, book_id, book_title, create_date, status) values (%d, %d, '%s', datetime('now'), 'INITIAL');";
		return String.format(command, loanerId, bookId, title);
	}
	
	static String createExchangeBorrow(Integer borrowId, Integer bookId, String title) {
		String command = "insert into exchanges (borrower_id, book_id, book_title, create_date, status) values (%d, %d, '%s', datetime('now'), 'INITIAL');";
		return String.format(command, borrowId, bookId, title);
	}

	static String updateBookStatus(Status newStatus, Integer id) {
		return "update exchanges set status = '" + newStatus + "' where exchange_id = " + id + ";";
	}
	
	static String insertBook(Integer bookId, String title, String author, String isbn, Integer pubYear, Integer origPubYear,
								String imageUrl, String smallImageUrl) {
		return "insert into books values ("
				+ bookId + ",'" + title + "','" + author + "','" + isbn + "'," + pubYear + "," + 
				origPubYear + ",'" + imageUrl + "','" + smallImageUrl + "'," + " datetime('now'));";
	}
		
	static String getOldestBook() {
		return "select book_id from books where add_date = (select min(add_date) from books);";
	}
	
	static String findBookFromISBN(String isbn) {
		return "select book_id from books where isbn = " + isbn;
	}
	
	static String isBookActive(Integer bookId) {
		return "select * from exchanges where book_id = " + bookId + " and status != 'COMPLETED';";
	}	
	
	static String deleteBook(Integer bookId) {
		return "delete * from books where book_id = bookId;";
	}
}
