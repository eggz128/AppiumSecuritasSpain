package appiumcourse.basics;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BareJunitTestCase {

	@Before
	public void setUp() throws Exception {
		//Code to run before each @Test goes here.
		//e.g. open a browser or establish a connection to Appium server
	}

	@After
	public void tearDown() throws Exception {
		//Code to run after each @Test goes here.
		//e.g. close/quit Webdriver/Appium session
	}

	@Test
	public void test() {
		fail("Not yet implemented"); //A test. This one will fail.
	}
	
	@Test
	public void anotherTest() {
		boolean isTrue = true;
		assertEquals("This is displayed on error", true , isTrue);
	}

}
