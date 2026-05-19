import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles the generation of an output text file containing the coded
 * text using Huffman Codes.
 */
public class FileOutput 
{
    private File outputFile;
    private String fileText;
    private String codeList;

    /**
     * Constructs a {@code FileOutput} instance and writes the encoded text to a new file.
     *
     * @param inputFileDirectory the directory path of the input file
     * @param inputFileName      the name of the input file
     * @param fileText           the original text content of the input file
     * @param codeList           a newline-separated string of Huffman codes
     */
    public FileOutput(String inputFileDirectory, String inputFileName, String fileText, String codeList)
    {
        try
        {
            String outputFilePath = inputFileName.replace(".txt", "Coded.txt");
            outputFile = new File(inputFileDirectory, outputFilePath);
            outputFile.createNewFile();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        this.fileText = fileText;
        this.codeList = codeList;

        try(FileWriter fw = new FileWriter(outputFile))
        {
            fw.write(generateCodedText());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Encodes the original file text using the provided Huffman codes.
     *
     * @return a {@code String} containing the encoded text
     */
    private String generateCodedText()
    {
        CustomTreeMap<Character, String> treeMap = new CustomTreeMap<>();

        String[] lines = codeList.split("\n");
        for(String line: lines)
        {
            String[] parts = line.split(": ");
            treeMap.insert((char)Integer.parseInt(parts[0]), parts[1]);
        }

        StringBuilder codedText = new StringBuilder();

        for(int i = 0; i < fileText.length(); i++)
        {
            String codedCharacter = treeMap.get(fileText.charAt(i));
            codedText.append(codedCharacter);
        }
        return codedText.toString();
    }
}