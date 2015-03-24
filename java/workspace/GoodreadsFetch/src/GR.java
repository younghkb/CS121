import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

// modified from code found at
// http://stackoverflow.com/questions/238547/how-do-you-programmatically-download-a-webpage-in-java

public class GR {
	public static void main(String[] args) throws Exception {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		String key = "";
		key += scan.nextLine();
		scan.close();
		
		// "https://www.goodreads.com/search.xml?key=YOUR_KEY&q=Ender%27s+Game"
		url = new URL("https://www.goodreads.com/search/index.xml?key=" + key + "&q=Ender%27s+Game");
		is = url.openStream(); // throws an IOException
		br = new BufferedReader(new InputStreamReader(is));

		PrintWriter pw = new PrintWriter(new File("out.xml"));
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			pw.println(line);
		}

		is.close();
	}
}
