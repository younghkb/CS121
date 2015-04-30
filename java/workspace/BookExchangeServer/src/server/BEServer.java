package server;

import java.net.ServerSocket;

import logging.Log;
import server.threads.DBWrite;
import server.threads.GRRecycle;
import server.threads.UserSession;

//import java.time.Clock; // only for JDK8 -- this class seems much nicer than Date

public abstract class BEServer {
	
	final static int PORT = 6789;
	
	//static ConcurrentLinkedQueue<DBWriteThread.DBWrite> DBWriteQueue = new ConcurrentLinkedQueue<DBWriteThread.DBWrite>();
	//static ConcurrentLinkedQueue<GRFetchThread.GRFetch> GRFetchQueue = new ConcurrentLinkedQueue<GRFetchThread.GRFetch>();
	//static ConcurrentLinkedQueue<GRRecycleThread.GRRecycle> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycleThread.GRRecycle>();

	static boolean keepRunning = true;

	// TODO make this handle Exceptions correctly
	public static void main(String[] args) throws Exception {
		
		Log.log("BEServer", "Server Start", "");
		
		// start background threads
		new DBWrite().start();
		//new GRFetch().start();
		new GRRecycle().start();

		ServerSocket connectSocket = new ServerSocket(PORT);
		
		while (keepRunning) {
			new UserSession(connectSocket.accept()).start();
		}
		
		connectSocket.close();
	}
} // end of BEServer
