import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Driver {
	static volatile ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<String>();

	public static void main(String[] args) {
		System.out.println("BEGIN");
		new InThread().start();
		new OutThread().start();
		while (true)
			;
	}

	public static class InThread extends Thread {
		public void run() {
			Scanner scan = new Scanner(System.in);
			while (true) {
				System.out.print("Input: ");
				String in = scan.nextLine();
				if (in.equals("go")) {
					synchronized (q) {
						q.notifyAll();
					}
				} else {
					q.add(in);
				}
			}
		}
	}

	public static class OutThread extends Thread {
		public void run() {
			try {
				while (true) {
					PrintWriter pw = new PrintWriter(new File("out.txt"));
					while (q.size() > 0) {
						String s = q.remove();
						pw.println(s);
						pw.println("=========================");
						URL url = new URL(s);
						InputStream is = url.openStream(); // throws an
															// IOException
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						

						String out = br.readLine();
						while (out != null) {
							pw.println(out);
							out = br.readLine();
						}
					}
					pw.flush();
					pw.close();
					System.out.println("WAIT");
					synchronized (q) {
						q.wait();
					}
				}
			} catch (Exception e) {
			}
		}
	}
}
