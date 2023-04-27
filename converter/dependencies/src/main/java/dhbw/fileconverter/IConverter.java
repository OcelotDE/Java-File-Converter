package dhbw.fileconverter;

public interface IConverter {
    String to(Object input) throws ProcessingException;
    Object from(String input) throws ProcessingException;
}
