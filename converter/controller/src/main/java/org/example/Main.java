package org.example;

import java.io.File;
import java.io.FileNotFoundException;
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

        Object pipeItem = getFile(args[0]);
        for (IConverter filter : converterPipe) {
            try {
                pipeItem = filter.from(pipeItem.toString());
                System.out.println(pipeItem);
            } catch (ProcessingException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("bye :)");

        System.out.println(converterPipe);

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
}