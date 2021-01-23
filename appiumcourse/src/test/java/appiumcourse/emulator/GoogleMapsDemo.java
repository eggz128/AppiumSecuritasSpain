/**
 * Zoom in Google Maps using Appium Touch/Multitouch Actions
 * Then another zoom using a UIAutomator2 backend shortcut
 * Finally take a couple of screenshots
 */
package appiumcourse.emulator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class GoogleMapsDemo {

  private AndroidDriver<MobileElement> driver;  //If you want you can specify driver returns MobileElements

  @Before
  public void setUp() throws MalformedURLException {
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability("platformName", "Android");
    desiredCapabilities.setCapability("deviceName", "Nexus 5 Pie");
    desiredCapabilities.setCapability("udid", "emulator-5554");
    desiredCapabilities.setCapability("appPackage", "com.google.android.apps.maps");
    desiredCapabilities.setCapability("appActivity", "com.google.android.maps.MapsActivity");
    desiredCapabilities.setCapability("ensureWebviewsHavePages", true);
    desiredCapabilities.setCapability("noReset", true);

    URL remoteUrl = new URL("http://localhost:4723/wd/hub");

    driver = new AndroidDriver<MobileElement>(remoteUrl, desiredCapabilities); //If you want you can specify driver returns MobileElements
    //This avoids Raw Type warnings in the IDE, but now you can't potentially reuse this on iOS/Web/Windows
  }

  @Test
  public void sampleTest() throws InterruptedException {
	  

	  //Zoom using Touch Actions
	  //Define two independent finger actions
	  //Then combine and execute them as a MultiTouch action
	  
	  //Rather than just supplying co-ordinates (could change with different screen sizes)
	  //Just try and do this relative to the reported current screen size.
	  Dimension windowSize = driver.manage().window().getSize();
	  
	  int scrHeight = windowSize.height;
	  int scrWidth = windowSize.width;
	  
	  //Tap center of screen
	  (new TouchAction<>(driver)).tap(PointOption.point(scrWidth / 2, scrHeight / 2)).perform();
	  
	  // Multi touch actions - Start by defining individual touch actions
	  TouchAction finger1 = new TouchAction(driver);
	  TouchAction finger2 = new TouchAction(driver);
	  // press finger one centre of the screen and then move x and y axis relatively left and up
	  finger1.press(PointOption.point(scrWidth / 2, scrHeight / 2 - 100))
	  .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
	  .moveTo(PointOption.point((int) (scrWidth*.5), (int) (scrHeight*.3))).release();
	  // press finger two below centre of the screen then move
	  finger2.press(PointOption.point(scrWidth / 2, scrHeight / 2 + 100))
	  .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
	  .moveTo(PointOption.point((int) (scrWidth*.5), (int) (scrHeight*.7))).release();
	  	  
	  System.out.println("Fingers defined");
	  
	  // Then create a multiTouch object, add the individual actions and perform();
	  MultiTouchAction multiTouch = new MultiTouchAction(driver);
	  multiTouch.add(finger1).add(finger2);
	  multiTouch.perform();// now perform both the finger actions simultaneously

	  System.out.println("Multitouch performed");
	  
	  Thread.sleep(3000);
	  System.out.println("Finished");
	  
	  Thread.sleep(3000);
	  
	  System.out.println(driver.getDisplayDensity()); //Docs say default speed below is pixel density * 2500, but that can't be right
	  
	  ((JavascriptExecutor) driver).executeScript("mobile: pinchOpenGesture", ImmutableMap.of(
			    "elementId", ((RemoteWebElement) driver.findElementById("com.google.android.apps.maps:id/explore_tab_home_bottom_sheet")).getId(),
			    "percent", 0.75
			    ,
			    "speed", 1000 //not too fast, not too slow...
			    
			));
	  
	  
	  
	  Thread.sleep(3000); 
	  System.out.println("Done");
	  
	  
	  
	  
	  //Take a screenshot of the entire screen, or just a sub element.
		try {
			File scrFile = driver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("C:\\users\\edgewords\\Pictures\\appiumscreen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			MobileElement map = driver.findElementById("com.google.android.apps.maps:id/explore_tab_home_bottom_sheet");
			File elemFile = map.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(elemFile, new File("C:\\users\\edgewords\\Pictures\\appiumselem.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	  Thread.sleep(10000);
	  
	  
	  
  }
  


  @After
  public void tearDown() {
    driver.quit();
  }
}
