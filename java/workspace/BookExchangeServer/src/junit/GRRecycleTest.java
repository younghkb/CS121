package junit;

import logging.Log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.DBWrite;
import server.GRRecycle;
import database.SQLE;
//import xmlparse.GRE;

public class GRRecycleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log.event("BEFORE CLASS");
		try {
			SQLE.deleteAllBooks();
			SQLE.deleteAllExchanges();
			SQLE.deleteAllUsers();
			
			
			//DBWrite.queue(SQLB.createBook(GRE.queryBook("Hop On Pop").get(0)));
			Thread.sleep(100);
			//DBWrite.queue(SQLB.createBook(GRE.queryBook("Catch 22").get(0)));
			Thread.sleep(100);
			//DBWrite.queue(SQLB.createBook(GRE.queryBook("Bakemonogatari").get(0)));
			Thread.sleep(100);
			//DBWrite.queue(SQLB.createBook(GRE.queryBook("The Hobbit").get(0)));
			Thread.sleep(100);
			
			new DBWrite().start();
			new GRRecycle().start();
			
		} catch (Exception e) {
			System.err.println(e);
		}
		
		Log.event("AFTER CLASS");
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
		Log.event("TEST START");
		while (true) {
			Thread.sleep(1000);
		}
	}

}
