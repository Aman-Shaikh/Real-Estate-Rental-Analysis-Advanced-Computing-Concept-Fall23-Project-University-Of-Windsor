package RentalAnalysis;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class Crawler {

	private static WebElement searchBox;

	public static void crawlMain(String urlToCrawl) {
		// Use WebDriverManager to dynamically set up the WebDriver binary
		Scanner scan = new Scanner(System.in);
		String choice = "y";
		
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		 options.addArguments("--start-maximized");




		try {


			do {
				System.out.println("\nSpecify the details of your search (e.g. city, house/apartment, number of bedrooms, etc): ");
				System.out.print("City : ");
				String city = scan.nextLine();
				System.out.print("\nHouse/Apartment/Condo (if you want to add multiple enter it in comma separated manner): ");
				String type = scan.nextLine();
				System.out.print("\nNumber of bedrooms : ");
				String beds = scan.nextLine();

				// Initialize ChromeDriver with options
				WebDriver driver = new ChromeDriver(options);
				// Open Chrome and navigate to the specified URL
				driver.get(urlToCrawl);

				// Locate the auto-suggestion input element
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5000));
				WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("sarea")));

				//searchBox = driver.findElement(By.id("sarea"));

				// Enter the city into the input
				searchBox.click();

				Thread.sleep(1000);

				for(int i=0;i<50;i++){
					searchBox.sendKeys(Keys.BACK_SPACE);
				}
				searchBox.sendKeys(city);
				// Wait for the auto-suggestion dropdown to appear
				Thread.sleep(500);
				searchBox.sendKeys(Keys.ENTER);

				// Wait for some time to let the search results load
				Thread.sleep(2000);

				// Select the type of house
//				selectCheckboxInDropdown(driver, "typeDropdown", type); // Replace with the actual name or ID of the type dropdown



				// Select the number of beds
				selectBeds(driver, "//*[@id=\"home_search_top\"]/ul/li[3]", beds); // Replace with the actual name or ID of the beds dropdown


				// Crawl all listings and store data in text files
				// crawlListingsAndStoreData(driver);


				System.out.print("\n\nDo you want to continue y/n: ");
				choice = scan.nextLine();

				System.out.println("\n**********************************************************************************************");
				System.out.println("**********************************************************************************************");
				
				
			}while (choice.equals("y"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the browser window
//			driver.quit();
		}
	}

	private static void selectBeds(WebDriver driver, String dropDownXPath, String value) {
		WebElement dropdown = driver.findElement(By.xpath(dropDownXPath+"/a")); // Replace with the actual ID of the dropdown
		dropdown.click();

		// Wait for some time to let the dropdown options load
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement bedOption = driver.findElement(By.xpath(dropDownXPath+"/div/div[2]/ul//a[contains(text(),\""+ value +"+\")"+"]"));
		bedOption.click();
	}

	private static void crawlListingsAndStoreData(WebDriver driver) {
		// Assume each listing is represented by a WebElement with a class "listing-item"
		List<WebElement> listings = driver.findElements(By.className("listing-item"));

		// Check if there are no listings
		if (listings.isEmpty()) {
			System.out.println("No listings found on the page.");
			return;
		}

		// Create a directory to store text files
		File directory = new File("assets/txtfiles");
		if (!directory.exists()) {
			directory.mkdirs();
		}

		for (int i = 0; i < listings.size(); i++) {
			WebElement listing = listings.get(i);

			// Extract data from the listing
			String listingTitle = safeGetText(listing.findElement(By.className("title")));
			String listingDescription = safeGetText(listing.findElement(By.className("description")));
			// Add more fields as needed...

			// Create a text file for each listing
			File txtFile = new File("assets/txtfiles/listing_" + i + ".txt");

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
				// Write data to the text file
				writer.write("Listing Title: " + listingTitle);
				writer.newLine();
				writer.write("Listing Description: " + listingDescription);
				// Add more lines for additional fields...
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String safeGetText(WebElement element) {
		try {
			return element.getText();
		} catch (Exception e) {
			System.out.println("Error getting text from element: " + e.getMessage());
			return "";
		}
	}
}
