package RentalAnalysis;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchWord {

	/**
	 * Searches word in the given file using the Booyer Moore algorithm. 
	 * Returns the number of times the word has been found in the file.
	 * 
	 * @param word     The word to search
     * @param filePath The file path
     * @return The number of occurrences of the word in the file
     * @throws IOException if an I/O error occurs while reading the file
     */
	public static int wordSearch(String word, File filePath) throws IOException {
		int count = 0;
		StringBuilder data = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String str = null;

			while ((str = reader.readLine()) != null) {
				data.append(str);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}
		// Find position of the word
		String text = data.toString();

		int offset = 0, loc =0;

		while (loc <= text.length()) {
			offset = RentalAnalysis.search1(word, text.substring(loc));
			if ((offset + loc) < text.length()) {
				count++;
//				System.out.println("\n" + word + " is at position " + (offset + loc) + "."); // printing the position of the word
			}
			loc += offset + word.length();
		}
		
		// If the word is found, print the file name where it is found
		if (count >= 0) {
			System.out.println("-------------------------------------------------");
			System.out.println("\nWord found in " + filePath.getName() + " " +count+ " number of times");
			System.out.println("-------------------------------------------------");
		}
		return count;
	}

	// Finds strings with similar patterns and calls edit distance on those strings.
	public static void findData(File sourceFile, int fileNumber, Matcher matcher, String p1)
			throws FileNotFoundException, ArrayIndexOutOfBoundsException {
		EditDistance.findWord(sourceFile, fileNumber, matcher, p1);
	}

	/**
	 * Finds all the words with an edit distance of 3 to the provided word.
	 * 
	 * @param word The word to find similar words for
	 */
	public static void altWord(String word) {
		String str = " ";
		String pattern1 = "[0-9a-zA-Z]+";

		// Compile the pattern and create a matcher for the provided word
		Pattern r3 = Pattern.compile(pattern1);
		Matcher m3 = r3.matcher(str);
		int fileNumber = 0;

		// Get the list of files in the directory to search for similar words	
		File dir = new File(System.getProperty("user.dir") + Constant.FILE_PATH);
		File[] fileArray = dir.listFiles();
		
		// Iterate through the files and find data based on edit distance
		for (File file : fileArray) {
			try {
				findData(file, fileNumber, m3, word);
				fileNumber++;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		Integer allowedDistance = 3;
		boolean matchFound = false; 

		System.out.println("Did you mean? ");
		int i = 0;
		// Display the list of words with an edit distance of 1 to the provided word
		for (String key : RentalAnalysis.numbers.keySet()) {
			if (allowedDistance.equals(RentalAnalysis.numbers.get(key))) {
				i++;
				System.out.print("(" + i + ") " + key + "\n");
				matchFound = true;
			}
		}
		if (!matchFound)
			System.out.println("Entered word cannot be resolved.");
	}
}