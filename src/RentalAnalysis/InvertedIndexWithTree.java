package RentalAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class TrieNode {
    HashMap<Character, TrieNode> children;
    ArrayList<String> documents;

    public TrieNode() {
        children = new HashMap<>();
        documents = new ArrayList<>();
    }
}

public class InvertedIndexWithTree {
    private TrieNode root;

    public InvertedIndexWithTree() {
        root = new TrieNode();
    }

    public void buildIndex(File[] files) {
        for (File file : files) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    insertWord(word, file.getName());
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertWord(String word, String document) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.documents.add(document);
    }

    public ArrayList<String> search(String query) {
        TrieNode current = root;
        for (char ch : query.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return new ArrayList<>(); // Return an empty list if the word is not found
            }
            current = current.children.get(ch);
        }
        return current.documents;
    }
}
