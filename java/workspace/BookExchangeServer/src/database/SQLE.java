package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import database.entry.Book;
import database.entry.Exchange;
import database.entry.Status;

// SQL Executer
public class SQLE {
	
	final static String DATABASE_NAME = "db.db";
	
	public static List<Book> queryBooks(String sql) throws Exception {
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
		
		for (Book b : lst) {
			System.out.println(b);
		}
		
		return lst;
	}
	
	public static List<Exchange> queryExchanges(String sql) throws Exception {
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
		
		for (Exchange e : lst) {
			System.out.println(e);
		}
		
		return lst;
	}
	
	private static Book mkBook(ResultSet rs) throws Exception {
		Book book = new Book();
		
		book.isbn = rs.getString("isbn");
		
		book.book_title = rs.getString("book_title");
		book.author =  rs.getString("author");
		book.book_id =  rs.getString("book_id");
		
		book.orig_pub_year = rs.getString("orig_pub_year");
		book.pub_year = rs.getString("pub_year");
		
		book.image_url = rs.getString("image_url");
		book.small_image_url = rs.getString("small_image_url");
		
		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		book.add_date = df.parse(rs.getString("add_date"));
		
		return book;
	}
	
	private static List<Book> mkBookList(ResultSet rs) throws Exception {
		List<Book> books = new ArrayList<Book>();
		
	    while (rs.next()) {
	    	books.add(mkBook(rs));
	    }
	    
		return books;
	}
	
	private static Exchange mkExchange(ResultSet rs) throws Exception {
		Exchange exchange = new Exchange();
		
		exchange.exchange_id = rs.getInt("exchange_id");
		exchange.loaner_id = rs.getInt("loaner_id");
		exchange.borrower_id = rs.getInt("borrower_id");
		exchange.book_id = rs.getInt("book_id");
		
		exchange.book_title = rs.getString("book_title");

		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		exchange.create_date = df.parse(rs.getString("create_date"));
		
		// non-guaranteed dates 
		String str_sd = rs.getString("start_date");
		exchange.start_date = (str_sd != null ? df.parse(str_sd) : null);
		
		String str_ed = rs.getString("end_date");
		exchange.end_date = (str_ed != null ? df.parse(str_ed) : null);
		
		
		String strstatus = rs.getString("status"); //TODO change this?
		switch(strstatus) {
		case "INITIAL":
			exchange.status = Status.INITIAL;
			break;
		case "RESPONSE":
			exchange.status = Status.RESPONSE;
			break;
		case "ACCEPTED":
			exchange.status = Status.ACCEPTED;
			break;
		case "COMPLETED":
			exchange.status = Status.COMPLETED;
			break;
		}
		
		return exchange;
	}
	
	private static List<Exchange> mkExchangeList(ResultSet rs) throws Exception {
		List<Exchange> exchanges = new ArrayList<Exchange>();
		
	    while (rs.next()) {
	    	exchanges.add(mkExchange(rs));
	    }
	    
		return exchanges;
	}

	public static void update(String sql) throws Exception {
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
	
	// debug functions
	public static void printResultSet(ResultSet rs) throws Exception {
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
