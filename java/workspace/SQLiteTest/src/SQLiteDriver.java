import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// modified from tutorial found at
// http://www.tutorialspoint.com/sqlite/sqlite_java.htm

public class SQLiteDriver {
	public static void main(String args[]) throws Exception {
		Class.forName("org.sqlite.JDBC"); // check jar
		
		String comm = SQLB.createExchangeLoan(100, 200, "C");
		System.out.println(comm);
		
		update(comm);
		
		comm = SQLB.insertBook(21413662, "What If?", "Randall Munroe", "0544272994", 2014, 2014,
				"https://d.gr-assets.com/books/1394648139m/21413662.jpg", "https://d.gr-assets.com/books/1394648139m/21413662.jpg");
		System.out.println(comm);
		
		update(comm);
		
		//select();
		
		//delete();
		
		
	}
	
	public static void query(String sql) throws Exception {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:db.db");
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");

		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		System.out.println(rs);
		
		rs.close();
		stmt.close();
		c.close();

		System.out.println("Query Operation done successfully");
	}

	public static void update(String sql) throws Exception {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:db.db");
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");

		stmt = c.createStatement();
		stmt.executeUpdate(sql);
		c.commit();

		stmt.close();
		c.close();
		System.out.println("Update Operation done successfully");
	}

	
//	public static void exe(String command, String args) throws Exception {
//		Connection c = null;
//		Statement stmt = null;
//	
//		c = DriverManager.getConnection("jdbc:sqlite:test.db");
//		System.out.println("Opened database successfully");
//
//		stmt = c.createStatement();
//		String sql = String.format(command, args);
//		stmt.executeUpdate(sql);
//		stmt.close();
//		c.close();
//		System.out.println(sql);
//	}
}