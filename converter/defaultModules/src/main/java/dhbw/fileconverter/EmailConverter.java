package dhbw.fileconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@ModuleName("email")
public class EmailConverter implements IConverter {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public String to(JsonNode input, String[] parameters) throws ProcessingException {
        // Create an array node to store the email addresses
        ArrayNode emails = mapper.createArrayNode();

        // Recursively iterate over the input node and add any email addresses to the array node
        filterEmails(input, emails);

        // Return the array node as a string
        try {
            return mapper.writeValueAsString(emails);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new ProcessingException(jsonProcessingException);
        }
    }

    @Override
    public JsonNode from(String input, String[] parameters) throws ProcessingException {
        try {
            return mapper.readValue(input, JsonNode.class);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new ProcessingException(jsonProcessingException);
        }
    }

    private void filterEmails(JsonNode node, ArrayNode emails) {
        for (JsonNode childNode : node) {
            if (childNode.has("Email")) {
                for (JsonNode email : childNode.get("Email")) {
                    emails.add(email);
                }
            } else {
                filterEmails(childNode, emails);
            }
        }
    }
}
