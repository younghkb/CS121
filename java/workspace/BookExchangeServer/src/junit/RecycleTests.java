package junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.grrecycle.GRRecycle;
import database.SQLE;

public class RecycleTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		SQLE.deleteAllBooks();
//		SQLE.deleteAllExchanges();
//		SQLE.deleteAllUsers();
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
		System.out.println("DERP");
	}

}
