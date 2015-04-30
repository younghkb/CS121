package server.threads;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import logging.Log;
import server.grfetch.GRFetch;
import client.Book;
// import xmlparse.GRE;
import database.SQLE;

/**
 * Thread for recycling Goodreads database entries
 */
public class GRRecycle extends Thread {
	
	//public static ConcurrentLinkedQueue<GRRecycleElement> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycleElement>();
	private static Book nextRecycle;
	private static Date recycleTime = new Date();
	private final static long RECYCLE_TIME = 18 * 60 * 60 * 1000; // 18hrs
	//private final static long RECYCLE_TIME = 60 * 1000; // 1 minute
	
	static boolean keepRunning = true;
	
	
	public void run() {
		//TODO refresh data
		
			Log.log("GRRecycle", "Thread Start", "");
			while (keepRunning) {
				try {
					updateRecycle();
					while (isTime()) {
						recycle(nextRecycle);
						nextRecycle = null; // need to do this so updateRecycle() will update
						updateRecycle(); // TODO sleep so we don't over query
					}
				} catch (SQLException|ParseException e) {
					System.err.println(e);
				}
				try {
					Thread.sleep(60000); //TODO have signals wake this up
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		
	}
	
	private void updateRecycle() throws SQLException, ParseException {
		if (nextRecycle == null) {
			nextRecycle = SQLE.getOldestBook();
			if (nextRecycle != null) {
				recycleTime.setTime(nextRecycle.add_date.getTime() + RECYCLE_TIME);
				Log.log("GRRecycle", "Next Recycle", "book_id = " + nextRecycle.book_id + " @ " + recycleTime);
			}
		}
	}
	
	private boolean isTime() {
		if (nextRecycle == null || recycleTime.after(new Date())) {
			return false;
		}
		return true;
	}
	
	private void recycle(Book book) throws SQLException { // TODO check if book is still needed
		Log.log("GRRecycle", "Recycling", "book_id = " + book.book_id); // TODO check if book exists
		SQLE.updateBook(GRFetch.query("" + book.isbn));
		//SQLE.updateBook(GRE.queryBook("" + book.isbn).get(0));
	}
} // end of GRRecycleThread
