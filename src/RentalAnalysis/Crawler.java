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

			visitedLinks.add(url);

			String pattern = "^((https?://)|(www\\.))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			System.out.println("\nParsing: " + pattern);

			String tmpURL = "";
			for (Element anchorTags : pageContent.select("a[href]")) {
				tmpURL = anchorTags.attr("abs:href");
				System.out.println(tmpURL);

				if (!Pattern.matches(pattern, tmpURL)) {
					System.out.println("\nFound URL: " + tmpURL + " => unknown");
				} else if (visitedLinks.contains(tmpURL)) {
					System.out.println("\nFound URL: " + tmpURL + " => ignored because visited");
				} else {
					visitedLinks.add(tmpURL);
					System.out.println("\nFound URL: " + tmpURL + " => added to crawl list");
				}
				tmpURL = "";
			}
		} catch (org.jsoup.HttpStatusException e) {
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
	public static void crawlMain(String url) {
		crawlURL(url);
		extractTextFromHTML();
	}

//	public static void main(String[] args) {
//		System.out.println(System.getProperty("user.dir") + Constant.FILE_PATH);
//		crawlMain("https://stackoverflow.com/questions/11952804/explanation-of-string-args-and-static-in-public-static-void-mainstring-a");
//	}

}
