package server.dbwrite;

import java.util.concurrent.ConcurrentLinkedQueue;

import logging.Log;
import database.SQLE;

/**
 * Thread for single writes to a the database
 */
public class DBWrite extends Thread {
	
	// static ConcurrentLinkedQueue<DBWriteElement> DBWriteQueue = new ConcurrentLinkedQueue<DBWriteElement>();
	static ConcurrentLinkedQueue<String> DBWriteQueue = new ConcurrentLinkedQueue<String>(); //TODO switch to PreparedStatement?
	
	public void run() {
		try {
			Log.log("DBWrite", "Thread Start", "");
			while (true) {
				//System.out.println("Size: " + DBWriteQueue.size());
				String sql = DBWriteQueue.poll();
				while (sql != null) { // better than checking length
					Log.log("DBWrite", "Deque", sql);
					SQLE.execute(sql);
					sql = DBWriteQueue.poll();
				}
				Thread.sleep(1000); //TODO have signals wake this up
			}
		} catch (Exception e) { // TODO fix
			System.err.println(e);
		}
	}
	
//	public void run() {
//		try {
//			Connection c;
//			Statement stmt;
//			
//			Class.forName("org.sqlite.JDBC");
//		
//			c = DriverManager.getConnection("jdbc:sqlite:test.db");
//			System.out.println("Opened database successfully");
//		
//			// TODO write things as they get queued up
//			while (true) {
//				System.out.println("Size: " + DBWriteQueue.size());
//				while (DBWriteQueue.size() > 0) {
//					stmt = c.createStatement();
//					String sql = DBWriteQueue.poll();
//					System.out.println("DEQUEUED: " + sql);
//					stmt.executeUpdate(sql);
//					stmt.close();
//					c.commit();
//				}
//				Thread.sleep(1000); //TODO have signals wake this up
//				
////				synchronized (DBWriteQueue) {
////					DBWriteQueue.wait();
////				}
//			}
//			//c.close(); //TODO make this reachable
//		} catch (Exception e) { // TODO fix
//			System.err.println(e);
//		}
//	}
	
	public static void queue(String sql) { //TODO could write ordering causing problems?
		Log.log("DBWrite", "Queue", sql);
		DBWriteQueue.add(sql);
	}
}