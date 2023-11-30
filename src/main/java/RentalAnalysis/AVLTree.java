package RentalAnalysis;

class AVLNode {
    String data;
    int height;
    AVLNode left, right;

    AVLNode(String data) {
        this.data = data;
        this.height = 1;
    }
}

class AVLTree {
    AVLNode root;

    int height(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    int balanceFactor(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    AVLNode insert(AVLNode node, String data) {
        if (node == null) {
            return new AVLNode(data);
        }

        if (data.compareToIgnoreCase(node.data) < 0) {
            node.left = insert(node.left, data);
        } else if (data.compareToIgnoreCase(node.data) > 0) {
            node.right = insert(node.right, data);
        } else {
            // Duplicate data, no action needed
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = balanceFactor(node);

        // Left Left Case
        if (balance > 1 && data.compareToIgnoreCase(node.left.data) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && data.compareToIgnoreCase(node.right.data) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && data.compareToIgnoreCase(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && data.compareToIgnoreCase(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void preOrder(AVLNode node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    String autocomplete(String prefix) {
        StringBuilder result = new StringBuilder();
        autocomplete(root, prefix, result);
        return result.length() > 0 ? result.toString() : "No complete word can be found.";
    }

    private void autocomplete(AVLNode node, String prefix, StringBuilder result) {
        if (node != null) {
            if (node.data.toLowerCase().startsWith(prefix.toLowerCase())) {
                result.append(node.data).append(" ");
            }
            if (prefix.compareToIgnoreCase(node.data) < 0) {
                autocomplete(node.left, prefix, result);
            } else if (prefix.compareToIgnoreCase(node.data) > 0) {
                autocomplete(node.right, prefix, result);
            } else {
                autocomplete(node.left, prefix, result);
                autocomplete(node.right, prefix, result);
            }
        }
    }
}
