package RentalAnalysis;
import java.util.ArrayList;

class AVLNodeII {
    String key;
    ArrayList<Integer> occurrences;
    int height;
    AVLNodeII left, right;

    AVLNodeII(String key, int lineNumber) {
        this.key = key;
        this.occurrences = new ArrayList<>();
        this.occurrences.add(lineNumber);
        this.height = 1;
        this.left = this.right = null;
    }
}

class AVLTreeII {
    AVLNodeII root;

    int height(AVLNodeII node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    int getBalance(AVLNodeII node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    AVLNodeII rightRotate(AVLNodeII y) {
        AVLNodeII x = y.left;
        AVLNodeII T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNodeII leftRotate(AVLNodeII x) {
        AVLNodeII y = x.right;
        AVLNodeII T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    AVLNodeII insert(AVLNodeII node, String key, int lineNumber) {
        if (node == null)
            return new AVLNodeII(key, lineNumber);

        if (key.compareToIgnoreCase(node.key) < 0)
            node.left = insert(node.left, key, lineNumber);
        else if (key.compareToIgnoreCase(node.key) > 0)
            node.right = insert(node.right, key, lineNumber);
        else
            node.occurrences.add(lineNumber);

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key.compareToIgnoreCase(node.left.key) < 0)
            return rightRotate(node);

        if (balance < -1 && key.compareToIgnoreCase(node.right.key) > 0)
            return leftRotate(node);

        if (balance > 1 && key.compareToIgnoreCase(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key.compareToIgnoreCase(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void reset() {
        root = null;
    }

    void buildIndex(String key, int lineNumber) {
        key = key.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        root = insert(root, key, lineNumber);
    }

    ArrayList<Integer> search(String key) {
        return search(root, key);
    }

    ArrayList<Integer> search(AVLNodeII root, String key) {
        ArrayList<Integer> occurrences = new ArrayList<>();
        if (root == null)
            return occurrences;

        int cmp = key.compareToIgnoreCase(root.key);
        if (cmp == 0) {
            return root.occurrences;
        } else if (cmp < 0) {
            occurrences = search(root.left, key);
        } else {
            occurrences = search(root.right, key);
        }

        return occurrences;
    }
}