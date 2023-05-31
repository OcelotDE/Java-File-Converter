package dhbw.fileconverter;

public class ModuleLoadException extends RuntimeException {
    public ModuleLoadException(String message, Exception exception) {
        super(message, exception);
    }

    public ModuleLoadException(String message) {
        super(message);
    }
}
