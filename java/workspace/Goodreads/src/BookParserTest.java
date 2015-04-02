import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class BookParserTest {
	
	QueryBuilder builder;
	BookParser parser;
	
	@Before
	public void setUp() {
		builder = new QueryBuilder();
		parser = new BookParser();
	}

	@Test
	public void testISBNQuery() {
		String query = builder.makeURLfromISBN("0544272994");
		
		try {
			List<Book> booksFound = parser.parseQuery(query);
			
			assertEquals(booksFound.size(), 1);
			
			Book book = booksFound.get(0);
			
			//assertEquals(book.isbn, "0544272994");	Not implemented yet.
			assertEquals(book.id, "21413662");
			assertEquals(book.title, "What If?: Serious Scientific Answers to Absurd Hypothetical Questions");
			assertEquals(book.author, "Randall Munroe"); 
			assertEquals(book.imageUrl, "https://d.gr-assets.com/books/1394648139m/21413662.jpg");
			assertEquals(book.smallImageUrl, "https://d.gr-assets.com/books/1394648139s/21413662.jpg");
			
		} catch (Exception e) {
			fail("Query failed with exception " + e.toString());
			e.printStackTrace();
		}
	}

}
