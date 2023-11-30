package RentalAnalysis;
import java.util.ArrayList;
import java.util.Hashtable;

class TrieNode {
    Hashtable<Character, TrieNode> children;
    ArrayList<Integer> fileOccurrences;

    public TrieNode() {
        this.children = new Hashtable<>();
        this.fileOccurrences = new ArrayList<>();
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word, int fileNumber) {
        TrieNode node = root;
        word = word.toLowerCase();

        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
            node.fileOccurrences.add(fileNumber);
        }
    }

    public ArrayList<Integer> search(String word) {
        TrieNode node = root;
        word = word.toLowerCase();

        for (char ch : word.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new ArrayList<>(); // Character not found
            }
            node = node.children.get(ch);
        }

        return node.fileOccurrences;
    }

    public void reset() {
        this.root = new TrieNode();
    }
}
