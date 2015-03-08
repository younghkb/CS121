import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

// modified from code found at
// https://gist.github.com/roroco/11131088

public class Client {
	public static void main(String args[]) throws Exception {
		
		// load private data
		Scanner scan = new Scanner(new File("data.private"));
		String ip = "";
		ip += scan.nextLine();
		
		int port = 0;
		port = Integer.parseInt(scan.nextLine());
		
		scan.close();
		
		
		
		System.out.println("Client Start");

		String sentence;
		String modifiedSentence;
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket(ip, port);
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		boolean swtch = true;
		
		while (swtch) {
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			modifiedSentence = inFromServer.readLine();
			
			System.out.println("FROM SERVER: " + modifiedSentence);
		}

		clientSocket.close();
	}
}
