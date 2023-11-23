package RentalAnalysis;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumTest {

    public static void main(String[] args) {
        // Setup WebDriverManager for Chrome
        WebDriverManager.chromedriver().setup();

        // Create ChromeOptions object to maximize the window
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        // Initialize ChromeDriver with options
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to eBay
            driver.get("https://www.ebay.com");

            // Find the search input field by its XPath
            WebElement searchField = driver.findElement(By.xpath("//input[@name='_nkw']"));

            // Type "mobile" into the search field
            searchField.sendKeys("mobile");

            // Submit the form
            searchField.submit();

            // Sleep for a few seconds to see the result (you might want to use WebDriverWait in a real project)
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the browser window
            driver.quit();
        }
    }
}
