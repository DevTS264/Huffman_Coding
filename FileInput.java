import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Provides a graphical user interface for accepting file paths to perform
 * Huffman Coding.
 */
public class FileInput extends JFrame 
{
    protected JTextArea createFile;
    private File textFileInput;
    private String fileText;
    private boolean codesGenerated;

    /**
     * Constructs the GUI window and initializes components.
     */
    public FileInput()
    {
        setTitle("Huffman Coding");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createFile = new JTextArea();
        createFile.setBackground(Color.BLACK);
        createFile.setForeground(Color.GREEN);
        createFile.setFont(new Font("Courier New", Font.BOLD, 16));
        createFile.setCaretColor(Color.GREEN);
        createFile.setText("Enter the path of the file: ");
        createFile.setCaretPosition(createFile.getDocument().getLength());
        createFile.requestFocusInWindow();

        add(new JScrollPane(createFile));
        fileAction();

        setVisible(true);
    }

    /**
     * Sets up the action listener for key events on the text area.
     */
    private void fileAction()
    {
        createFile.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !codesGenerated)
                {
                    e.consume();
                    processFileCommand();
                }
            }
        });
    }

    /**
     * Processes the file path entered by the user, validates it, and
     * starts the Huffman Coding encoding process.
     */
    private void processFileCommand()
    {
        try
        {    
            int rowStart=createFile.getText().lastIndexOf("Enter the path of the file: ");
            if(rowStart==-1)
                rowStart=0;
            String filePath=createFile.getText(rowStart,createFile.getDocument().getLength()-rowStart).replace("Enter the path of the file: ","").trim();

            if(filePath.contains(".txt"))
            {
                try
                {
                    textFileInput = new File(filePath);
                    if(textFileInput.exists())
                    {
                        if(textFileInput.isFile())
                        {
                            codesGenerated = true;
                            this.fileText = convertToString();
                            createFile.append("\nHuffman Codes for the file " + textFileInput.getName() + " are:-\n(Characters have been converted to their corresponding ASCII values)\n");
                            HuffmanCode code = new HuffmanCode(fileText);
                            code.encode();
                            createFile.append(code.generateHuffmanCodes());
                        }
                        else
                        {
                            createFile.append("\nNot a valid text file\n");
                            SwingUtilities.invokeLater(()->
                            {
                                createFile.append("\nEnter the path of the file: ");
                                createFile.setCaretPosition(createFile.getDocument().getLength());
                            });
                        }
                    }
                    else
                    {
                        createFile.append("\nText file does not exist\n");
                        SwingUtilities.invokeLater(()->
                        {
                            createFile.append("Enter the path of the file: ");
                            createFile.setCaretPosition(createFile.getDocument().getLength());
                        });
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                createFile.append("Not a valid text file\n");
                SwingUtilities.invokeLater(()->
                {
                    createFile.append("Enter the path of the file: ");
                    createFile.setCaretPosition(createFile.getDocument().getLength());
                });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Reads the contents of the text file and converts it into a single String.
     *
     * @return a {@code String} containing the file contents
     */
    private String convertToString()
    {
        StringBuilder text = new StringBuilder();

        try(FileReader fr = new FileReader(textFileInput))
        {
            int read;
            while((read = fr.read()) != -1)
                text.append((char)read);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        return text.toString().replace("\r", "");
    }
}
