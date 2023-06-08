package dhbw.fileconverter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;



public class JSONFromTest {

    @Test
    public void testJSONConverter() {
        String jsonCorrectData = "[\n" +
                "    {\n" +
                "        \"FirstName\": \"Fred\",\n" +
                "        \"Surname\": \"Smith\",\n" +
                "        \"Age\": 28,\n" +
                "        \"Address\": {\n" +
                "            \"Street\": \"Hursley Park\",\n" +
                "            \"City\": \"Winchester\",\n" +
                "            \"Postcode\": \"SO21 2JN\"\n" +
                "        },\n" +
                "        \"Phone\": [\n" +
                "            {\n" +
                "                \"type\": \"home\",\n" +
                "                \"number\": \"0203 544 1234\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"number\": \"01962 001234\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"number\": \"01962 001235\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"mobile\",\n" +
                "                \"number\": \"077 7700 1234\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"Email\": [\n" +
                "            {\n" +
                "                \"type\": \"office\",\n" +
                "                \"address\": [\n" +
                "                    \"fred.smith@my-work.com\",\n" +
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

        String jsonEmptyData = "";
        String jsonFalseData = "[\n" +
                "    {\n" +
                "  §      \"FirstName\": \"Fred\",\n" +
                "        \"Surname\": \"Smith\",\n" +
                "        \"Age\": 28,";

        JsonConverter instance = new JsonConverter();
        JsonNode node = null;
        try {
            node = instance.from(jsonCorrectData, new String[]{});
        } catch (ProcessingException processingException) {
            System.out.println("testJSONConverter(jsonCorrectData) failed.");
        }
        assertNotNull(node); // check if jsonCorrectData has been successfully converted
        assertTrue(node.isArray()); // check if root node is an array

        // this is a sample test to check whether there is any valid data contained in the node array
        assertTrue(node.get(0).has("FirstName")); // check if first node contains the expected name
        assertEquals(node.get(0).get("FirstName").textValue(), "Fred"); // check if first node contains the expected string value

        node = null;
        try {
            node = instance.from(jsonEmptyData, new String[]{});
            System.out.println("testJSONConverter(jsonEmptyData) failed."); // This try-statement shouldn't be executed
        } catch (ProcessingException processingException) {
            // This catch needs to be executed in order for the test to succeed
        }
        assertNull(node); // check if jsonEmptyData has not been successfully converted

        node = null;
        try {
            node = instance.from(jsonFalseData, new String[]{});
            System.out.println("testJSONConverter(jsonFalseData) failed."); // This try-statement shouldn't be executed
        } catch (ProcessingException processingException) {
            // This catch needs to be executed in order for the test to succeed
        }
        assertNull(node); // check if jsonFalseData has not been successfully converted

        System.out.println("testJSONConverter() successful");
    }

    // test timeout

}
