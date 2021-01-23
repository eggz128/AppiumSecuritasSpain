package appiumcourse.basics;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver; //Appium is built upon Webdriver - it's one of the dependencies pulled in via POM.xml
import org.openqa.selenium.chrome.ChromeDriver; //Class to interact with Chromedriver.exe

public class BasicWebDriverExample {
	
	private WebDriver driver; //A field (variable) that will hold our WebDrievr instance. Accessible by all methods in this class.

	@Before
	public void setUp() throws Exception {
		driver = new ChromeDriver(); //Assumes ChromeDriver.exe is on your path
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //Allow WebDriver to keep trying to find an element for up to 10 seconds.
	}

	@After
	public void tearDown() throws Exception {
		Thread.sleep(5000); //Wait before ending the run, so we can see what happened.
		driver.quit();
		
	}

	@Test
	public void test() {
		driver.get("https://www.edgewordstraining.co.uk/webdriver2");
		
		driver.findElement( //"Chrome...find an element..."
				By.linkText("Login To Restricted Area")) //"...by looking for a link with some text...if you find it..."
				.click(); //"...click  it!
		
		// No need to worry about waiting for the next page to load as there is an implicit timeout set in @Before
		
		driver.findElement(By.id("password")) //Find the password field via its id attribute
			.sendKeys("edgewords"); //type in. If there is text already there use .clear() first
		
		driver.findElement(By.id("username")).sendKeys("Edgewords123");
		
		driver.findElement(By
				.xpath("//*[@id='Login']//a[1][contains(.,'Submit')]")) //If there's no easy way to find an element you can use xpath
						//Find an element with an id attribute/property of 'Login'
						//Then find an element inside that is a link, AND is the first link, AND contains the text 'Submit'
				.click(); //Then click it
		
		//Finish the test with an assertion
		
		//Capture the text of the heading on the final page
		//Hopefully this will happen after we have navigated to the new page
		//To be more robust we would wait here.
		String headingText = driver.findElement(By.xpath("//*[@id='right-column']/h1"))
									.getText(); //Capture the  <tags>text in</tags> tags
												//If you want an attribute/property use .getAttribute("AttributeName");
		
		assertEquals("We are not on the Add Record page. Login failed?", "Add A Record To the Database",headingText);
		
		
	}

}
