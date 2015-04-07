package server.user;

import java.net.Socket;

/**
 * Thread for accepted user connections
 */
public class UserThread extends Thread {
	Socket user;
	
	public void run() {
		//TODO parse commands and issue orders
	}
	
	public UserThread(Socket user) {
		this.user = user;
	}
	
} // end of UserThread
