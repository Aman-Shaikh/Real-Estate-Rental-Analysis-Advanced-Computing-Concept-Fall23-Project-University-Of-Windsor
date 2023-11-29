package RentalAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

class WCTrieNode {
    Map<Character, WCTrieNode> children;
    boolean isEndOfWord;

    WCTrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}

class Trie {
    WCTrieNode root;

    Trie() {
        root = new WCTrieNode();
    }

    void insert(String word) {
        WCTrieNode current = root;
        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new WCTrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
    }

    boolean search(String prefix) {
        WCTrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return false;
            }
            current = current.children.get(ch);
        }
        return true;
    }

    void autoComplete(String prefix) {
        WCTrieNode node = getNodeForPrefix(prefix.toLowerCase());
        if (node != null) {
            System.out.println("Word completion for prefix '" + prefix + "':");
            autoComplete(prefix.toLowerCase(), node, "");
        } else {
            System.out.println("No matching words for the prefix '" + prefix + "'.");
        }
    }

    private WCTrieNode getNodeForPrefix(String prefix) {
        WCTrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return null;
            }
            current = current.children.get(ch);
        }
        return current;
    }

    private void autoComplete(String prefix, WCTrieNode node, String currentWord) {
        if (node.isEndOfWord) {
            System.out.println(prefix + currentWord);
        }

        for (Map.Entry<Character, WCTrieNode> entry : node.children.entrySet()) {
            autoComplete(prefix, entry.getValue(), currentWord + entry.getKey());
        }
    }
}

public class WordCompletion {
    public static void main(String[] args) {
        Trie trie = new Trie();

        // Load all Canadian cities from the file into the trie
        loadCitiesFromFile("/canadiancities.txt", trie);

        // Get input from the console
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter a prefix: ");
            String prefix = br.readLine();
            trie.autoComplete(prefix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCitiesFromFile(String filename, Trie trie) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Convert to lowercase for case-insensitive matching
                trie.insert(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
