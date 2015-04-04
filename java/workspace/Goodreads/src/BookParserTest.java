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
		
		try {
			String query = QueryBuilder.makeURL("0544272994");
			System.out.println(query);
		
			List<Book> booksFound = BookParser.parseQuery(query);
			
			assertEquals(1, booksFound.size());
			
			Book book = booksFound.get(0);
			
			//assertEquals(book.isbn, "0544272994");	Not implemented yet.
			assertEquals("21413662", book.id);
			assertEquals("What If?: Serious Scientific Answers to Absurd Hypothetical Questions", book.title);
			assertEquals("Randall Munroe", book.author); 
			assertEquals("https://d.gr-assets.com/books/1394648139m/21413662.jpg", book.imageUrl);
			assertEquals("https://d.gr-assets.com/books/1394648139s/21413662.jpg", book.smallImageUrl);
			
		} catch (Exception e) {
			fail("Query failed with exception " + e.toString());
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTitleQuery() {
		
		try {
			String query = QueryBuilder.makeURL("The Great Gatsby");
			System.out.println(query);
			
			//assertEquals(book.isbn, "0544272994");	Not implemented yet.

		} catch (Exception e) {
			fail("Query failed with exception " + e.toString());
			e.printStackTrace();
		}
		
	}

}
