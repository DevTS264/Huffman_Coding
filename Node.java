public class Node<K, V extends Comparable<? super V>> implements Comparable<Node<K, V>>
{
    K key;
    V freq;
    Node<K, V> left;
    Node<K, V> right;

    public Node(K key, V freq)
    {
        this.key = key;
        this.freq = freq;
        this.left = null;
        this.right = null;
    }

    @Override
    public int compareTo(Node<K, V> node)
    {
        return this.freq.compareTo(node.freq);
    }
}
