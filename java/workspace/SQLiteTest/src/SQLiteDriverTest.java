import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SQLiteDriverTest {

/** Tests that make sure the SQL commands execute without error. They do not verify all changes in the database
 * and can be made more robust once we can work with the information returned from the database. 
 * @throws Exception */
	
	// Currently only use SQLiteDriver.update()
	// TODO difference between query and update?
	
	@Before
	public void setUp() throws Exception {
		// Empty the db so we can have a blank slate. 
		
		String sql = SQLB.deleteAllBooks();
		SQLiteDriver.update(sql);
		
		sql = SQLB.deleteAllExchanges();
		SQLiteDriver.update(sql);
	}
	
	@After
	public void tearDown() throws Exception {
		// Empty the tables so we leave the database in a clean state.
		
		String sql = SQLB.deleteAllBooks();
		SQLiteDriver.update(sql);
		
		sql = SQLB.deleteAllExchanges();
		SQLiteDriver.update(sql);
	}
	
	
	@Test
	public void testExchanges() {

		try {
			
			// Create two exchanges.
			String comm = SQLB.createExchangeLoan(100, 200, "This is a book");			
			SQLiteDriver.update(comm);
			
			comm = SQLB.createExchangeBorrow(300, 400, "This is another book");
			SQLiteDriver.update(comm);
			
			// Get those exchanges
			comm = SQLB.getPrivateExchanges(100);
			SQLiteDriver.update(comm);

			comm = SQLB.getPrivateExchanges(300);			
			SQLiteDriver.update(comm);
			
			comm = SQLB.getPublicExchanges();
			SQLiteDriver.update(comm);
			
			comm = SQLB.getExchangeForBook(200);
			SQLiteDriver.update(comm);
			
			// Update those exchanges
			// TODO uncomment once we have a way of getting an exchangeId from the query. 
			
			/*
			comm = SQLB.addBorrower(exchangeId, 300);
			SQLiteDriver.update(comm);
			
			comm = SQLB.addLoaner(exchangeId, 100);
			SQLiteDriver.update(comm);
			
			comm = SQLB.updateExchangeStatus(exchangeId, Status.ACCEPTED);
			SQLiteDriver.update(comm);
			
			comm = SQLB.getExchange(exchangeId);
			SQLiteDriver.update(comm); */

			
		} catch (Exception e) {
			e.printStackTrace();
			fail();

		}		
	}
	
	@Test
	public void testBooks() {
		
		try {
			String comm = SQLB.insertBook(21413662, "What If?", "Randall Munroe", "0544272994", 2014, 2014,
					"https://d.gr-assets.com/books/1394648139m/21413662.jpg", "https://d.gr-assets.com/books/1394648139m/21413662.jpg");			
			SQLiteDriver.update(comm);
			
			// TODO insert another example book
			
			comm = SQLB.findBookFromISBN("0544272994");
			SQLiteDriver.update(comm);
			
			// TODO verify that this is indeed the oldest book
			comm = SQLB.getOldestBook();
			SQLiteDriver.update(comm);

			comm = SQLB.deleteBook(21413662);
			SQLiteDriver.update(comm);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
