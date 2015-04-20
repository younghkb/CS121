package server.threads;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import logging.Log;
import database.SQLE;

/**
 * Thread for single writes to a the database
 */
public class DBWrite extends Thread {
	
	// static ConcurrentLinkedQueue<DBWriteElement> DBWriteQueue = new ConcurrentLinkedQueue<DBWriteElement>();
	static ConcurrentLinkedQueue<String> DBWriteQueue = new ConcurrentLinkedQueue<String>(); //TODO switch to PreparedStatement?
	static boolean keepRunning = true;
	
	public void run() {
		try {
			Log.log("DBWrite", "Thread Start", "");
			while (keepRunning) {
				//System.out.println("Size: " + DBWriteQueue.size());
				String sql = DBWriteQueue.poll();
				while (sql != null) { // better than checking length
					Log.log("DBWrite", "Deque", sql);
					SQLE.execute(sql);
					sql = DBWriteQueue.poll();
				}
				try {
				Thread.sleep(1000); //TODO have signals wake this up
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	public static void queue(String sql) { //TODO could write ordering causing problems?
		Log.log("DBWrite", "Queue", sql);
		DBWriteQueue.add(sql);
	}
}