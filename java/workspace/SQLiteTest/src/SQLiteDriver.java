import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

// modified from tutorial found at
// http://www.tutorialspoint.com/sqlite/sqlite_java.htm

public class SQLiteDriver {
	public static void main(String args[]) throws Exception {
		Class.forName("org.sqlite.JDBC"); // check jar
		
		//select();
		
		//delete()'	
		
	}
	
	public static void query(String sql) throws Exception {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:db.db");
		c.setAutoCommit(false);
		//System.out.println("Opened database successfully");
		
		System.out.println(sql);

		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		printResultSet(rs);
		System.out.println();
	        
		rs.close();
		stmt.close();
		c.close();
		
		//System.out.println("Query Operation done successfully");
		
		//return rs;
	}
	
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

	public static void update(String sql) throws Exception {
		Connection c = null;
		Statement stmt = null;
		
		c = DriverManager.getConnection("jdbc:sqlite:db.db");
		c.setAutoCommit(false);
		//System.out.println("Opened database successfully");
		
		System.out.println(sql);
		System.out.println();

		stmt = c.createStatement();
		stmt.executeUpdate(sql);
		c.commit();

		stmt.close();
		c.close();
		//System.out.println("Update Operation done successfully");
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