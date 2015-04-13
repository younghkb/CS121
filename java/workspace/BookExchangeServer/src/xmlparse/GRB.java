package xmlparse;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class GRB {
	static String key = "";
	static String scheme = "https";
	static String host = "www.goodreads.com";
	static String searchPath = "/search/index.xml";
	
	public static URL makeSearchURL(String query) throws Exception {
		
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key = scan.nextLine(); // TODO do this once
		scan.close();
		
		// See https://docs.oracle.com/javase/8/docs/api/java/net/URI.html
		URI uri = new URI(scheme, null, host, -1, searchPath, "key=" + key + "&q=" + query, null); 
		
		return uri.toURL();
	}
	
	public static URL makeBookDetailsURL(int bookId) throws Exception {
		
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key = scan.nextLine();
		scan.close();
		
		URL url = new URL("https://www.goodreads.com/book/show/" + bookId + "?format=xml&key=" + key);
		return url;
	}
	
}