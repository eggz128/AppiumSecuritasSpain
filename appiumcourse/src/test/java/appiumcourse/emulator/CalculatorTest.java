//Created with Appium Recorder
package appiumcourse.emulator;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CalculatorTest {

  private AndroidDriver driver; //driver field(variable) accessible to all methods in class

  @Before
  public void setUp() throws MalformedURLException {
	  //Dear Appium server, we would like to run on...
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability("platformName", "Android");
    desiredCapabilities.setCapability("udid", "emulator-5554");  //This particular attached device, which happens to be an emulator
    desiredCapabilities.setCapability("appPackage", "com.android.calculator2");  //Start the calculator for me
    desiredCapabilities.setCapability("appActivity", "com.android.calculator2.Calculator"); //And jump to this view in the calc app
    desiredCapabilities.setCapability("noReset", true); //Don't clear the app cache & data before start
    desiredCapabilities.setCapability("ensureWebviewsHavePages", true);  //Not really needed here. Concerns Hybrid apps.

    URL remoteUrl = new URL("http://localhost:4723/wd/hub");  //Appium server is here

    driver = new AndroidDriver(remoteUrl, desiredCapabilities); //instantiate driver with appium and the desired caps
  }

  @Test
  public void sampleTest() {
    //MobileElement el4 = (MobileElement) driver.findElementById("com.android.calculator2:id/digit_5");
	MobileElement el4 = (MobileElement) driver.findElement(MobileBy.id("com.android.calculator2:id/digit_5")); //Longer form of the above
    el4.click();  //Auto captured element names are bad and should be renamed for readability.
 
    MobileElement el5 = (MobileElement) driver.findElementByAccessibilityId("plus");
    el5.click();
    MobileElement el6 = (MobileElement) driver.findElementById("com.android.calculator2:id/digit_3");
    el6.click();
    MobileElement el7 = (MobileElement) driver.findElementByAccessibilityId("equals");
    el7.click();
    MobileElement el8 = (MobileElement) driver.findElementById("com.android.calculator2:id/result");
    
    
    //If el8 was a chackbox/switch (it is not)
    //You could read its state like so:
    //String checkedstate = el8.getAttribute("checked");
    
    String result = el8.getText(); //Read the calculator result
    // System.out.println(driver.getPageSource()); SOmetimes useful to dump xml of view to console for debugging.
    assertEquals("Result is not 8", "8", result );
    
    
    
    //	Click "clr" button using x and y coords from top left of screen
	//  (new TouchAction(driver)).tap(904, 791).perform() //As recorded with Appium Recorder this is broken. But does give a good start.
    
	//  Tap by point co-ordinates relative to top left of screen    
	//  (new TouchAction(driver)).tap(PointOption.point(904, 791)).perform();

    //  Tap inside a particular element
    //  (new TouchAction(driver)).tap(ElementOption.element(driver.findElementByAccessibilityId("clear"),10,10)).perform();
    
    
    //  Swipe action opens adv calc draw. Code has been fixed from the Recorder generated broken example
    (new TouchAction(driver))
      .press(PointOption.point(1045,1037))  //Finger down
      .moveTo(PointOption.point(289, 999))  //Finger move
      .release()							//Finger up
      .perform();							//DO IT NOW
  }

  @After
  public void tearDown() {
    driver.quit();
  }
}
