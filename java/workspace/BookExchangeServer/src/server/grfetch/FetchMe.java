package server.grfetch;

import java.util.ArrayList;
import java.util.List;

import database.entry.Book;

/**
 * Implementation class for GRFetchQueue
 */
public class FetchMe extends GRFetchElement {
	String isbn;
	
	FetchMe(String isbn) {
		this.isbn = isbn;
	}
	
	String getQuery() {
		return "https://www.goodreads.com/search/index.xml?key=" + GRFetch.GRKey + "&q=" + isbn;
	}
	
	List<Book> fetch() throws Exception { //TODO fix
		//TODO XML Parse code
		return new ArrayList<Book>();
	}
}