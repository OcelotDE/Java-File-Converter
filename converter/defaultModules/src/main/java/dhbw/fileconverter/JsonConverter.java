package dhbw.fileconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ModuleName("json")
public class JsonConverter implements IConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String to(Object input) throws ProcessingException {
        try {
            return mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new ProcessingException(e);
        }
    }

    @Override
    public Object from(String input) throws ProcessingException {
        try {
            return mapper.readValue(input, Object.class);
        } catch (JsonProcessingException e) {
            throw new ProcessingException(e);
        }
    }
}
