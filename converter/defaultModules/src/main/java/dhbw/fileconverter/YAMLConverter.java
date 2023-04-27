package dhbw.fileconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@ModuleName("yaml")
@FileEnding("yaml")
public class YAMLConverter implements IConverter {
    private final YAMLMapper mapper = new YAMLMapper();

    @Override
    public String to(JsonNode input, String[] parameters) throws ProcessingException {
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
