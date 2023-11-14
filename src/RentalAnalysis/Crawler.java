package RentalAnalysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

public class Crawler {
	static HashSet<String> visitedLinks = new HashSet<String>();

	public static void crawlURL(String url) {
		try {
			Document pageContent = Jsoup.connect(url).get();

			// add url only after HTML can be fetched
			visitedLinks.add(url);

			// regex for web url matching
			String pattern = "^((https?://)|(www\\.))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			System.out.println("\nParsing: " + pattern);

			String tmpURL = "";
			// iterate over all anchor tags with href attribute using a css selector
			for (Element anchorTags : pageContent.select("a[href]")) {
				// get the value of href attribute from anchor tag
				tmpURL = anchorTags.attr("abs:href");
				System.out.println(tmpURL);

				// anchor tage with no matching url pattern
				if (!Pattern.matches(pattern, tmpURL)) {
					System.out.println("\nFound URL: " + tmpURL + " => unknown");
				}
				// already visited links
				else if (visitedLinks.contains(tmpURL)) {
					System.out.println("\nFound URL: " + tmpURL + " => ignored because visited");
				}
				// add valid links to crawl
				else {
					visitedLinks.add(tmpURL);
					System.out.println("\nFound URL: " + tmpURL + " => added to crawl list");
				}
				tmpURL = "";
			}
		}
		// catch exception when jsoup can not connect to website
		catch (org.jsoup.HttpStatusException e) {
			System.out.println("\nURL: " + url + " => blocked, not crawled");
		} catch (IOException e) {
			System.out.println("\nURL: " + url + " => I/O error, not crawled");
		}
	}

	public static void extractTextFromHTML() {
		try {
			String txt, currentURL;
			String filePath = System.getProperty("user.dir") + Constant.FILE_PATH;
			Iterator<String> itr = visitedLinks.iterator();
			while (itr.hasNext()) {
				currentURL = itr.next();
				try {
					Document document = Jsoup.connect(currentURL).get();
					txt = document.text();
					String docTitle = document.title().replaceAll("[^a-zA-Z0-9_-]", "") + ".txt";
					BufferedWriter out = new BufferedWriter(new FileWriter(filePath + docTitle, true));
					out.write(currentURL + " " + txt);
					out.close();
				} catch (org.jsoup.HttpStatusException e) {
					System.out.println("\nURL from page: " + currentURL + " => blocked, not crawled");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void spider(String url) {
		crawlURL(url);
		extractTextFromHTML();
	}

//	public static void main(String[] args) {
//		System.out.println(System.getProperty("user.dir") + Constant.FILE_PATH);
//		spider("https://stackoverflow.com/questions/11952804/explanation-of-string-args-and-static-in-public-static-void-mainstring-a");
//	}

}
