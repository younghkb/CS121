package server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
//import java.time.Clock; // only for JDK8 -- this class seems much nicer than Date
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

class BEServer {
	
	final static int PORT = 6789;
	
	static PrivateData pd;
	
	static ConcurrentLinkedQueue<DBWriteThread.DBWrite> DBWriteQueue = new ConcurrentLinkedQueue<DBWriteThread.DBWrite>();
	static ConcurrentLinkedQueue<GRFetchThread.GRFetch> GRFetchQueue = new ConcurrentLinkedQueue<GRFetchThread.GRFetch>();
	static ConcurrentLinkedQueue<GRRecycleThread.GRRecycle> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycleThread.GRRecycle>();

	static boolean keepRunning = true;

	// TODO make this handle Exceptions correctly
	static void main(String[] args) throws Exception {
		pd = new PrivateData("data.private");
		
		// start background threads
		new DBWriteThread().start();
		new GRFetchThread().start();
		new GRRecycleThread().start();

		
		ServerSocket connectSocket = new ServerSocket(PORT);
		
		while (keepRunning) {
			new UserThread(connectSocket.accept()).start();
		}
		
		connectSocket.close();
	}

/**
 * Loads private data
 */
static class PrivateData {
	String GRKey;
	
	PrivateData(String filename) throws Exception {
		Scanner scan = new Scanner(new File(filename));
		
		String GRKey = scan.nextLine();
		
		scan.close();
	}
}
	
/**
 * Thread for single writes to a the database
 */
static class DBWriteThread extends Thread {

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

/**
 * Abstract class for DBWriteQueue
 */
static abstract class DBWrite {
	abstract String getSQL();
}

/**
 * Implementation class for DBWriteQueue
 */
static class WriteMe extends DBWrite {
	String getSQL() {
		return "";
	}
}

} // end of DBWriteThread

/**
 * Thread for fetching queued Goodreads data
 */
static class GRFetchThread extends Thread {
	
	String key;
	
	public void run() {
		//TODO fetch new data and insert into database
		
		try {
			
		} catch (Exception e) {
		}
		
	}

	
/**
 * Abstract class for GRFetchQueue
 */
static abstract class GRFetch {
	abstract String getQuery();
}


/**
 * Implementation class for GRFetchQueue
 */
static class FetchMe extends GRFetch {
	String isbn;
	
	FetchMe(String isbn) {
		this.isbn = isbn;
	}
	
	String getQuery() {
		return "https://www.goodreads.com/search/index.xml?key=" + pd.GRKey + "&q=" + isbn;
	}
	
	List<GRBook> fetch() throws Exception { //TODO fix
		//TODO XML Parse code
		return new ArrayList<GRBook>();
	}
}

/**
 * 
 */
static class GRBook {
	String isbn;
	
	String title;
	String author;
	String id;		// Goodreads ID, primary key in DB
	
	int originialPubYear;
	int pubYear;
	
	String imgURL;
	String imgURLSmall;
}

} // end of GRFetchThread


/**
 * Thread for recycling Goodreads database entries
 */
static class GRRecycleThread extends Thread {
	
	public void run() {
		//TODO refresh data
	}

	
/**
 * Abstract class for GRRecycleQueue
 */
static abstract class GRRecycle { // TODO fix
	//Date d = new Date();
	Date refreshAt;
	String book;
}


/**
 * Implementation class for GRRecycleQueue
 */
static class RecycleMe extends GRRecycle {
	
}


} // end of GRRecycleThread


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
	
} // end of UserThread


} // end of BEServer
