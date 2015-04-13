package junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.SQLE;
//import xmlparse.GRE;

public class GR_SQLE_Test {

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
		//DBWrite.queue(SQLB.createBook(GRE.queryBook("Hop On Pop").get(0)));
		//DBWrite.queue(SQLB.createBook(GRE.queryBook("Catch 22").get(0)));
		//DBWrite.queue(SQLB.createBook(GRE.queryBook("Bakemonogatari").get(0)));
		//DBWrite.queue(SQLB.createBook(GRE.queryBook("The Hobbit").get(0)));
	}
}
