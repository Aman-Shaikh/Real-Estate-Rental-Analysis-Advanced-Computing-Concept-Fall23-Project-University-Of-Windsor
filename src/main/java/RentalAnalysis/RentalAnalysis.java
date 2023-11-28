package RentalAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RentalAnalysis {
    static ArrayList<String> key = new ArrayList<String>();
    static Hashtable<String, Integer> numbers = new Hashtable<String, Integer>();
    static int n = 1;
    static Scanner sc = new Scanner(System.in);
    static int R;
    static int[] right;

    private static InvertedIndexWithTree invertedIndex = new InvertedIndexWithTree();

    public RentalAnalysis() {
        System.out.println("***************************************************");
        System.out.println("****************RENTAL ANALYSIS******************");
        System.out.println("***************************************************");
        System.out.println("**************TEAM MEMBERS***************");
        System.out.println("\n		Aman Shaikh					   ");
        System.out.println("		Abubakar  				   ");
        System.out.println("		Naman Goyal		    		   ");
        System.out.println("		Mohammad Farhan		    		   ");
        System.out.println("\n***************************************************");
    }

    // Finding pattern using Boyer Moore method.
    public static int search1(String pat, String txt) {
        BoyerMoore b = new BoyerMoore(pat);
        int offset = b.search(txt);
        return offset;
    }

    public static void RentalAnalysis() {
        File dir = new File(System.getProperty("user.dir") + Constant.FILE_PATH);

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                f.delete();
            }
        }


        RentalAnalysis w = new RentalAnalysis();

//        System.out.println("\n\n Enter the URL you want to crawl");
//        String urlToCrawl = scan.nextLine();
//
//        String pattern = "^((https?://)|(www\\.))[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
//
//        while(!Pattern.matches(pattern, urlToCrawl)){
//            System.out.println("\n\n Please enter a valid url (it should start with either https: or www.)");
//
//            System.out.println("\n\n Enter the URL you want to crawl");
//            urlToCrawl = scan.nextLine();
//
//        }


        Scanner scan = new Scanner(System.in);
        Hashtable<String, Integer> occurrs = new Hashtable<String, Integer>();
        String choice = "y";

        String word;
        Crawler.crawlZolo("https://zolo.ca/");
        Crawler.crawlRentals("https://rentals.ca/");

        System.out.print("Enter word to search in crawled data : ");
        word = scan.nextLine();




        do {
            long fileNumber = 0;
            int occur = 0;
            int pg = 0;

            try {
                File[] fileArray = dir.listFiles();
                invertedIndex.buildIndex(fileArray);

                for (int i = 0; i < fileArray.length; i++) {
                    occur = SearchWord.wordSearch(word, fileArray[i]);
                    occurrs.put(fileArray[i].getName(), occur);
                    if (occur != 0)
                        pg++;
                    fileNumber++;
                }

                if (pg == 0) {
                    System.out.println("\n\n\n\n\n\n---------------------------------------------------");
                    System.out.println("Given word not found!!");
                    System.out.println("Searching for similar words.....");
                    SearchWord.altWord(word);
                } else {
                    RentalAnalysis.hashing(occurrs, pg);
                    Sorting.sortWebPagesByOccurrence(occurrs, pg);
                }

                System.out.println("\n\n Do you want to continue(y/n)??");
                choice = scan.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (choice.equals("y"));

        System.out.println("\n***************************************************\n");
        System.out.println("	THANK YOU FOR USING OUR RENTAL ANALYSIS PROGRAM       ");
        System.out.println("\n***************************************************\n");
    }

    public static void main(String[] args) {
        RentalAnalysis.RentalAnalysis();
    }

    static void hashing(Hashtable<String, Integer> hashtable, Integer page) {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("| %10s | %20s", "VALUE", "KEY");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        hashtable.forEach(
                (k, v) -> {
                    System.out.format("| %10s | %20s ", v, k);
                    System.out.println();
                });
        System.out.println("-----------------------------------------------------------------------------");
    }
}
