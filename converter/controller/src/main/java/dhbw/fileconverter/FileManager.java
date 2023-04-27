package dhbw.fileconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public final class FileManager {
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
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return fileLines;
    }

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
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
