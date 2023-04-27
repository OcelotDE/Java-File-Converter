package dhbw.fileconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@ModuleName("xml")
public class XMLConverter implements IConverter {
    private final XmlMapper mapper = new XmlMapper();

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
