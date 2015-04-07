package server.dbwrite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread for single writes to a the database
 */
public class DBWriteThread extends Thread {

	static ConcurrentLinkedQueue<DBWrite> DBWriteQueue = new ConcurrentLinkedQueue<DBWrite>();
	
	public void run() {
		try {
			Connection c;
			Statement stmt;
			
			Class.forName("org.sqlite.JDBC");
		
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("Opened database successfully");
		
			// TODO write things as they get queued up
			while (true) {
				if (DBWriteQueue.size() > 0) {
					stmt = c.createStatement();
					String sql = DBWriteQueue.poll().getSQL();
					stmt.executeUpdate(sql);
					stmt.close();
					c.close();
				}
				
				synchronized (DBWriteQueue) {
					DBWriteQueue.wait();
				}
			}
		} catch (Exception e) { // TODO fix
		}
	}
}