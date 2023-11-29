package RentalAnalysis;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
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
import java.util.*;

public class Crawler {

	private static WebElement searchBox;

	public static void crawlZolo(String urlToCrawl,String city,String type,String beds) {

		// Use WebDriverManager to dynamically set up the WebDriver binary
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		 options.addArguments("--start-maximized");

		try {

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
				Thread.sleep(1500);
				searchBox.sendKeys(Keys.ENTER);

				// Wait for some time to let the search results load
				Thread.sleep(2000);

				// Select the type of house
				selectType(driver, "//*[@id=\"home_search_top\"]/ul/li[4]", type); // Replace with the actual name or ID of the beds dropdown


				// Wait for some time to let the search results load
				Thread.sleep(2000);


				// Select the number of beds//*[@id="home_search_top"]/ul/li[4]/div/ul/li[1]/label/span
				selectBeds(driver, "//*[@id=\"home_search_top\"]/ul/li[3]", beds); // Replace with the actual name or ID of the beds dropdown

				// Crawl all listings and store data in text files
				 crawlListingsAndStoreDataZolo(driver);

				System.out.println("\n**********************************************************************************************");
				System.out.println("**********************************************************************************************");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the browser window
//			driver.quit();
		}
	}


	public static void crawlRentals(String urlToCrawl,String city,String type,String beds) {
		// Use WebDriverManager to dynamically set up the WebDriver binary
		Scanner scan = new Scanner(System.in);

		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");

		try {
				// Initialize ChromeDriver with options
				WebDriver driver = new ChromeDriver(options);
				// Open Chrome and navigate to the specified URL
				driver.get(urlToCrawl);

//				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/div/div/div/div[2]/div/div")).click();
				// Locate the auto-suggestion input element
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2000));
				WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[1]/div[1]/div/div/div/div[2]/div/div/div/input")));

				Thread.sleep(2000);

				// Enter the city into the input
//				searchBox.click();

				Thread.sleep(1000);

				searchBox.sendKeys(city);
				Thread.sleep(1500);
				searchBox.sendKeys(Keys.ARROW_DOWN);
				// Wait for the auto-suggestion dropdown to appear
				Thread.sleep(1500);
				searchBox.sendKeys(Keys.ENTER);

				// Wait for some time to let the search results load
				Thread.sleep(2000);

				// Select the type of house
				selectTypeRentals(driver, "//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[2]/span/button", type); // Replace with the actual name or ID of the beds dropdown


				// Wait for some time to let the search results load
				Thread.sleep(2000);

				// Select the type of house

				// Select the number of beds//*[@id="home_search_top"]/ul/li[4]/div/ul/li[1]/label/span
				selectBedsRentals(driver, "//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[3]/span/button", beds); // Replace with the actual name or ID of the beds dropdown


				// Crawl all listings and store data in text files
				crawlListingsAndStoreDataRentals(driver);

