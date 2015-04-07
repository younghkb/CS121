package server.grfetch;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread for fetching queued Goodreads data
 */
public class GRFetchThread extends Thread {
	
	static ConcurrentLinkedQueue<GRFetch> GRFetchQueue = new ConcurrentLinkedQueue<GRFetch>();
	
	static String GRKey;
	
	public void run() {
		//TODO fetch new data and insert into database
		
		try {
			
		} catch (Exception e) {
		}
		
	}
}