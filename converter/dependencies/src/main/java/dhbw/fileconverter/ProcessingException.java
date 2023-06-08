package dhbw.fileconverter;

public class ProcessingException extends Exception {
    public ProcessingException(Exception exception) {
        super(exception);
    }
    public ProcessingException(String message) {
        super(message);
    }
}
