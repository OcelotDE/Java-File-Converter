package dhbw.fileconverter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XMLToTest {

    @Test
    public void testXMLConverter() {
        String jsonCorrectData = "[\n" +
                "    {\n" +
                "        \"FirstName\": \"Fred\",\n" +
                "        \"Surname\": \"Smith\",\n" +
                "        \"Age\": 24,\n" +
                "        \"Address\": {\n" +
                "            \"Street\": \"Hurley Park\",\n" +
                "            \"City\": \"Winchester\",\n" +
                "            \"Postcode\": \"SO21 2JN\"\n" +
                "        },\n" +
                "        \"Phone\": [\n" +
                "            {\n" +
                "                \"type\": \"home\",\n" +
                "                \"number\": \"0205 544 1234\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"number\": \"01952 001234\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"number\": \"01952 001235\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"mobile\",\n" +
                "                \"number\": \"077 8700 1234\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"Email\": [\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"address\": [\n" +
                "                    \"fred.smith@my-goodwork.com\",\n" +
                "                    \"fsmith@my-work.com\"\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"home\",\n" +
                "                \"address\": [\n" +
                "                    \"freddy@my-social.com\",\n" +
                "                    \"frederic.smith@very-serious.com\"\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"FirstName\": \"Dieter\",\n" +
                "        \"Surname\": \"Müller\",\n" +
                "        \"Age\": 32,\n" +
                "        \"Address\": {\n" +
                "            \"City\": \"Stuttgart\",\n" +
                "            \"Postcode\": \"70191\",\n" +
                "            \"Street\": \"Nordbahnhofstr\"\n" +
                "        },\n" +
                "        \"Email\": [\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"address\": [\n" +
                "                    \"bohl@moenkemoeller.com\",\n" +
                "                    \"bohl@lehre.dhbw-stuttgart.de\"\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"Phone\": [\n" +
                "            {\n" +
                "                \"type\": \"home\",\n" +
                "                \"number\": \"0711 123456789\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"number\": \"0711 987654321\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]\n";

        JsonConverter JsonInstance = new JsonConverter();
        JsonNode node = null;
        try {
            node = JsonInstance.from(jsonCorrectData, new String[]{});
        } catch (ProcessingException processingException) {
            System.out.println("JSONConverter(jsonCorrectData) processingException:" + processingException);
        }

        XMLConverter XmlInstance = new XMLConverter();
        String XmlOutput = null;
        try {
            XmlOutput = XmlInstance.to(node, new String[]{});
        } catch(ProcessingException processingException) {
            System.out.println("XMLConverter(node) processingException:" + processingException);
        }
        assertNotNull(XmlOutput);

        node = null;
        XmlOutput = null;
        try {
            XmlOutput = XmlInstance.to(node, new String[]{});
            System.out.println("XMLConverter(null) failed");    // This try-statement shouldn't be executed
        } catch(ProcessingException | NullPointerException exception) {
            // This catch needs to be executed in order for the test to succeed
        }
        assertNull(XmlOutput);

        System.out.println("testXMLConverter() successful");
    }
}