				System.out.println("\n**********************************************************************************************");
				System.out.println("**********************************************************************************************");

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
	private static void selectType(WebDriver driver, String dropDownXPath, String value) {
		WebElement dropdown = driver.findElement(By.xpath(dropDownXPath+"/a")); // Replace with the actual ID of the dropdown
		dropdown.click();

		// Wait for some time to let the dropdown options load
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		// Split the comma-separated values
		String[] typesToSelect = value.split(",");
		for (String type : typesToSelect) {

			if(type.equalsIgnoreCase("house")){
				driver.findElement(By.xpath("//*[@id=\"home_search_top\"]/ul/li[4]/div/ul/li[1]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("condo")){
				driver.findElement(By.xpath("//*[@id=\"home_search_top\"]/ul/li[4]/div/ul/li[2]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("townhouse")||type.equalsIgnoreCase("apartment")){
				driver.findElement(By.xpath("//*[@id=\"home_search_top\"]/ul/li[4]/div/ul/li[3]")).click();
			}
		}
	}

	private static void selectTypeRentals(WebDriver driver, String dropDownXPath, String value) {
		WebElement dropdown = driver.findElement(By.xpath(dropDownXPath)); // Replace with the actual ID of the dropdown
		dropdown.click();

		// Wait for some time to let the dropdown options load
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		// Split the comma-separated values
		String[] typesToSelect = value.split(",");
		for (String type : typesToSelect) {

			if(type.equalsIgnoreCase("house")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[2]/span/span/div/div/div/div/div[1]/div/div[9]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("condo")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[2]/span/span/div/div/div/div/div[1]/div/div[8]/label")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("townhouse")||type.equalsIgnoreCase("apartment")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[2]/span/span/div/div/div/div/div[1]/div/div[1]")).click();
			}
		}
	}

	private static void selectBedsRentals(WebDriver driver, String dropDownXPath, String value) {
		WebElement dropdown = driver.findElement(By.xpath(dropDownXPath)); // Replace with the actual ID of the dropdown
		dropdown.click();

		// Wait for some time to let the dropdown options load
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		// Split the comma-separated values
		String[] typesToSelect = value.split(",");
		for (String type : typesToSelect) {

			if(type.equalsIgnoreCase("0")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[3]/span/span/div/div/div/div/div[1]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("1")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[3]/span/span/div/div/div/div/div[2]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("2")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[3]/span/span/div/div/div/div/div[3]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("3")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[3]/span/span/div/div/div/div/div[4]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(type.equalsIgnoreCase("4")){
				driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/nav/div/div[2]/div[3]/span/span/div/div/div/div/div[5]")).click();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void crawlListingsAndStoreDataZolo(WebDriver driver) {
		// Assume each listing is represented by a WebElement with a class "listing-item"

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<WebElement> listings = driver.findElements(By.xpath("//*[@id=\"gallery\"]/div/article"));

		// Check if there are no listings
		if (listings.isEmpty()) {
			System.out.println("No listings found on the page.");
			return;
		}

		// Create a StringBuilder to store all the data as a string
		StringBuilder allDataStringBuilder = new StringBuilder();

		// Loop through each listing
		for (int i = 0; i < listings.size(); i++) {
			WebElement listing = listings.get(i);

			try {
				// Extract data from the listing as strings
				String numberOfBeds = listing.findElement(By.xpath(".//ul/li[2]")).getText();
				String numberOfBaths = listing.findElement(By.xpath(".//ul/li[3]")).getText();
				String street = listing.findElement(By.xpath(".//div[1]/a/h3/span[1]")).getText();
				String city = listing.findElement(By.xpath(".//div[1]/a/h3/span[2]")).getText();
				String province = listing.findElement(By.xpath(".//div[1]/a/h3/span[3]")).getText();



				// Handle the case where price may not be present
				String price = "";
				try {
					price = listing.findElement(By.xpath(".//ul/li[1]/span[2]")).getText();
				} catch (Exception ex) {
					System.out.println("Price not found for listing " + i);
					continue;
				}

				// Append data to the StringBuilder
				allDataStringBuilder.append("City: ").append(city).append("\n");
				allDataStringBuilder.append("Number of Beds: ").append(numberOfBeds).append("\n");
				allDataStringBuilder.append("Number of Baths: ").append(numberOfBaths).append("\n");
				allDataStringBuilder.append("Street: ").append(street).append("\n");
				allDataStringBuilder.append("City: ").append(city).append("\n");
				allDataStringBuilder.append("Province: ").append(province).append("\n");
				allDataStringBuilder.append("Price: ").append(price).append("\n");

				allDataStringBuilder.append("\n");
			} catch (Exception e) {
				// Handle any other elements not found exception for this listing
				System.out.println("Error extracting data for listing " + i + ": " + e.getMessage());
				continue;
			}
		}

		// Create a directory to store text files
		File directory = new File("assets/textFiles");
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Create a text file for all listings
		File txtFile = new File("assets/textFiles/zolo.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
			// Write all data to the text file
			writer.write(allDataStringBuilder.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void crawlListingsAndStoreDataRentals(WebDriver driver) {
		// Assume each listing is represented by a WebElement with a class "listing-item"

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<WebElement> listings = driver.findElements(By.xpath("//*[@id=\"app\"]/div[1]/div/div[2]/div/div[2]/div/div/div"));

		// Check if there are no listings
		if (listings.isEmpty()) {
			System.out.println("No listings found on the page.");
			return;
		}

		// Create a StringBuilder to store all the data as a string
		StringBuilder allDataStringBuilder = new StringBuilder();

		System.out.println(listings.size());
		// Loop through each listing
		for (int i = 0; i < listings.size(); i++) {
			WebElement listing = listings.get(i);

			try {
				// Extract data from the listing as strings
				String houseType = listing.findElement(By.xpath(".//div/div[4]//p[@class=\"listing-card__type\"]")).getText();

																	//				div/div[4]/div[4]/ul/li[listing-card__main-features--active]
				String numberOfBeds = listing.findElement(By.xpath(".//div/div[4]//ul/li[@class=\"listing-card__main-features--active\"]")).getText();
				String numberOfBaths = listing.findElement(By.xpath(".//div/div[4]//ul/li[2]")).getText();
				String address = listing.findElement(By.xpath(".//div//h2")).getText();
				String price =listing.findElement(By.xpath(".//div/div[4]/p")).getText();


				// Append data to the StringBuilder
				allDataStringBuilder.append("Type of House: ").append(houseType).append("\n");
				allDataStringBuilder.append("Number of Beds: ").append(numberOfBeds).append("\n");
				allDataStringBuilder.append("Number of Baths: ").append(numberOfBaths).append("\n");
				allDataStringBuilder.append("Address: ").append(address).append("\n");
				allDataStringBuilder.append("Price: ").append(price).append("\n");

				allDataStringBuilder.append("\n");
			} catch (Exception e) {
				// Handle any other elements not found exception for this listing
				System.out.println("Error extracting data for listing " + i + ": " + e.getMessage());
				continue;
			}
		}

		// Create a directory to store text files
		File directory = new File("assets/textFiles");
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Create a text file for all listings
		File txtFile = new File("assets/textFiles/rentals.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
			// Write all data to the text file
			writer.write(allDataStringBuilder.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
