package server.grfetch;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import logging.Log;
import xmlparse.GRE;
import database.SQLE;
import database.entry.Book;

/**
 * Thread for fetching queued Goodreads data
 */
public class GRFetch extends Thread {
	
	//static ConcurrentLinkedQueue<String> GRFetchQueue = new ConcurrentLinkedQueue<String>();
	
	static String GRKey;
	
	public static synchronized List<Book> query(String query) throws Exception {
		return GRE.queryBook(query);
	}
	
	public void run() {
		//TODO fetch new data and insert into database
		
		try {
			Log.log("GRFetch", "Thread Start", "");
			while (true) {
				//System.out.println("Size: " + DBWriteQueue.size());
				String sql = ""; //GRFetchQueue.poll();
				while (sql != null) { // better than checking length
					Log.log("GRFetch", "Deque", sql);
					SQLE.execute(sql);
					sql = ""; //GRFetchQueue.poll();
				}
				Thread.sleep(1000); //TODO have signals wake this up
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		
	}
}