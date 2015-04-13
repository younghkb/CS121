package server.grfetch;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import logging.Duration;
import logging.Log;
import xmlparse.GRParser;
import database.SQLE;
import database.entry.Book;

/**
 * Thread for fetching queued Goodreads data
 */
public class GRFetch {
	
	private final static long MIN_FETCH_DURATION = 1000; // TODO Not request any method more than once a second. Does that mean same parameters?
	
	private static String GRKey = "";
	private final static String SCHEME = "https";
	private final static String HOST = "www.goodreads.com";
	private final static String SEARCHPATH = "/search/index.xml";
	
//	public static void main(String[] args) throws Exception {
//		System.out.println(query("Hop on Pop"));
//	}
	
	public static Book query(String query) throws Exception {	
		return queryBooks(query, Integer.MAX_VALUE).get(0);
	}
	
	public static List<Book> queryBooks(String query) throws Exception {
		return queryBooks(query, Integer.MAX_VALUE);
	}
	
	public static List<Book> queryBooks(String query, int maxLength) throws Exception {	
		List<Book> books = GRParser.parse(fetch(mkQueryURL(query)));
		
		
		int detailsCount = (maxLength < books.size() ? maxLength : books.size());
		for (int i = 0; i < detailsCount; i++) {
			Book book = books.get(i);
			GRParser.parseDetails(book, fetch(mkDetailsURL(book.book_id)));
		}
		
		while (books.size() > detailsCount) {
			books.remove(detailsCount);
		}
		
		return books;
	}
	
	private static synchronized String fetch(URL queryURL) throws Exception {
		long sleepUntil = System.currentTimeMillis() + MIN_FETCH_DURATION;
		
		Duration dur = new Duration();
		Log.log("GRFetch", "Fetch Begin", queryURL.toString());
		
		InputStream is = queryURL.openStream(); // throws an IOException
		
		Scanner s = new Scanner(is); // gives me a warning if I don't split this into two statements and close them seperately
		Scanner scan = s.useDelimiter("\\A");
		
		String input = scan.next();
		
		s.close();
		scan.close();
		
		Log.log("GRFetch", "Fetch Completed", "Duration: " + dur.lap() + "ms");
		
		long sleepFor = sleepUntil - System.currentTimeMillis();
		if (sleepFor > 0) {
			Thread.sleep(sleepFor);
		}
		
		return input;
	}
	
	private static void ldGRKey() throws Exception {
		if (GRKey.equals("")) {
			Scanner scan = new Scanner(new File("data.private"));
			GRKey = scan.nextLine(); // TODO do this once
			scan.close();
		}
	}
	
	private static URL mkQueryURL(String query) throws Exception {
		ldGRKey();
		return new URI(SCHEME, null, HOST, -1, SEARCHPATH, "key=" + GRKey + "&q=" + query, null).toURL();
	}
	
	private static URL mkDetailsURL(int book_id) throws Exception {
		ldGRKey();
		return new URL("https://www.goodreads.com/book/show/" + book_id + "?format=xml&key=" + GRKey);
	}
}