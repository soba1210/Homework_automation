import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static java.time.Duration.ofSeconds;

public class MainTestClass {

    private static AppiumDriver driver;

    @BeforeClass
    public static void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "9");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        // capabilities.setCapability("app", "/Users/siinc/Desktop/JavaAppiumAutomation/JavaAppiumAutomation/apk/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        System.out.println("setUp");

    }


    @AfterClass
    public static void tearDown() {
        System.out.println("tearDown");

        driver.quit();
    }


    @Before
    public void setPortait() {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    @After
    public void resetAPP() {
        driver.resetApp();
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
            WebElement element = resultsList.get(i);
            System.out.println("text= " + element.getText());
            Assert.assertTrue("element with text: " + element.getText() + " doesnt contain text: " + word + " with number " + i
                    , element.getText().contains(word));
        }


    }


    @Test
    public void savingTwoArticlesInReadingList() {
        String name_of_list = "Island";
        String first_search_request = "Java";
        String subtitle_of_first_article = "Island of Indonesia";
        String second_search_request = "Appium";

        waitForElementAndClick(
                By.id("search_container"),
                "search field not found",
                6
        );


        waitForElementAndSendKeys(
                By.id("search_src_text"),
                "keyword 'Java' not found",
                first_search_request,
                5);

        waitForElementAndClick(
                By.xpath("//*[@text='" + subtitle_of_first_article + "']"),
                "Island of Indonesia not found",
                6
        );

        waitForElementAndClick(
                By.xpath("//*[@content-desc='More options']"),
                "Options not found",
                12
        );

        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Button 'Add to reading list' not found",
                5
        );


        waitForElementAndClick(
                By.xpath("//*[@text='GOT IT']"),
                "Button 'GOT IT' not found",
                3
        );

        waitForElementAndClear(
                By.id("text_input"),
                "Field with text input not found",
                5
        );


        waitForElementAndSendKeys(
                By.id("text_input"),
                "Field with text input not found",
                name_of_list,
                5
        );

        waitForElementAndClick(
                By.id("android:id/button1"),
                "Button 'OK' not found",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@content-desc='Navigate up']"),
                "Close button not found(button that closing the article)",
                5
        );

        waitForElementAndClick(
                By.id("search_container"),
                "search field not found",
                6
        );


        waitForElementAndSendKeys(
                By.id("search_src_text"),
                "keyword '" + second_search_request + "' not found",
                second_search_request,
                5);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + second_search_request + "']"),
                second_search_request + " article not found",
                6
        );

        waitForElementAndClick(
                By.xpath("//*[@content-desc='More options']"),
                "Options not found",
                12
        );

        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Button 'Add to reading list' not found",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='" + name_of_list + "']"),
                "List with title " + name_of_list + " not found",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@content-desc='Navigate up']"),
                //     By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Close button not found(button that closing the article)",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@content-desc='My lists']"),
                "Button 'My lists' not found",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@text='" + name_of_list + "']"),
                "List with title " + name_of_list + " not found",
                5
        );

//        Тесты с коллекцией, не хочу удалять, пригодятся заметки
//        waitForElementPresent(By.id("org.wikipedia:id/page_list_item_container"),"dsdd",30);
//        List<WebElement> k = driver.findElements(By.id("org.wikipedia:id/page_list_item_container"));
//
////        System.out.println(k);
//        System.out.println(k.size());
//        swipeToLeftByElement(k.get(1),"fgfgfgfgf");

        swipeElementToLeft(
                By.xpath("(//*[@resource-id='org.wikipedia:id/page_list_item_container'])[2]"), //Подправил Xpath, предыдущий не совсем корректно обтрабатывал
                subtitle_of_first_article + " not found in My reading lists");

        waitForElementNotPresent(
                By.xpath("//*[@text='" + subtitle_of_first_article + "']"),
                "island of Indonesia in my list still present after deleting",
                5
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + second_search_request + "']"),
                "Article with title 'Appium' not found in Reading list",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + second_search_request + "']"),
                "Appium article in my reading list not found",
                6
        );

        String article_title = waitForElementAndGetArticleTitle(
                By.id("view_page_title_text"),
                "text",
                "cannot find article title",
                5
        );

        Assert.assertEquals(
                "Article title after entering from 'My reading list' differs from original article title ",
                second_search_request,
                article_title);


    }


    @Test
    public void testTitlePresent() {

        String search_keyword_request = "Java";
        String subtitle_of_article = "Island of Indonesia";

        waitForElementAndClick(
                By.id("search_container"),
                "search field not found",
                5
        );


        waitForElementAndSendKeys(
                By.id("search_src_text"),
                "keyword 'Java' not found",
                search_keyword_request,
                5);

        waitForElementAndClick(
                By.xpath("//*[@text='" + subtitle_of_article + "']"),
                "Island of Indonesia not found",
                5
        );

        assertElementPresent(
                By.id("view_page_title_text"),
                "Element title not found"
        );


    }


    private WebElement waitForElementPresent(By by, String error_message, long timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.elementToBeClickable(by));
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

    private WebElement waitForElementAndClear(By by, String error_message, long timeInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeInSeconds);
        element.clear();
        return element;
    }

    protected void swipeElementToLeft(By by, String error_message) {
        WebElement element = waitForElementPresent(
                by,
                error_message,
                7);
        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth() - 1;
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(new PointOption().withCoordinates(right_x, middle_y))
                .waitAction(waitOptions(ofSeconds(1)))
                .moveTo(new PointOption().withCoordinates(left_x, middle_y))
                .release()
                .perform();

    }

    protected void swipeToLeftByElement(WebElement element, String error_message) {     // Тест с коллекцией. Не удяляю, пригодится заметка для себя
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.withMessage(error_message + "\n");
        wait.until(ExpectedConditions.elementToBeClickable(element));

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth() - 1;
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(new PointOption().withCoordinates(right_x, middle_y))
                .waitAction(waitOptions(ofSeconds(1)))
                .moveTo(new PointOption().withCoordinates(left_x, middle_y))
                .release()
                .perform();

    }

    private String waitForElementAndGetArticleTitle(By by, String attribute, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    private void assertElementPresent(By by, String error_message) {

        try {
            waitForElementPresent(by, error_message, 0);
        } catch (Exception e) {
            throw new AssertionError(error_message);
        }

    }


}

