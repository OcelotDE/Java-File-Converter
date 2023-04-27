package dhbw.fileconverter;

public interface IFormatter {
    Object encrypt(Object input);

    Object decrypt(Object input);
}
