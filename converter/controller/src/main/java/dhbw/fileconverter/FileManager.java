package dhbw.fileconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 * The FileManager class provides utility methods for reading and writing files.
 */
public final class FileManager {

    /**
     * Reads the contents of a file and returns them as a string.
     *
     * @param fileName the name of the file to be read
     * @return a string representing the contents of the file
     */
    static public String getFile(String fileName) {
        String fileLines = "";

        try {
            File fileObject = new File(fileName);
            Scanner fileReader = new Scanner(fileObject);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                fileLines += data;
            }
            fileReader.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("An error occurred.");
            fileNotFoundException.printStackTrace();
        }

        return fileLines;
    }

    /**
     * Writes the specified content to a file.
     *
     * @param fileName    the name of the file to be written
     * @param fileType    the type of the file (e.g., txt, csv)
     * @param fileContent the content to be written to the file
     */
    static public void writeFile(String fileName, String fileType, Object fileContent) {
        try {
            // Create file
            File myObj = new File(fileName + "." + fileType);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Write to file
            FileWriter myWriter = new FileWriter(fileName + "." + fileType);
            myWriter.write(fileContent.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException ioException) {
            System.out.println("An error occurred.");
            ioException.printStackTrace();
        }
    }
}
