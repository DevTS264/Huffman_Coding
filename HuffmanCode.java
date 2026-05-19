import java.util.ArrayList;

/**
 * Represents the Huffman Coding algorithm used to encode text data.
 */
public class HuffmanCode 
{
    private String fileText;
    private Heap<Node<Character, Integer>> priorityQueue = new Heap<>();
    private StringBuilder code;

    /**
     * Constructs a new {@code HuffmanCode} with the given text.
     *
     * @param fileText the text to encode
     */
    public HuffmanCode(String fileText)
    {
        this.fileText = fileText;
    }

    /**
     * Recursively traverses the Huffman Tree to generate the codes for each character.
     *
     * @param root  the current node in the Huffman Tree
     * @param codes an array storing the current path in the tree
     * @param top   the current depth in the tree
     */
    private void printCodes(Node<Character, Integer> root, int[] codes, int top)
    {
        if(root.left != null)
        {
            codes[top] = 0;
            printCodes(root.left, codes, top + 1);
        }

        if(root.right != null)
        {
            codes[top] = 1;
            printCodes(root.right, codes, top + 1);
        }

        if(root.left == null && root.right == null)
        {
            code.append((int)root.key + ": ");
            for(int i = 0; i < top; i++)
                code.append(String.valueOf(codes[i]));
            code.append("\n");
        }
    }

    /**
     * Builds the frequency map and generates the Huffman Tree.
     */
    public void encode()
    {
        ArrayList<Character> charList = new ArrayList<>();
        ArrayList<Integer> freqList = new ArrayList<>();

        for(int i = 0; i < fileText.length(); i++)
        {
            if(charList.contains(fileText.charAt(i)))
            {
                int index = charList.indexOf(fileText.charAt(i));
                freqList.set(index, freqList.get(index) + 1);
            }
            else
            {
                charList.add(fileText.charAt(i));
                freqList.add(1);
            }
        }

        for(int i = 0; i < charList.size(); i++)
            priorityQueue.insert(new Node<>(charList.get(i), freqList.get(i)));

        while(priorityQueue.size() > 1)
        {
            Node<Character, Integer> leftNode = priorityQueue.extractMin();
            Node<Character, Integer> rightNode = priorityQueue.extractMin();
            Node<Character, Integer> tempNode = new Node<>('$', leftNode.freq + rightNode.freq);
            tempNode.left = leftNode;
            tempNode.right = rightNode;
            priorityQueue.insert(tempNode);
        }
    }

    /**
     * Generates and returns a string containing the Huffman codes for all characters.
     *
     * @return a {@code String} with character-to-code mappings separated by newlines
     */
    public String generateHuffmanCodes()
    {
        int[] codes = new int[10000];
        code = new StringBuilder();
        printCodes(priorityQueue.extractMin(), codes, 0);
        return code.toString();
    }
}
