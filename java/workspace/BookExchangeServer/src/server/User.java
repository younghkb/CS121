package server;

import java.net.Socket;

/**
 * Thread for accepted user connections
 */
public class User extends Thread {
	Socket user;
	
	public void run() {
		//TODO parse commands and issue orders
	}
	
	public User(Socket user) {
		this.user = user;
	}
	
} // end of UserThread
