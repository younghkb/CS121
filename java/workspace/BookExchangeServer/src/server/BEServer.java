package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
//import java.time.Clock; // only for JDK8 -- this class seems much nicer than Date
import java.util.Date;

class BEServer {
	final static int port = 6789;

	static ConcurrentLinkedQueue<DBWriteThread.DBWrite> DBWriteQueue = new ConcurrentLinkedQueue<DBWriteThread.DBWrite>();
	static ConcurrentLinkedQueue<GRFetchThread.GRFetch> GRFetchQueue = new ConcurrentLinkedQueue<GRFetchThread.GRFetch>();
	static ConcurrentLinkedQueue<GRRecycleThread.GRRecycle> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycleThread.GRRecycle>();

	static boolean keepRunning = true;

	// TODO make this handle Exceptions correctly
	static void main(String[] args) throws Exception {
		
		// start background threads
		new DBWriteThread().start();
		new GRFetchThread().start();
		new GRRecycleThread().start();

		
		ServerSocket connectSocket = new ServerSocket(port);
		
		while (keepRunning) {
			new UserThread(connectSocket.accept()).start();
		}
		
		connectSocket.close();
	}

	/**
	 * Thread for single writes to a the database
	 */
	static class DBWriteThread extends Thread {

		public void run() {
			//TODO write things as they get queued up
		}
		
		static abstract class DBWrite {
			abstract String getSQL();
		}

		static class WriteMe extends DBWrite {
			String getSQL() {
				return "";
			}
		}
	}

	/**
	 * Thread for fetching queued Goodreads data
	 */
	static class GRFetchThread extends Thread {
		
		public void run() {
			//TODO fetch new data and insert into database
		}
		
		static abstract class GRFetch {
			abstract String getQuery();
		}
		
		static class FetchMe extends GRFetch {
			String getQuery() {
				return "";
			}
		}
	}

	/**
	 * Thread for recycling Goodreads database entries
	 */
	static class GRRecycleThread extends Thread {
		
		public void run() {
			//TODO refresh data
		}
		
		static abstract class GRRecycle {
			//Date d = new Date();
			Date refreshAt;
			String book;
		}
		
		static class RecycleMe extends GRRecycle {
			
		}
	}
	
	
	/**
	 * Thread for accepted user connections
	 */
	static class UserThread extends Thread {
		Socket user;
		
		public void run() {
			//TODO parse commands and issue orders
		}
		
		UserThread(Socket user) {
			this.user = user;
		}
	}
}
