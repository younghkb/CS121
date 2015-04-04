package server;

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
//	static String createExchangeLoan() {
//		return "";
//	}
//	
//	static String createExchangeBorrow() {
//		return "";
//	}
	
	static String getOldestBook() {
		return "select min(add_date) from books;\n"
		+ "select book_id from books where add_date = minDate;";
	}
}
