package server.grfetch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import logging.Duration;
import logging.Log;

import org.xml.sax.SAXException;

import client.Book;
import database.SQLB;

/**
 * Thread for fetching queued Goodreads data
 */
public class GRFetch {
	
	private final static long MIN_FETCH_DURATION = 150; // TODO Not request any method more than once a second. Does that mean same parameters?
	
	private static String GRKey = "";
	private final static String SCHEME = "https";
	private final static String HOST = "www.goodreads.com";
	private final static String SEARCHPATH = "/search/index.xml";
	
	public static void main(String[] args) throws Exception {
		System.out.println("Print");
		System.out.println(SQLB.createBook(query("Hop on Pop")));
	}
	
	public static Book query(String query) {	
		return queryBooks(query, 1).get(0);
	}
	
	public static List<Book> queryBooks(String query) {
		return queryBooks(query, Integer.MAX_VALUE);
	}
	
	public static List<Book> queryBooks(String query, int maxLength) {
		List<Book> books = null;
		
		try {
			books = GRParser.parse(fetch(mkQueryURL(query)));		
			int detailsCount = (maxLength < books.size() ? maxLength : books.size());
			for (int i = 0; i < detailsCount; i++) {
				Book book = books.get(i);
				GRParser.parseDetails(book, fetch(mkDetailsURL(book.book_id)));
			}
			
			while (books.size() > detailsCount) {
				books.remove(detailsCount);
			}
		} catch (ParserConfigurationException|IOException|SAXException e) {
			System.err.println(e);
		}
		
		return books;
	}
	
	private static synchronized String fetch(URL queryURL) throws IOException {
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
			try {
				Thread.sleep(sleepFor);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		return input;
	}
	
	private static void ldGRKey() throws FileNotFoundException {
		if (GRKey.equals("")) {
			Scanner scan = new Scanner(new File("data.private"));
			GRKey = scan.nextLine(); // TODO do this once
			scan.close();
		}
	}
	
	private static URL mkQueryURL(String query) throws MalformedURLException {
		try {
			ldGRKey();
			return new URI(SCHEME, null, HOST, -1, SEARCHPATH, "key=" + GRKey + "&q=" + query, null).toURL();
		} catch (FileNotFoundException|URISyntaxException e) {
			System.err.println(e);
			throw new MalformedURLException(); // throws this since if either of these exceptions occur, it cannot form a valid URL
		}
	}
	
	private static URL mkDetailsURL(int book_id) throws MalformedURLException {
		try {
			ldGRKey();
			return new URL("https://www.goodreads.com/book/show/" + book_id + "?format=xml&key=" + GRKey);
		} catch (FileNotFoundException e) {
			System.err.println(e);
			throw new MalformedURLException(); // throws this since if this exception occurs, it cannot form a valid URL
		}
	}
}