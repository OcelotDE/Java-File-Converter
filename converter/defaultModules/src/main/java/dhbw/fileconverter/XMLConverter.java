package dhbw.fileconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@ModuleName("xml")
@FileEnding("xml")
public class XMLConverter implements IConverter {
    private final XmlMapper mapper = new XmlMapper();

    @Override
    public String to(JsonNode input, String[] parameters) throws ProcessingException {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        if (input.isArray()) {
            input = mapper.createObjectNode().set("row", input);
        }
        try {
            return mapper.writer().withRootName("root").writeValueAsString(input);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new ProcessingException(jsonProcessingException);
        }
    }

    @Override
    public JsonNode from(String input, String[] parameters) throws ProcessingException {
        try {
            ObjectNode node = mapper.readValue(input, ObjectNode.class);
            JsonNode child = node.get("row");
            if (child != null && child.isArray()) {
                return child;
            }
            return node;
        } catch (JsonProcessingException jsonProcessingException) {
            throw new ProcessingException(jsonProcessingException);
        }
    }
}
