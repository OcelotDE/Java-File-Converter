package dhbw.fileconverter;

public class ModuleLoadException extends RuntimeException {
    public ModuleLoadException(String message, Exception e) {
        super(message, e);
    }

    public ModuleLoadException(String message) {
        super(message);
    }
}
