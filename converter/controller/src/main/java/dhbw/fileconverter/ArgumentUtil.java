package dhbw.fileconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArgumentUtil {
    private ArgumentUtil() {}

    public static List<ProcessStep> parseArguments(String[] args) throws ArgumentException {
        List<ProcessStep> parsedArguments = new ArrayList<>();

        ModuleUtil moduleUtil = ModuleUtil.GetInstance();

        if (moduleUtil.converters != null) {
            for (int i = 1; i < args.length; i++) {
                String current = args[i];
                ArrayList<String> parameters = new ArrayList<>();
                if (current.startsWith("-")) { // is module argument?
                    String moduleName = current.substring(1).toLowerCase();
                    IModule<?> module = moduleUtil.converters.get(moduleName);
                    if (module == null) {
                        module = moduleUtil.formatters.get(moduleName);
                        if (module == null) {
                            throw new ArgumentException("Module for argument not found: " + moduleName);
                        }
                    }

                    while (args.length > i + 1) {
                        String next = args[i + 1];
                        if (next.startsWith("-")) {
                            break;
                        }
                        parameters.add(next);
                        i++;
                    }

                    ProcessStep currentStep = new ProcessStep(module, parameters.toArray(new String[0]));
                    parsedArguments.add(currentStep);
                }
            }
        }
        return parsedArguments;
    }
}
