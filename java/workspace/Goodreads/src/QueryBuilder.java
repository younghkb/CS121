import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class QueryBuilder {
	static String key = "";
	static String scheme = "https";
	static String host = "www.goodreads.com";
	static String path = "/search/index.xml";

	public static URL makeURL(String query) throws Exception {
		
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key = scan.nextLine();
		scan.close();
		
		// See https://docs.oracle.com/javase/8/docs/api/java/net/URI.html
		URI uri = new URI(scheme, null, host, -1, path, "key=" + key + "&q=" + query, null); 
		
		return uri.toURL();
	}
	
}