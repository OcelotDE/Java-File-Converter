package dhbw.fileconverter;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for managing modules in a file converter application.
 */
public final class ModuleUtil {
    private static ModuleUtil instance;
    public Map<String, IConverter> converters;
    public Map<String, IFormatter> formatters;
    private ModuleUtil() {}

    // SINGLETON PATTERN

    /**
     * Retrieves the singleton instance of the ModuleUtil class
     *
     * @return ModuleUtil instance
     */
    public static ModuleUtil GetInstance() {
        if (instance == null) {
            instance = new ModuleUtil();
            try {
                instance.converters = ModuleUtil.loadModules(IConverter.class);
                instance.formatters = ModuleUtil.loadModules(IFormatter.class);
            } catch (ModuleLoadException e) {
                System.out.println(e.getMessage());
            }
        }
        return instance;
    }
    // END SINGLETON PATTERN

    /**
     * Loads modules of a specific type and returns them as a map
     *
     * @param moduleType The type of modules to load
     * @param <T>        module type
     * @return           a map of module names to module instances
     * @throws ModuleLoadException if an error occurs while loading the modules
     */
    public static <T> Map<String, T> loadModules(Class<T> moduleType) throws ModuleLoadException {
        URLClassLoader classLoader = loadJars();
        Configuration config = new ConfigurationBuilder().addClassLoaders(classLoader).setUrls(ClasspathHelper.forClassLoader(classLoader));
        Reflections ref = new Reflections(config);
        Set<Class<? extends T>> moduleClasses = ref.getSubTypesOf(moduleType);

        return moduleClasses.stream().filter(module -> !Modifier.isAbstract(module.getModifiers()))
                .collect(Collectors.toMap((module) -> {
                    ModuleName annotation = module.getAnnotation(ModuleName.class);
                    if (annotation != null) {
                        return annotation.value().toLowerCase();
                    }
                    return module.getSimpleName().toLowerCase();
                }, module -> {
                    try {
                        Constructor<? extends T> constructor = module.getConstructor();
                        return constructor.newInstance();
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException operationException) {
                        throw new ModuleLoadException("Could not instantiate module: " + module.getSimpleName(), operationException);
                    }
                }));
    }

    /**
     * Loads JAR files from the "modules" directory and creates a URLClassLoader with the JAR file URLs.
     *
     * @return The URLClassLoader containing the loaded JAR files.
     * @throws ModuleLoadException if an error occurs while loading the JAR files.
     */
    private static URLClassLoader loadJars() {
        File dir = new File("modules");

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar");
            }
        });
        if (files == null) {
            throw new ModuleLoadException("No modules found");
        }

        URL[] urls = Arrays.stream(files).map(file -> {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException malformedURLException) {
                throw new ModuleLoadException("Could not read module file: " + file.getName(), malformedURLException);
            }
        }).toArray(URL[]::new);

        try {
            return new URLClassLoader(urls, ModuleUtil.class.getClassLoader());
        } catch (SecurityException | NullPointerException exception) {
            throw new ModuleLoadException("Could not read module files", exception);
        }
    }
}
