package database;

import java.sql.SQLException;
import java.util.List;

import logging.Log;
import client.Book;
import client.Exchange;
import client.User;

// SQL Builder
public abstract class SQLB { //TODO Sanitize
	
//	public static void main(String[] args) {
//		System.out.println(createBook(10, "Test Book", "Mr. Test", "123", "1990", "1985", "www.google.com", "www.google.com"));
//		System.out.println(createExchange(1, Exchange.Type.BORROW, 10, "Test Book"));
//		System.out.println(getPublicExchanges());
//	}

	// ====================
	// login/create account
	// ====================
	
	public static String login(String username, String password) {
		String command = "select * from users where username = '%s' and password = '%s';";
		return String.format(command, username, password);
	}
	
	public static String createLogin(String username, String password) {
		String command = "insert into users (username, password) values ('%s', '%s');";
		return String.format(command, username, password);
	}
	
	public static String getUser(int user_id) {
		String command = "select * from users where user_id = %d;";
		return String.format(command, user_id);
	}
	
	public static String getUserFromUsername(String username) {
		String command = "select * from users where username = '%s';";
		return String.format(command, username);
	}
	
	// ===================
	// create/delete books
	// ===================
	
	public static String createBook(int book_id, String book_title, String author, String isbn, String pub_year, String orig_pub_year, String image_url, String small_image_url) {
		String command = "insert into books values (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', datetime('now'));";
		return String.format(command, book_id, book_title, author, isbn, pub_year, orig_pub_year, image_url, small_image_url);
	}
	
	public static String createBook(Book book) {
		String command = "insert into books values (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', datetime('now'));";
		return String.format(command, book.book_id, book.book_title, book.author, book.isbn, book.pub_year, book.orig_pub_year, book.image_url, book.small_image_url);
	}
	
	public static String updateBook(int book_id, String book_title, String author, String isbn, String pub_year, String orig_pub_year, String image_url, String small_image_url) {
		String command = "update books set book_id = %d, book_title = '%s', author = '%s', isbn = '%s', pub_year = '%s', orig_pub_year = '%s', image_url = '%s', small_image_url = '%s', add_date = datetime('now') where book_id = %d;";
		return String.format(command, book_id, book_title, author, isbn, pub_year, orig_pub_year, image_url, small_image_url, book_id);
	}
	
	public static String updateBook(Book book) {
		String command = "update books set book_id = %d, book_title = '%s', author = '%s', isbn = '%s', pub_year = '%s', orig_pub_year = '%s', image_url = '%s', small_image_url = '%s', add_date = datetime('now') where book_id = %d;";
		return String.format(command, book.book_id, book.book_title, book.author, book.isbn, book.pub_year, book.orig_pub_year, book.image_url, book.small_image_url, book.book_id);
	}
	
	public static String deleteBook(int book_id) {
		String command = "delete from books where book_id = %d;";
		return String.format(command, book_id);
	}
	
	// =========
	// get books
	// =========
	
	public static String getBook(int book_id) {
		String command = "select * from books where book_id = %d;";
		return String.format(command, book_id);
	}
	
	public static String getBookFromISBN(String isbn) {
		String command = "select * from books where isbn = %s;";
		return String.format(command, isbn); // changed to *
	}
	
	public static String getOldestBook() {
		return "select * from books where add_date = (select min(add_date) from books);"; // changed to *
	}
	
	// Can be used to determine if a book is active or find exchanges for a specific book (i.e., user search)
	public static String getExchangeForBook(int book_id) {
		String command = "select * from exchanges where book_id = %i and status != 'COMPLETED';";
		return String.format(command, book_id);
	}
	
	// ============
	// empty tables
	// ============
	
	public static String deleteAllBooks() {
		return "delete from books;";
	}
	
	public static String deleteAllExchanges() {
		return "delete from exchanges;";
	}
	
	public static String deleteAllUsers() {
		return "delete from users;";
	}
	
	// ====================================================================================================
	// ====================================================================================================
	
	// ================
	// create exchanges
	// ================
	
	public static String createExchange(int initUser_id, Exchange.Type exchange_type, int book_id, String book_title) {
		switch (exchange_type) {
		case BORROW:
			return createExchangeBorrow(initUser_id, Exchange.Type.BORROW, book_id, book_title);
		case LOAN:
			return createExchangeLoan(initUser_id, Exchange.Type.LOAN, book_id, book_title);
		}
		return ""; //TODO throw exception
	}
	
	public static String createExchange(Exchange exchange) {
		return createExchange((exchange.exchange_type == Exchange.Type.BORROW ? exchange.borrower_id : exchange.loaner_id), exchange.exchange_type, exchange.book_id, exchange.book_title);
	}
	
	private static String createExchangeBorrow(int borrow_id, Exchange.Type exchange_type, int book_id, String book_title) { //TODO exchange type
		String command = "insert into exchanges (borrower_id, exchange_type, book_id, book_title, create_date, status) values (%d, '%s', %d, '%s', datetime('now'), 'INITIAL');";
		return String.format(command, borrow_id, exchange_type, book_id, book_title);
	}
	
	private static String createExchangeLoan(int loaner_id, Exchange.Type exchange_type, int book_id, String book_title) { //TODO exchange type
		String command = "insert into exchanges (loaner_id, exchange_type, book_id, book_title, create_date, status) values (%d, '%s', %d, '%s', datetime('now'), 'INITIAL');";
		return String.format(command, loaner_id, exchange_type, book_id, book_title);
	}
	
	// =============
	// get exchanges
	// =============
	
	public static String getExchange(int exchange_id) { // Does not guarantee that user is allowed to view the exchange.
		String command = "select * from exchanges where exchange_id = %s;";
		return String.format(command, exchange_id);
	}
	
	public static String getPublicExchanges() {
		// "select * from exchanges where status = 'INITIAL' or status = 'RESPONSE';";
		return "select * from exchanges where status = 'INITIAL';";
	}
	
	public static String getPrivateExchanges(int user_id) {
		String command = "select * from exchanges where loaner_id = %s or borrower_id = %s;";
		return String.format(command, user_id, user_id);
	}	
	
	// ================
	// modify exchanges
	// ================
	
	public static String updateBorrower(int exchange_id, int borrower_id) {
		String command = "update exchanges set borrower_id = %d where exchange_id = %d;";
		return String.format(command, borrower_id, exchange_id);
	}
	
	public static String updateLoaner(int exchange_id, int loaner_id) {
		String command = "update exchanges set loaner_id = %d where exchange_id = %d;";
		return String.format(command, loaner_id, exchange_id);
	}
	
	public static String updateExchangeStatus(int exchange_id, Exchange.Status status) {
		String command = "update exchanges set status = '%s' where exchange_id = %d;";
		return String.format(command, status, exchange_id);
	}
}
