package dhbw.fileconverter;

import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("File converter initialized...");

        // load modules
        Map<String, IConverter> modules = null;
        try {
            modules = ModuleUtil.loadModules(IConverter.class);
        } catch (ModuleLoadException e) {
            System.out.println(e.getMessage());
        }

        // parse arguments and get the modules
        ArrayList<IConverter> converterPipe = null;
        try {
            converterPipe = ArgumentUtil.parseArguments(args, modules);
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }

        Object pipeItem;
        String fileDescriptor = args[0];
        String fileEndingFormat = fileDescriptor.split("\\.")[1];
        String fileName = fileDescriptor.split("\\.")[0];

        // handle the first module in the pipe
        try {
            pipeItem = modules.get(fileEndingFormat).from(FileManager.getFile(fileDescriptor));
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }

        // handle the rest of the modules in the pipe
        for (IConverter filter : converterPipe) {
            try {
                String formatString = filter.to(pipeItem);
                pipeItem = filter.from(formatString);
            } catch (ProcessingException e) {
                System.out.println(e.getMessage());
            }
        }

        // get the file type of the last module in the pipe
        IConverter lastModule = converterPipe.get(converterPipe.size() - 1);
        String lastFileType;
        ModuleName annotation = lastModule.getClass().getAnnotation(ModuleName.class);
        if (annotation != null) { // if the module has a ModuleName annotation, use that as the file type
            lastFileType = annotation.value().toLowerCase();
        } else { // otherwise, use the class name
            lastFileType = lastModule.getClass().getSimpleName().toLowerCase();
        }

        // convert pipe item to string
        String pipeItemString = null;
        try {
            pipeItemString = lastModule.to(pipeItem);
        } catch (ProcessingException e) {
            System.out.println(e.getMessage());
        }

        FileManager.writeFile(fileName, lastFileType, pipeItemString); // write the file

        System.out.println("Finished converting given file.");
    }
}