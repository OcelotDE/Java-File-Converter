package dhbw.fileconverter;

import java.util.ArrayList;
import java.util.Map;

public final class ArgumentUtil {
    private ArgumentUtil() {}

    public static ArrayList<IConverter> parseArguments(String[] args, Map<String, IConverter> modules) throws ArgumentException {
        ArrayList<IConverter> parsedArguments = new ArrayList<>();

        if (modules != null) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].startsWith("-")) { // is module argument?
                    /*if (i + 1 < args.length) {
                        if (!args[i + 1].startsWith("-")) { // is module argument with parameters?
                            // TODO
                            throw new RuntimeException("Not implemented");
                        } else { // is module argument without parameters
                            String moduleName = args[i].substring(1);
                            IConverter module = modules.get(moduleName);
                            parsedArguments.add(module);
                        }
                    } else {
                        String moduleName = args[i].substring(1);
                        IConverter module = modules.get(moduleName);
                        parsedArguments.add(module);
                    }*/

                    String moduleName = args[i].substring(1).toLowerCase();
                    IConverter module = modules.get(moduleName);
                    if (module == null) {
                        throw new ArgumentException("Module for argument not found: " + moduleName);
                    }
                    parsedArguments.add(module);
                }
            }
        }
        return parsedArguments;
    }
}
