package org.example;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

public final class ModuleUtil {
    private ModuleUtil() {}

    public static <T> Map<String, T> loadModules(Class<T> moduleType) throws ModuleLoadException {
        URLClassLoader classLoader = loadJars();
        Configuration config = new ConfigurationBuilder().addClassLoaders(classLoader).setUrls(ClasspathHelper.forClassLoader(classLoader));
        Reflections ref = new Reflections(config);
        Set<Class<? extends T>> moduleClasses = ref.getSubTypesOf(moduleType);

        return moduleClasses.stream().collect(Collectors.toMap((module) -> {
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
                     InvocationTargetException e) {
                throw new ModuleLoadException("Could not instantiate module: " + module.getSimpleName(), e);
            }
        }));
    }

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
            } catch (MalformedURLException e) {
                throw new ModuleLoadException("Could not read module file: " + file.getName(), e);
            }
        }).toArray(URL[]::new);

        try {
            return new URLClassLoader(urls, ModuleUtil.class.getClassLoader());
        } catch (SecurityException | NullPointerException e) {
            throw new ModuleLoadException("Could not read module files", e);
        }


    }
}
