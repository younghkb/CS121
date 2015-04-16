package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import logging.Log;
import client.Book;
import client.Exchange;

// SQL Executer
public abstract class SQLE {
	// TODO change queryBooks to query(SQL, List<E>), check if there is way to get current method header?
	
	final static String DATABASE_NAME = "bookexchange.db";
	
	// ===================
	// create/delete books
	// ===================
	
	public static void createBook(int book_id, String book_title, String author, String isbn, String pub_year, String orig_pub_year, String image_url, String small_image_url) throws SQLException {
		Log.log("SQLE", "createBook", "book_id = " + book_id);
		update(SQLB.createBook(book_id, book_title, author, isbn, pub_year, orig_pub_year, image_url, small_image_url));
	}
	
	public static void createBook(Book book) throws SQLException {
		Log.log("SQLE", "createBook", "book_id = " + book.book_id);
		update(SQLB.createBook(book));
	}
	
	public static void updateBook(int book_id, String book_title, String author, String isbn, String pub_year, String orig_pub_year, String image_url, String small_image_url) throws SQLException {
		Log.log("SQLE", "updateBook", "book_id = " + book_id);
		update(SQLB.updateBook(book_id, book_title, author, isbn, pub_year, orig_pub_year, image_url, small_image_url));
	}
	
	public static void updateBook(Book book) throws SQLException { // TODO make a parameter version of this function?
		Log.log("SQLE", "updateBook", "book_id = " + book.book_id);
		update(SQLB.updateBook(book));
	}
	
	public static void deleteBook(int book_id) throws SQLException {
		update(SQLB.deleteBook(book_id));
	}
	
	// =========
	// get books
	// =========
	
	public static Book getBook(int book_id) throws SQLException, ParseException {
		Log.log("SQLE", "getBook", "book_id = " + book_id);
		List<Book> books = queryBooks(SQLB.getBook(book_id));
		if (books.size() > 0) {
			return books.get(0); // should never be more than 1
		} else {
			return null;
		}
	}
	
	public static Book getBookFromISBN(String isbn) throws SQLException, ParseException {
		Log.log("SQLE", "getBookFromISBN", "isbn = " + isbn);
		List<Book> books = queryBooks(SQLB.getBookFromISBN(isbn));
		if (books.size() > 0) {
			return books.get(0); // should never be more than 1
		} else {
			return null;
		}
	}
	
	public static Book getOldestBook() throws SQLException, ParseException {
		Log.log("SQLE", "getOldestBook", "");
		List<Book> books = queryBooks(SQLB.getOldestBook());
		if (books.size() > 0) {
			return books.get(0); // should never be more than 1
		} else {
			return null;
		}
	}
	
	// Can be used to determine if a book is active or find exchanges for a specific book (i.e., user search)
	public static List<Book> getExchangeForBook(int book_id) throws SQLException, ParseException {
		Log.log("SQLE", "getExchangeForBook", "book_id = " + book_id);
		return queryBooks(SQLB.getBook(book_id));
	}
	
	// ====================================================================================================
	// ====================================================================================================
	
	// ================
	// create exchanges
	// ================
	
	public static void createExchange(int initUser_id, Exchange.Type exchange_type, int book_id, String book_title) throws SQLException {
		Log.log("SQLE", "createExchange", "book_id = " + book_id);
		update(SQLB.createExchange(initUser_id, exchange_type, book_id, book_title));
	}
	
	// =============
	// get exchanges
	// =============
	
	public static Exchange getExchange(int exchange_id) throws SQLException, ParseException { // Does not guarantee that user is allowed to view the exchange.
		Log.log("SQLE", "getExchange", "exchange_id = " + exchange_id);
		List<Exchange> exchanges = queryExchanges(SQLB.getExchange(exchange_id));
		if (exchanges.size() > 0) {
			return exchanges.get(0); // should never be more than 1
		} else {
			return null;
		}
	}
	
	public static List<Exchange> getPublicExchanges() throws SQLException, ParseException {
		Log.log("SQLE", "getPublicExchanges", "");
		return queryExchanges(SQLB.getPublicExchanges());
	}
	
	public static List<Exchange> getPrivateExchanges(int user_id) throws SQLException, ParseException {
		Log.log("SQLE", "getPrivateExchanges", "user_id = " + user_id);
		return queryExchanges(SQLB.getPrivateExchanges(user_id));
	}	
	
	// ================
	// modify exchanges
	// ================
	
	public static void updateBorrower(int exchange_id, int borrower_id) throws SQLException {
		Log.log("SQLE", "updateBorrower", "exchange_id = " + exchange_id + ", borrower_id = " + borrower_id);
		update(SQLB.updateBorrower(exchange_id, borrower_id));
	}
	
	public static void updateLoaner(int exchange_id, int loaner_id) throws SQLException {
		Log.log("SQLE", "updateLoaner", "exchange_id = " + exchange_id + ", loaner_id = " + loaner_id);
		update(SQLB.updateLoaner(exchange_id, loaner_id));
	}
	
