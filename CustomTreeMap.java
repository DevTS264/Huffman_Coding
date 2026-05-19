/**
 * A custom implementation of a TreeMap using a self-balancing AVL Tree.
 * This map stores key-value pairs sorted by their keys, which must implement
 * the {@link Comparable} interface.
 * <p>
 * This implementation provides guaranteed logarithmic time complexity O(log n)
 * for the insertion, retrieval, and removal operations by maintaining tree balance.
 *
 * @param <K> the type of keys maintained by this map, which must be comparable to itself or its supertypes
 * @param <V> the type of mapped values
 */
public class CustomTreeMap<K extends Comparable<? super K>, V>
{
    /**
     * The number of key-value mappings contained in this map.
     */
    private int size;

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size()
    {
        return size;
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * A node representing an element in the AVL Tree.
     * Each node holds a key, a value, references to its left and right children,
     * and its current height in the tree.
     */
    private class Node
    {
        /**
         * The key stored in this node.
         */
        private K key;
        
        /**
         * The value associated with the key in this node.
         */
        private V value;
        
        /**
         * The reference to the left child node.
         */
        private Node left;
        
        /**
         * The reference to the right child node.
         */
        private Node right;
        
        /**
         * The height of the node in the tree. Newly created nodes start with a height of 1.
         */
        private int height;

        /**
         * Constructs a new node with the specified key and value.
         * The left and right child pointers are initialized to {@code null}, and the height is set to 1.
         *
         * @param key   the key of the node
         * @param value the value of the node
         */
        public Node(K key, V value)
        {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }

        /**
         * Returns a string representation of this node in the form {@code {key: value}}.
         *
         * @return a string representation of the node
         */
        @Override
        public String toString()
        {
            return "{" + key + ": " + value + "}";
        }
    }

    /**
     * The root node of the AVL Tree.
     */
    private Node root;

    /**
     * Constructs an empty {@code CustomTreeMap}.
     */
    public CustomTreeMap()
    {
        root = null;
    }

    /**
     * Helper method to get the height of a given node.
     * If the node is {@code null}, returns 0.
     *
     * @param root the node whose height is to be calculated
     * @return the height of the node, or 0 if the node is {@code null}
     */
    private int getHeight(Node root)
    {
        if(root == null)
            return 0;
        return root.height;
    }

    /**
     * Returns the height of the tree.
     * An empty tree has a height of 0.
     *
     * @return the height of the tree
     */
    public int height()
    {
        return getHeight(root);
    }

    /**
     * Calculates the balance factor of a given node.
     * The balance factor is defined as the height of the right subtree minus the height of the left subtree.
     *
     * @param root the node to calculate the balance factor for
     * @return the balance factor of the node, or 0 if the node is {@code null}
     */
    private int getBalanceFactor(Node root)
    {
        if(root == null)
            return 0;
        return getHeight(root.right) - getHeight(root.left);
    }

    /**
     * Performs a left rotation on the given node to balance the tree.
     *
     * @param root the node around which the rotation is performed
     * @return the new root node of the rotated subtree
     */
    private Node leftRotation(Node root)
    {
        Node right = root.right;
        Node left = right.left;

        right.left = root;
        root.right = left;

        root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        right.height = Math.max(getHeight(right.left), getHeight(right.right)) + 1;
        return right;
    }

    /**
     * Performs a right rotation on the given node to balance the tree.
     *
     * @param root the node around which the rotation is performed
     * @return the new root node of the rotated subtree
     */
    private Node rightRotation(Node root)
    {
        Node left = root.left;
        Node right = left.right;

        left.right = root;
        root.left = right;

        root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        left.height = Math.max(getHeight(left.left), getHeight(left.right)) + 1;
        return left;
    }

    /**
     * Helper method to recursively insert a node into the AVL tree and perform balancing.
     * If the key already exists, updates the value associated with the key.
     *
     * @param root the current root node of the subtree
     * @param node the node to be inserted
     * @return the new root node of the balanced subtree
     */
    private Node insertNode(Node root, Node node)
    {
        if(root == null)
        {
            return node;
        }

        if(node.key.compareTo(root.key) > 0)
            root.right = insertNode(root.right, node);
        else if(node.key.compareTo(root.key) < 0)
            root.left = insertNode(root.left, node);
        else
        {
            root.value = node.value;
            return root;
        }

        root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;

        int bf = getBalanceFactor(root);

        if(bf < -1 && node.key.compareTo(root.left.key) < 0)
            return rightRotation(root);
        if(bf > 1 && node.key.compareTo(root.right.key) > 0)
            return leftRotation(root);
        if(bf < -1 && node.key.compareTo(root.left.key) > 0)
        {
            root.left = leftRotation(root.left);
            return rightRotation(root);
        }
        if(bf > 1 && node.key.compareTo(root.right.key) < 0)
        {
            root.right = rightRotation(root.right);
            return leftRotation(root);
        }

        return root;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @throws NullPointerException if the specified key is {@code null}
     */
    public void insert(K key, V value)
    {
        if(key == null)
            throw new NullPointerException();

        if(!containsKey(key))
            size++;
        Node node = new Node(key, value);
        root = insertNode(root, node);
    }

    /**
     * Helper method to recursively search for a node with the specified key in the tree.
     *
     * @param root the current root node of the subtree to search
     * @param key  the key to search for
     * @return the node with the specified key, or {@code null} if no such node exists
     */
    private Node searchNode(Node root, K key)
    {
        if(root == null)
            return null;

        if(key.compareTo(root.key) > 0)
            return searchNode(root.right, key);
        if(key.compareTo(root.key) < 0)
            return searchNode(root.left, key);
        return root;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key
     * @throws NullPointerException if the specified key is {@code null}
     */
    public V get(K key)
    {
        if(key == null)
            throw new NullPointerException();
        
        Node temp = searchNode(root, key);
        if(temp == null)
            return null;
        return temp.value;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key the key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     * @throws NullPointerException if the specified key is {@code null}
     */
    public boolean containsKey(K key)
    {
        if(key == null)
            throw new NullPointerException();

        return searchNode(root, key) != null;
    }

    /**
     * Helper method to locate the node with the minimum key starting from the given node.
     *
     * @param root the current root node of the subtree
     * @return the node with the minimum key in the subtree
     */
    private Node minKeyNode(Node root)
    {
        while(root.left != null)
            root = root.left;
        return root;
    }

    /**
     * Helper method to recursively delete a node with the specified key from the AVL tree and perform balancing.
     *
     * @param root the current root node of the subtree
     * @param key  the key of the node to be deleted
     * @return the new root node of the balanced subtree
     */
    private Node deleteNode(Node root, K key)
    {
        if(root == null)
            return root;

        if(key.compareTo(root.key) < 0)
            root.left = deleteNode(root.left, key);
        else if(key.compareTo(root.key) > 0)
            root.right = deleteNode(root.right, key);
        else
        {
            if(root.left == null)
                return root.right;
            if(root.right == null)
                return root.left;

            Node temp = minKeyNode(root.right);
            root.key = temp.key;
            root.value = temp.value;
            root.right = deleteNode(root.right, temp.key);
        }

        root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;

        int bf = getBalanceFactor(root);

        if(bf < -1 && getBalanceFactor(root.left) <= 0)
            return rightRotation(root);
        if(bf > 1 && getBalanceFactor(root.right) >= 0)
            return leftRotation(root);
        if(bf < -1 && getBalanceFactor(root.left) > 0)
        {
            root.left = leftRotation(root.left);
            return rightRotation(root);
        }
        if(bf > 1 && getBalanceFactor(root.right) < 0)
        {
            root.right = rightRotation(root.right);
            return leftRotation(root);
        }

        return root;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key the key whose mapping is to be removed from the map
     */
    public void remove(K key)
    {
        if(containsKey(key))
        {
            size--;
            root = deleteNode(root, key);
        }
    }

    /**
     * Helper method to perform an inorder traversal of the tree starting from the given node,
     * printing each node's string representation.
     *
     * @param root the root node of the subtree to traverse
     */
    private void inorderTraversal(Node root)
    {
        if(root == null)
            return;

        inorderTraversal(root.left);
        System.out.print(root + " ");
        inorderTraversal(root.right);
    }

    /**
     * Prints the contents of this map to the standard output in sorted order using inorder traversal.
     */
    public void print()
    {
        inorderTraversal(root);
        System.out.println();
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear()
    {
        root = null;
        size = 0;
    }
}