package server.grrecycle;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread for recycling Goodreads database entries
 */
public class GRRecycleThread extends Thread {
	
	public static ConcurrentLinkedQueue<GRRecycle> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycle>();
	
	
	public void run() {
		//TODO refresh data
		try {
			
		} catch(Exception e) {}
	}
} // end of GRRecycleThread
