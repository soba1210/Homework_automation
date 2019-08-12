import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class MainTestClass {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "9");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "/Users/siinc/Desktop/JavaAppiumAutomation/JavaAppiumAutomation/apk/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testKeywordSearchPresence() {

        waitForElementAndClick(
                By.id("search_container"),
                "no search filed found",
                5
        );
        WebElement searchField = waitForElementPresent(
                By.id("search_src_text"),
                "",
                12
        );

        String keywordInSearchField = searchField.getAttribute("text");
        Assert.assertEquals(
                "another text in search field",
                "Search…",
                keywordInSearchField);
    }

    @Test
    public void cancelSearchTest() {

        waitForElementAndClick(
                By.id("search_container"),
                "no search filed found",
                5
        );

        waitForElementAndSendKeys(
                By.id("search_src_text"),
                "cannot find search field",
                "Automation",
                5
        );

        waitForElementPresent(
                By.xpath("//android.widget.ListView/android.widget.LinearLayout[5]"),
//                By.xpath("//*[@resource-id='page_list_item_container']//*[contains(@index,'4')]"),

                "cannot find the fifth search result",
                6
        );
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton"),
                "cannot find close search button",
                5
        );
        waitForElementNotPresent(
                By.id("search_close_btn"),
                "close search button is still present",
                6
        );


    }

    @Test
    public void testMatchingSearchResultToKeyword() {
        String word = "Automation";

        waitForElementAndClick(
                By.id("search_container"),
                "no search filed found",
                5
        );

        waitForElementAndSendKeys(
                By.id("search_src_text"),
                "cannot find search field",
                word,
                5
        );

//        for (int i = 1; i < 10; i++) {
//            waitForElementPresent(
//                    By.xpath("//android.widget.ListView/android.widget.LinearLayout[" + i + "]//*[contains(@text,'" + word + "')]"),
//                    "Search title # " + i + " doesn't contain keyword " + word,
//                    5);
//
//
//        }
//
        waitForElementPresent(By.id("page_list_item_title"), "element not shown", 5);
        List<WebElement> resultsList = driver.findElements(By.id("page_list_item_title"));


        for (int i = 0; i < resultsList.size(); i++) {
            WebElement element=resultsList.get(i);
            System.out.println("text= " + element.getText());
            Assert.assertTrue("element with text: " + element.getText() + " doesnt contain text: " + word + " with number " + i
                    , element.getText().contains("1"));
        }


    }


    private WebElement waitForElementPresent(By by, String error_message, long timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String error_message, String value, long timeInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    //  private WebElement waitForElementAndCompare(By by, String error_message, )
}

