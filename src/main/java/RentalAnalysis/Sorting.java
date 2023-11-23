package RentalAnalysis;

import java.util.*;

/**

* This class implements web page ranking using merge sort.

*/

public class Sorting {
    /**
     * Sorts web pages based on their occurrence using merge sort.
     *
     */
    public static void sortWebPagesByOccurrence(Hashtable<?, Integer> t, int occur) {
        // Organize the list of hashtable entries by sorting it.
        ArrayList<Map.Entry<?, Integer>> entryList = new ArrayList<>(t.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<?, Integer>>() {
            public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        //Reverse the sorted list to get it in descending order of occurrence
        Collections.reverse(entryList);
        // Display the sorted web page rankings if the 'occur' flag is not zero
        if (occur != 0) {
            System.out.println("\n------------------------------Web Page Ranking------------------------------\n");
            int k = 5; // number of top results to be shown
            int l = 0;
            System.out.printf("%-10s %s\n", "Sr. No.", "Name and Occurrence");
            System.out.println("-------------------------------------------------");
            while (l < entryList.size() && k > 0) {
                System.out.printf("\n%-10d| %s\n", l + 1, entryList.get(l));
                l++;
                k--;
            }
            System.out.println("\n------------------------------------------------------------\n");
        }
    }
}
