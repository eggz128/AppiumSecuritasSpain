/*
 * It's just Selenium WebDriver, but via Appium server
 */
package appiumcourse.emulator;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;

public class WebTest {

	private AppiumDriver<WebElement> driver;
	
	@Before
	public void setUp() throws Exception {
	    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
	    desiredCapabilities.setCapability("platformName", "Android");
	    desiredCapabilities.setCapability("deviceName", "Nexus 5 Pie");
	    desiredCapabilities.setCapability("udid", "emulator-5554");
	    desiredCapabilities.setCapability("browserName", "chrome");
	    desiredCapabilities.setCapability("chromedriverExecutableDir", "c:/users/edgewords/Documents/appiumChromeDriver");
	    //Important the above cap points to a folder we can write to.
	    
	    URL remoteUrl = new URL("http://localhost:4723/wd/hub");
	    driver = new AppiumDriver<WebElement>(remoteUrl, desiredCapabilities);
	    
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws InterruptedException {
		driver.get("https://www.edgewordstraining.co.uk/webdriver2/");
		
		driver.findElement(By.linkText("Login To Restricted Area")).click();
		
		driver.findElement(By.id("username")).sendKeys("edgewords");
		driver.findElement(By.id("password")).sendKeys("edgewords123");
		
		driver.findElement(By.linkText("Submit")).click();
		
		Thread.sleep(3000);
		
	}

}
