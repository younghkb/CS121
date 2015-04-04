import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;

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
			URL query = QueryBuilder.makeSearchURL("0544272994");
			System.out.println(query);
		
			ArrayList<Book> booksFound = BookParser.parseQuery(query);
			
			assertEquals(1, booksFound.size());
			
			Book book = booksFound.get(0);
			
			assertEquals(book.isbn, "0544272994");
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
			URL query = QueryBuilder.makeSearchURL("The Great Gatsby");
			System.out.println(query);
			
			
			ArrayList<Book> booksFound = BookParser.parseQuery(query);
			
			assertTrue(booksFound.size() > 1);
			Book book = booksFound.get(0); 
			
			assertEquals("4671", book.id);
			assertEquals("0743273567", book.isbn);	
			assertEquals("The Great Gatsby", book.title);
			assertEquals("F. Scott Fitzgerald", book.author);
			assertEquals("https://d.gr-assets.com/books/1361191055m/4671.jpg", book.imageUrl);
			assertEquals("https://d.gr-assets.com/books/1361191055s/4671.jpg", book.smallImageUrl);

		} catch (Exception e) {
			fail("Query failed with exception " + e.toString());
			e.printStackTrace();
		}
		
	}

}
