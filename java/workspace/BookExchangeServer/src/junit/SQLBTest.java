package junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.SQLB;
import database.entry.Exchange;

public class SQLBTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void bookTests() {
		System.out.println(SQLB.updateBook(1, "Hop on Pop", "Dr. Seuss", "123", "1950", "1940", "a.com", "b.com"));
		assertEquals(
				SQLB.createBook(1, "Hop on Pop", "Dr. Seuss", "123", "1950", "1940", "a.com", "b.com"),
				"insert into books values (1, 'Hop on Pop', 'Dr. Seuss', '123', '1950', '1940', 'a.com', 'b.com', datetime('now'));"
				);
		assertEquals(
				SQLB.updateBook(1, "Hop on Pop", "Dr. Seuss", "123", "1950", "1940", "a.com", "b.com"),
				"update books set book_id = 1, book_title = 'Hop on Pop', author = 'Dr. Seuss', isbn = '123', pub_year = '1950', orig_pub_year = '1940', image_url = 'a.com', small_image_url = 'b.com', add_date = datetime('now') where book_id = 1;"
				);
		assertEquals(
				SQLB.deleteBook(1),
				"delete from books where book_id = 1;"
				);
		assertEquals(
				SQLB.getBook(1),
				"select * from books where book_id = 1;"
				);
		assertEquals(
				SQLB.getBookFromISBN("123"),
				"select * from books where isbn = 123;"
				);
		assertEquals(
				SQLB.getOldestBook(),
				"select * from books where add_date = (select min(add_date) from books);"
				);
	}
	
	@Test
	public void exchangeTests() {		
		assertEquals(
				SQLB.createExchange(1, Exchange.Type.BORROW, 2, "Hello"),
				"insert into exchanges (borrower_id, exchange_type, book_id, book_title, create_date, status) values (1, 'BORROW', 2, 'Hello', datetime('now'), 'INITIAL');"
				);
		assertEquals(
				SQLB.createExchange(1, Exchange.Type.LOAN, 2, "Hello"),
				"insert into exchanges (loaner_id, exchange_type, book_id, book_title, create_date, status) values (1, 'LOAN', 2, 'Hello', datetime('now'), 'INITIAL');"
				);
		assertEquals(
				SQLB.updateBorrower(2, 1),
				"update exchanges set borrower_id = 1 where exchange_id = 2;"
				);
		assertEquals(
				SQLB.updateLoaner(2, 1),
				"update exchanges set loaner_id = 1 where exchange_id = 2;"
				);
		assertEquals(
				SQLB.updateExchangeStatus(1, Exchange.Status.RESPONSE),
				"update exchanges set status = 'RESPONSE' where exchange_id = 1;"
				);
		assertEquals(
				SQLB.getExchange(1),
				"select * from exchanges where exchange_id = 1;"
				);
		assertEquals(
				SQLB.getPublicExchanges(),
				"select * from exchanges where status = 'INITIAL';"
				);
		assertEquals(
				SQLB.getPrivateExchanges(1),
				"select * from exchanges where loaner_id = 1 or borrower_id = 1;"
				);
		//fail("Not yet implemented");
	}

}
