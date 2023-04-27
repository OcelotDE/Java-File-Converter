package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("hi :)");

        ArrayList<IConverter> converterPipe = null;
        try {
            converterPipe = ArgumentUtil.parseArguments(args);
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }

        Map<String, IConverter> modules = null;
        try {
            modules = ModuleUtil.loadModules(IConverter.class);
        } catch (ModuleLoadException e) {
            System.out.println(e.getMessage());
        }

        Object pipeItem = null;

        String fileDescriptor = args[0];
        String fileEndingFormat = fileDescriptor.split("\\.")[1];
        String fileName = fileDescriptor.split("\\.")[0];

        try {
            pipeItem = modules.get(fileEndingFormat).from(getFile(fileDescriptor));
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }

        for (IConverter filter : converterPipe) {
            try {
                String formatString = filter.to(pipeItem);
                pipeItem = filter.from(formatString);
            } catch (ProcessingException e) {
                System.out.println(e.getMessage());
            }
        }

        String lastFileType = converterPipe.get(converterPipe.size() - 1).getClass().getAnnotation(ModuleName.class).value();
        System.out.println(lastFileType);


        writeFile(fileName, lastFileType, pipeItem);

        System.out.println("bye :)");

        //modules.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    static private String getFile(String fileName) {
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

    static private void writeFile(String fileName, String fileType, Object fileContent) {
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