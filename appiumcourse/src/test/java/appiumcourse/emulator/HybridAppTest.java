/*
 * Hybrid apps are native apps with webviews inside
 * You may not even realise while using it that parts of the app are really displaying a web page...
 * In order to debug a webview the developer must have set some flags at build time
 * This code runs against:
 * https://play.google.com/store/apps/details?id=com.snc.test.webview2&hl=en_US&gl=US
 * 
 * Launches the app with the initial activity (required for this app)
 * Opens the (native) draw
 * (natively) opens a web view
 * Switches the execution context to the webview
 * Does some WebDriver things
 * Switches context back to the native android view
 * 
 * Partially created with Appium Recorder,
 * Appium recorders broken context switch code swapped for something that works.
 */
package appiumcourse.emulator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class HybridAppTest {

	private AndroidDriver<MobileElement> driver;

	@Before
	public void setUp() throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("deviceName", "Nexus 5 Pie");
		desiredCapabilities.setCapability("udid", "emulator-5554");
		desiredCapabilities.setCapability("appPackage", "com.snc.test.webview2");
		desiredCapabilities.setCapability("appActivity", "com.snc.test.webview.activity.SplashActivity");
		desiredCapabilities.setCapability("chromedriverExecutableDir", //Appium will figure out and download the version of chromedriver it needs
				"c:/users/edgewords/Documents/appiumChromeDriver");  //the download must be to a folder we have write permissions for
		desiredCapabilities.setCapability("ensureWebviewsHavePages", true); //Useful for Hybrid apps
		desiredCapabilities.setCapability("noReset", true);

		URL remoteUrl = new URL("http://localhost:4723/wd/hub");

		driver = new AndroidDriver<MobileElement>(remoteUrl, desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}

	@Test
	public void sampleTest() throws InterruptedException {

		MobileElement navDraw = (MobileElement) driver.findElementByAccessibilityId("Open navigation drawer");
		navDraw.click();

//	  //Dump source for debugging locators later
//	  System.out.println(driver.getPageSource());
//	  System.out.println("----------------------------------------");

		MobileElement webviewMenuItem = driver.findElementByXPath(
				"//android.widget.FrameLayout[@resource-id='com.snc.test.webview2:id/nav_view']//android.widget.CheckedTextView[@text='WebView']");

		webviewMenuItem.click();
		MobileElement urlBox = (MobileElement) driver.findElementById("com.snc.test.webview2:id/input_url");
		urlBox.clear();
		urlBox.sendKeys("www.edgewordstraining.co.uk/training-site/");

		MobileElement goButton = (MobileElement) driver.findElementById("android:id/button1");
		goButton.click();

		// Find the WebView
		Set<String> availableContexts = driver.getContextHandles();
		System.out.println("Total No of Context Found After we reach to WebView = " + availableContexts.size());
		for (String context : availableContexts) {
			if (context.contains("WEBVIEW")) {
				System.out.println("Context Name is " + context);
				// Call context() method with the id of the context you want to access
				// and change it to WEBVIEW_1. This puts Appium session into a mode where all
				// commands
				// are interpreted as being intended for automating the web view
				driver.context(context);
				break;
			}
		}
		
		// Now in WebView - use standard Selenium Webdriver
		driver.findElement(By.partialLinkText("Form")).click();
		Thread.sleep(3000);
		
		// Switch back to native
		for (String context : availableContexts) {
			if (context.contains("NATIVE")) {
				System.out.println("We are Back to " + context);
				driver.context(context);
				System.out.println("Context Switched");
			}
		}
		
		// Native menu interaction
		navDraw.click();
		Thread.sleep(3000);

	}

	@After
	public void tearDown() {
		driver.quit();
	}
}