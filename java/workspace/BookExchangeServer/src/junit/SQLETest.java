package junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.SQLE;

public class SQLETest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SQLE.deleteAllBooks();
		SQLE.deleteAllExchanges();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void test() throws Exception {
		SQLE.createBook(206962, "Hop On Pop", "Dr. Seuss", "0007158491", "2003", "1963", "https://d.gr-assets.com/books/1327947739m/206962.jpg", "https://d.gr-assets.com/books/1327947739s/206962.jpg");
		System.out.println(SQLE.getOldestBook());
		
		
	}
}
