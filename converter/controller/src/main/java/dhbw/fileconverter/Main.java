package dhbw.fileconverter;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The main method is the entry point for the file converter application.
 * It parses the command-line arguments,
 * sets up the conversion pipeline
 * and performs the conversion.
 *
 * @param \args These are the command line arguments passed to the application
 *              and these specify the conversion modules to use
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("File converter initialized...");
      
        // parse arguments and get the modules
        List<ProcessStep> processPipe = null;
        try {
            processPipe = ArgumentUtil.parseArguments(args);
        } catch (ArgumentException argumentException) {
            System.out.println(argumentException.getMessage());
        }

        JsonNode pipeItem;
        String fileDescriptor = args[0];
        String[] dotStrings = fileDescriptor.split("\\.");
        String pathString = dotStrings[dotStrings.length - 2];
        String fileName = pathString.substring(pathString.lastIndexOf("/") + 1);
    

        if (processPipe == null) {
            throw new RuntimeException("No module parameters given.");
        }

        // check if pipe contains at least two modules
        if (processPipe.size() < 2) {
            System.out.println(processPipe);
            throw new RuntimeException("Pipe must contain at least two modules.");
        }

        // remove the first module from the pipe and save it
        ProcessStep firstStep = processPipe.get(0);
        if (!(firstStep.getModule() instanceof IConverter firstConverter)) {
            throw new RuntimeException("First module in pipe must be a converter.");
        }
        processPipe.remove(0);

        // load the first converter module to pipe
        try {
            pipeItem = firstConverter.from(FileManager.getFile(fileDescriptor), firstStep.getParameters());
        } catch (ProcessingException processingException) {
            System.out.println(processingException.getMessage());
            return;
        }

        // remove the last module from the pipe and save it
        int lastModuleIndex = processPipe.size() - 1;
        ProcessStep lastStep = processPipe.get(lastModuleIndex);
        if (!(lastStep.getModule() instanceof IConverter lastConverter)) {
            throw new RuntimeException("Last module in pipe must be a converter.");
        }
        processPipe.remove(lastModuleIndex);

        // create an iterator for converterPipe
        for (ProcessStep step : processPipe) {
            IModule<?> module = step.getModule();
            if (module instanceof IConverter converter) {
                try {
                    String formatString = converter.to(pipeItem, step.getParameters());

                    pipeItem = converter.from(formatString, step.getParameters());

                } catch (ProcessingException processingException) {
                    System.out.println(processingException.getMessage());
                    return;
                }
            } else if (module instanceof IFormatter formatter) {
                try {
                    JsonNode format = formatter.to(pipeItem, step.getParameters());

                    pipeItem = formatter.from(format, step.getParameters());

                } catch (ProcessingException processingException) {
                    System.out.println(processingException.getMessage());
                    return;
                }
            }
        }

        // get file ending for last element in pipe
        String lastFileType;
        FileEnding annotation = lastConverter.getClass().getAnnotation(FileEnding.class);
        if (annotation != null) { // if the module has a ModuleName annotation, use that as the file type
            lastFileType = annotation.value().toLowerCase();
        } else { // otherwise, use the class name
            lastFileType = lastConverter.getClass().getSimpleName().toLowerCase();
        }

        // try to convert pipe item to string
        String pipeItemString = null;
        try {
            pipeItemString = lastConverter.to(pipeItem, lastStep.getParameters());
        } catch (ProcessingException processingException) {
            System.out.println(processingException.getMessage());
            return;
        }

        FileManager.writeFile(fileName, lastFileType, pipeItemString); // write output to file

        System.out.println("Finished converting given file.");
    }
}