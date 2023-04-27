package dhbw.fileconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@ModuleName("json")
@FileEnding("json")
public class JsonConverter implements IConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String to(JsonNode input, String[] parameters) throws ProcessingException {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new ProcessingException(e);
        }
    }

    @Override
    public JsonNode from(String input, String[] parameters) throws ProcessingException {
        try {
            return mapper.readValue(input, JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new ProcessingException(e);
        }
    }
}
