package dhbw.fileconverter.cipher;

public class CipherException extends RuntimeException {
    public CipherException(Exception exception) {
        super(exception);
    }
}
