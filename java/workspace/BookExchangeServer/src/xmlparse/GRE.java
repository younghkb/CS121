package xmlparse;

import java.util.List;

import logging.Log;
import database.entry.Book;

public abstract class GRE {
	public static List<Book> queryBook(String query) throws Exception {
		Log.log("GRE", "Query", "query = " + query);
		return GRP.queryBook(GRB.makeSearchURL(query));
	}
}
