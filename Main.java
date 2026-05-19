import javax.swing.SwingUtilities;

/**
 * The main entry point for the Huffman Coding application.
 */
public class Main 
{
    /**
     * The main method that launches the application GUI.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() ->
        {
            new FileInput();
        });
    }
}