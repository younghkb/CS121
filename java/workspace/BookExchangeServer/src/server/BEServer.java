package server;

import java.net.ServerSocket;

//import java.time.Clock; // only for JDK8 -- this class seems much nicer than Date

public class BEServer {
	
	final static int PORT = 6789;
	
	//static ConcurrentLinkedQueue<DBWriteThread.DBWrite> DBWriteQueue = new ConcurrentLinkedQueue<DBWriteThread.DBWrite>();
	//static ConcurrentLinkedQueue<GRFetchThread.GRFetch> GRFetchQueue = new ConcurrentLinkedQueue<GRFetchThread.GRFetch>();
	//static ConcurrentLinkedQueue<GRRecycleThread.GRRecycle> GRRecycleQueue = new ConcurrentLinkedQueue<GRRecycleThread.GRRecycle>();

	static boolean keepRunning = true;

	// TODO make this handle Exceptions correctly
	static void main(String[] args) throws Exception {
	
		// start background threads
		new DBWrite().start();
		//new GRFetch().start();
		new GRRecycle().start();

		ServerSocket connectSocket = new ServerSocket(PORT);
		
		while (keepRunning) {
			new User(connectSocket.accept()).start();
		}
		
		connectSocket.close();
	}
} // end of BEServer
