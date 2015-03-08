import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

// modified from code found at
// https://gist.github.com/roroco/11131088

public class Server implements Runnable  {
	
	Socket socket;
	
	public Server(Socket socket) {
		this.socket = socket;
		System.out.println("THREAD START");
	}
	
	
	public static void main(String args[]) throws Exception {
		
		// load private data
		Scanner scan = new Scanner(new File("data.private"));
		scan.nextLine();
		int port = 0;
		port = Integer.parseInt(scan.nextLine());
		scan.close();
				
		
		ServerSocket welcomeSocket = new ServerSocket(port);
		
		System.out.println("Server Start");
		boolean swtch = true;
		
		
		while (swtch) {
			new Thread(new Server(welcomeSocket.accept())).start();
		}
		
		welcomeSocket.close();
	}
	
	public void run() {
		String clientSentence;
		String capitalizedSentence;
		
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

			while (true) {
				//Socket connectionSocket = Socket.accept();
				System.out.println("LOOP");
				clientSentence = inFromClient.readLine();
				
				System.out.println("Received: " + clientSentence);
				
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);
			}
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}
}
