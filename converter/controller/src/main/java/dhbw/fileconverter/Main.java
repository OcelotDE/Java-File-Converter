package dhbw.fileconverter;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("File converter initialized...");

        // load converter and formatter modules
        Map<String, IConverter> converters = null;
        Map<String, IFormatter> formatters = null;
        try {
            converters = ModuleUtil.loadModules(IConverter.class);
            formatters = ModuleUtil.loadModules(IFormatter.class);
        } catch (ModuleLoadException e) {
            System.out.println(e.getMessage());
        }

        // parse arguments and get the modules
        List<ProcessStep> processPipe = null;
        try {
            processPipe = ArgumentUtil.parseArguments(args, converters, formatters);
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }

        JsonNode pipeItem;
        String fileDescriptor = args[0];
        String fileEndingFormat = fileDescriptor.split("\\.")[1];
        String fileName = fileDescriptor.split("\\.")[0];



        // handle the first module in the pipe
        /*try {
            pipeItem = converters.get(fileEndingFormat).from(FileManager.getFile(fileDescriptor), null);
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }*/

        // check if pipe contains at least two modules
        if (processPipe.size() < 2) {
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
        } catch (ProcessingException e) {
            System.out.println(e.getMessage());
            return;
        }

        // remove the last module from the pipe and save it
        int lastModuleIndex = processPipe.size() - 1;
        ProcessStep lastStep = processPipe.get(lastModuleIndex);
        if (!(lastStep.getModule() instanceof IConverter lastConverter)) {
            throw new RuntimeException("Last module in pipe must be a converter.");
        }
        processPipe.remove(lastModuleIndex);

        // check if pipe contains at least two converter modules
        if (processPipe.size() == 0) {
            throw new RuntimeException("Pipe must contain at least two converter modules.");
        }

        // create an iterator for converterPipe
        for (ProcessStep step : processPipe) {
            IModule<?> module = step.getModule();
            if (module instanceof IConverter converter) {
                try {
                    String formatString = converter.to(pipeItem, step.getParameters());

                    pipeItem = converter.from(formatString, step.getParameters());

                } catch (ProcessingException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            } else if (module instanceof IFormatter formatter) {
                try {
                    JsonNode format = formatter.to(pipeItem, step.getParameters());

                    pipeItem = formatter.from(format, step.getParameters());

                } catch (ProcessingException e) {
                    System.out.println(e.getMessage());
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

        // convert pipe item to string
        String pipeItemString = null;
        try {
            pipeItemString = lastConverter.to(pipeItem, lastStep.getParameters());
        } catch (ProcessingException e) {
            System.out.println(e.getMessage());
            return;
        }

        FileManager.writeFile(fileName, lastFileType, pipeItemString); // write output to file

        System.out.println("Finished converting given file.");
    }
}