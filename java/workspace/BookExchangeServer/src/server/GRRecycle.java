package server;

import java.util.Date;

import logging.Log;
// import xmlparse.GRE;
import database.SQLE;
import database.entry.Book;

/**
 * Thread for recycling Goodreads database entries
 */
public class GRRecycle extends Thread {
	
	//public static ConcurrentLinkedQueue<GRRecycleElement> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycleElement>();
	private static Book nextRecycle;
	private static Date recycleTime = new Date();
	//private final static long RECYCLE_TIME = 18 * 60 * 60 * 1000; // 18hrs
	private final static long RECYCLE_TIME = 60 * 1000; // 1 minute
	
	
	public void run() {
		//TODO refresh data
		try {
			Log.log("GRRecycle", "Thread Start", "");
			while (true) {
				updateRecycle();
				while (isTime()) {
					recycle(nextRecycle);
					nextRecycle = null; // need to do this so updateRecycle() will update
					updateRecycle(); // TODO sleep so we don't over query
				}
				Thread.sleep(1000); //TODO have signals wake this up
			}
		} catch(Exception e) {
			System.err.println();
		}
	}
	
	private void updateRecycle() throws Exception {
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
	
	private void recycle(Book book) throws Exception { // TODO check if book is still needed
		Log.log("GRRecycle", "Recycling", "book_id = " + book.book_id);
		SQLE.updateBook(GRFetch.query("" + book.isbn));
		//SQLE.updateBook(GRE.queryBook("" + book.isbn).get(0));
	}
} // end of GRRecycleThread
