package junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.grrecycle.GRRecycle;
import database.SQLE;

public class DBWriteTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SQLE.deleteAllBooks();
		SQLE.deleteAllExchanges();
		SQLE.deleteAllUsers();
		
		new GRRecycle().start();
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
		while (true) {
			
		}
	}

}
