

// SQL Builder
public abstract class SQLB { //TODO Sanitize
	static String getSpecificExchange(String exchangeID) {
		return "";
	}
	
//	static String getPublicExchange(String a) {
//		return "";
//	}
//	
//	static String getPrivateExchange() {
//		return "";
//	}
//	
	static String createExchangeLoan(Integer loaner, Integer book, String title) {
		String command = "insert into exchanges (loaner_id, book_id, book_title, create_date, status) values (%d, %d, '%s', datetime('now'), 0);";
		return String.format(command, loaner, book, title);
	}
	
//	static String createExchangeBorrow() {
//		return "";
//	}
	
	static String getOldestBook() {
		return "select min(add_date) from books;\n"
		+ "select book_id from books where add_date = minDate;";
	}
}
