package dhbw.fileconverter;

import com.fasterxml.jackson.databind.JsonNode;

public interface IModule<T> {
    T to(JsonNode input, String[] parameters) throws ProcessingException;
    JsonNode from(T input, String[] parameters) throws ProcessingException;
}
