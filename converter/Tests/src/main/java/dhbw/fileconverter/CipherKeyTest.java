package dhbw.fileconverter;

import com.fasterxml.jackson.databind.JsonNode;
import dhbw.fileconverter.cipher.CipherDecryptFormatter;
import dhbw.fileconverter.cipher.CipherEncryptFormatter;
import dhbw.fileconverter.cipher.CipherFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CipherKeyTest {

    @Test
    public void testCipherKey() {
        // this is a valid json file to test the encrypt-module
        final String validJsonInput = "{\n" +
                "\"FirstName\": \"Ferdi Fuchs\"" +
                "\n}";

        // choose a random key in the wrong length to test the autofill function of the encrypt module.
        // onto the key there will be 'x'-characters appended until the length matches 16 characters
        String secretKey = "happy";

        // initialize the jsonConverter module to create the expected node array for the encryption and decryption module
        JsonConverter jsonConverter = new JsonConverter();
        JsonNode node = null;
        try {
            node = jsonConverter.from(validJsonInput, new String[]{});
        } catch (ProcessingException processingException) {
            System.out.println("JSONConverter(jsonCorrectData) failed.");
        }

        // initialize the encrypt-module and encrypt the content of the nodes in the nodes array
        CipherEncryptFormatter cipherEncryptFormatter = new CipherEncryptFormatter();
        try {
            node = cipherEncryptFormatter.to(node, new String[]{secretKey});
        } catch (ProcessingException processingException) {
            throw new RuntimeException(processingException);
        }

        // initialize the decrypt-module and decrypt the encrypted content of the nodes in the nodes array
        CipherDecryptFormatter cipherDecryptFormatter = new CipherDecryptFormatter();
        try {
            node = cipherDecryptFormatter.to(node, new String[]{secretKey});
        } catch (ProcessingException processingException) {
            throw new RuntimeException(processingException);
        }

        // get the content of the node to compare the output of the Cipher-module against a known string
        String outputNodeContent = node.get("FirstName").textValue();

        // test whether the output matches the known input
        assertEquals(outputNodeContent, "Ferdi Fuchs");
    }
}