	public static void updateExchangeStatus(int exchange_id, Exchange.Status status) throws SQLException {
		Log.log("SQLE", "updateExchangeStatus", "exchange_id = " + exchange_id + ", status = " + status);
		update(SQLB.updateExchangeStatus(exchange_id, status));
	}
		
	// ====================================================================================================
	// ====================================================================================================
	
	// ============
	// empty tables
	// ============
	
	public static void deleteAllBooks() throws SQLException {
		Log.log("SQLE", "deleteAllBooks", "");
		update(SQLB.deleteAllBooks());
	}
	
	public static void deleteAllExchanges() throws SQLException {
		Log.log("SQLE", "deleteAllExchanges", "");
		update(SQLB.deleteAllExchanges());
	}
	
	public static void deleteAllUsers() throws SQLException {
		Log.log("SQLE", "deleteAllUsers", "");
		update(SQLB.deleteAllUsers());
	}
	
	// ====================================================================================================
	// ====================================================================================================
	
	private static List<Book> queryBooks(String sql) throws SQLException, ParseException {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		c.setAutoCommit(false);
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		List<Book> lst = mkBookList(rs);
		
		rs.close();
		stmt.close();
		c.close();
		
//		for (Book b : lst) {
//			System.out.println(b);
//		}
		
		return lst;
	}
	
	private static List<Exchange> queryExchanges(String sql) throws SQLException, ParseException {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		c.setAutoCommit(false);
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		List<Exchange> lst = mkExchangeList(rs);
		
		rs.close();
		stmt.close();
		c.close();
		
//		for (Exchange e : lst) {
//			System.out.println(e);
//		}
		
		return lst;
	}
	
	private static Book mkBook(ResultSet rs) throws SQLException, ParseException {
		Book book = new Book();
		
		book.book_id =  rs.getInt("book_id");
				
		book.book_title = rs.getString("book_title");
		book.author =  rs.getString("author");
		book.isbn = rs.getString("isbn");
		
		book.pub_year = rs.getString("pub_year");
		book.orig_pub_year = rs.getString("orig_pub_year");
		
		book.image_url = rs.getString("image_url");
		book.small_image_url = rs.getString("small_image_url");
		
		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		book.add_date = df.parse(rs.getString("add_date"));
		
		return book;
	}
	
	private static List<Book> mkBookList(ResultSet rs) throws SQLException, ParseException {
		List<Book> books = new ArrayList<Book>();
		
	    while (rs.next()) {
	    	books.add(mkBook(rs));
	    }
	    
		return books;
	}
	
	private static Exchange mkExchange(ResultSet rs) throws SQLException, ParseException {
		Exchange exchange = new Exchange();
		
		exchange.exchange_id = rs.getInt("exchange_id");
		exchange.exchange_type = Exchange.Type.parse(rs.getString("exchange_type"));
		
		exchange.loaner_id = rs.getInt("loaner_id");
		exchange.borrower_id = rs.getInt("borrower_id");
		exchange.book_id = rs.getInt("book_id");
		
		exchange.book_title = rs.getString("book_title");

		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		exchange.create_date = df.parse(rs.getString("create_date"));
		
		// non-guaranteed dates 
		String str_sd = rs.getString("start_date");
		exchange.start_date = (str_sd != null ? df.parse(str_sd) : null);
		
		String str_ed = rs.getString("end_date");
		exchange.end_date = (str_ed != null ? df.parse(str_ed) : null);
		
		exchange.status = Exchange.Status.parse(rs.getString("status"));
		
		return exchange;
	}
	
	private static List<Exchange> mkExchangeList(ResultSet rs) throws SQLException, ParseException {
		List<Exchange> exchanges = new ArrayList<Exchange>();
		
	    while (rs.next()) {
	    	exchanges.add(mkExchange(rs));
	    }
	    
		return exchanges;
	}

	private static void update(String sql) throws SQLException {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		c.setAutoCommit(false);

		stmt = c.createStatement();
		stmt.executeUpdate(sql);
		c.commit();

		stmt.close();
		c.close();
	}
	
	public static void execute(String sql) throws SQLException { // TODO make different from update? besides visibility
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		c.setAutoCommit(false);

		stmt = c.createStatement();
		stmt.executeUpdate(sql);
		c.commit();

		stmt.close();
		c.close();
	}
	
	// TODO use PreparedStatements
	
	// debug functions
	private static void printResultSet(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
	    int columnsNumber = rsmd.getColumnCount();
	    while (rs.next()) {
	    	for (int i = 1; i <= columnsNumber; i++) {
	    		if (i > 1) System.out.print(",  ");
	    		String columnValue = rs.getString(i);
	    		System.out.print(columnValue + " " + rsmd.getColumnName(i));
	    	}
	    	System.out.println("");
	    }
	}
}
