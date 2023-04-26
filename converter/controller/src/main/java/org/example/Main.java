package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("hi :)");

        ArgumentUtil.parseArguments(args);

        Map<String, IConverter> modules = null;
        try {
            modules = ModuleUtil.loadModules(IConverter.class);
        } catch (ModuleLoadException e) {
            System.out.println(e.getMessage());
            return;
        }

        modules.forEach((key, value) -> System.out.println(key + " : " + value));
    }
}