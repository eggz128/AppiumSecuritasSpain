/**
 * Touch Action moves happen *instantly* - this can cause problems recognising that action for some applications
 * Appium, being built upon WebDriver, has access to a more complex, but flexible gesture API
 * Movements can be given a duration, so our virtual fingers don't "warp" about the screen
 * 
 * Script opens the notifications panel, then slowly drags down to reveal the brightness slider using w3c actions
 * Finally the slider is set to a value. The needed tap location is calculated as there is no simple setvalue command.
 */
package appiumcourse.emulator;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class W3CActionsDemo {

  private AndroidDriver<MobileElement> driver;

  @Before
  public void setUp() throws MalformedURLException {
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability("platformName", "Android");
    desiredCapabilities.setCapability("deviceName", "Nexus 5 Pie");
    desiredCapabilities.setCapability("udid", "emulator-5554");
//    desiredCapabilities.setCapability("appPackage", "com.google.android.apps.nexuslauncher");
//    desiredCapabilities.setCapability("appActivity", "com.google.android.apps.nexuslauncher.NexusLauncherActivity");
    desiredCapabilities.setCapability("ensureWebviewsHavePages", true);
    desiredCapabilities.setCapability("noReset", true);

    URL remoteUrl = new URL("http://localhost:4723/wd/hub");

    driver = new AndroidDriver<MobileElement>(remoteUrl, desiredCapabilities);
  }

  @Test
  public void slidertest() throws InterruptedException {
	  
	  //gather data for calculations later
	  Dimension windowSize = driver.manage().window().getSize();
	  
	  int scrHeight = windowSize.height;
	  int scrWidth = windowSize.width;
	  
	  //Oficially only works on emulators, but worked on a real Galaxy S8
	  driver.openNotifications();
	  
	  
	  
	  //W3C Action
	  WebElement dragMe = (MobileElement) driver.findElementById("com.android.systemui:id/qs_drag_handle_view");
	  
	  Point start = ((MobileElement) dragMe).getCenter();
	  Point end = new Point(scrWidth / 2, 1700); 
	  PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
	  Sequence dragNDrop = new Sequence(finger, 1);
	  dragNDrop.addAction(finger.createPointerMove(Duration.ofMillis(0),
	                      PointerInput.Origin.viewport(), start.x, start.y));
	  dragNDrop.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
	  dragNDrop.addAction(finger.createPointerMove(Duration.ofMillis(100), //Note movement duration
	                      PointerInput.Origin.viewport(),end.x, end.y));
	  dragNDrop.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
	  
	  driver.perform(Arrays.asList(dragNDrop));
	  
	  
	  
	  WebDriverWait wait = new WebDriverWait(driver, 10); //Maybe we should wait to be sure the slider is there
	  wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("com.android.systemui:id/slider")));
	  
	  
	  //Set slider
	  MobileElement slider = driver.findElementById("com.android.systemui:id/slider");
	 
	  int paddingFudge = 57; //Have to physically measure the padding between the element and the actual slider start
	  						//This could vary by device/theme
	  int sliderStart = slider.getLocation().getX();
	  int sliderY = slider.getLocation().getY();
	  int sliderEnd = slider.getSize().getWidth()-(paddingFudge*2);
	  int moveTo = (int) (sliderEnd*0.5);  //Change float to pick different brightness levels (slider values)
	  TouchAction<?> moveSlider = new TouchAction(driver)
			  .press(PointOption.point(sliderStart+paddingFudge,sliderY))
			  .moveTo(PointOption.point(sliderStart+paddingFudge+moveTo,sliderY))
			  .release();
	  
	  
	  
	  moveSlider.perform(); //DO IT
	  
  }
  


  @After
  public void tearDown() {
	  driver.quit();
  }
}
