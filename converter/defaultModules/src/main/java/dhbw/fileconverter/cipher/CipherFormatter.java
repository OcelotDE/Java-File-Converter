package dhbw.fileconverter.cipher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import dhbw.fileconverter.IFormatter;
import dhbw.fileconverter.ProcessingException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

/**
 * Abstract class representing a Cipher Formatter for file conversion.
 * Implements the IFormatter interface.
 * Provides encryption and decryption capabilities using AES cipher.
 */
public abstract class CipherFormatter implements IFormatter {

    protected ObjectMapper mapper = new ObjectMapper();
    protected static final String ENCRYPTION_ALGORITHM = "AES";
    protected static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    protected static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    protected static final int SECRET_KEY_LENGTH = 256;
    protected static final int ITERATION_COUNT = 65536;
    protected static String SECRET_KEY = "dhbw";
    protected static final byte[] SALT = new byte[] { 0x1a, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef };

    /**
     * Converts the input JsonNode to a cipher-transformed JsonNode using the provided secret key.
     * Supports transformation for objects, arrays, and textual values.
     *
     * @param input      The input JsonNode to be transformed.
     * @param parameters An array of parameters with the secret key as the first element.
     * @return The transformed JsonNode.
     * @throws ProcessingException if no secret key is provided or an exception occurs during the transformation.
     */
    @Override
    public JsonNode to(JsonNode input, String[] parameters) throws ProcessingException {
        if (parameters.length == 0) {
            throw new ProcessingException("No secret key provided");
        }

        SECRET_KEY = parameters[0];

        try {
            SecretKeySpec secretKeySpec = generateSecretKey();
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

            if (input.isObject()) {
                return transformObject((ObjectNode) input, cipher, secretKeySpec);
            } else if (input.isArray()) {
                return transformArray((ArrayNode) input, cipher, secretKeySpec);
            } else if (input.isTextual()) {
                return new TextNode(transform(input.asText(), cipher, secretKeySpec));
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | CipherException exception) {
            throw new ProcessingException(exception);
        }

        return input;
    }

    /**
     * Converts the input JsonNode back to its original form without any transformation.
     *
     * @param input      The input JsonNode to be converted.
     * @param parameters An array of parameters (not used in this method).
     * @return The original input JsonNode.
     */
    @Override
    public JsonNode from(JsonNode input, String[] parameters) {
        return input;
    }

    /**
     * Transforms the input string using the specified cipher and secret key.
     * This method needs to be implemented by concrete subclasses.
     *
     * @param input  The input string to be transformed.
     * @param cipher The Cipher object used for transformation.
     * @param key    The SecretKey used for encryption/decryption.
     * @return The transformed string.
     */
    protected abstract String transform(String input, Cipher cipher, SecretKey key);

    /**
     * Transforms the fields of an ObjectNode recursively using the specified cipher and secret key.
     *
     * @param object The ObjectNode whose fields are to be transformed.
     * @param cipher The Cipher object used for transformation.
     * @param key    The SecretKey used for encryption/decryption.
     * @return The transformed ObjectNode.
     */
    protected ObjectNode transformObject(ObjectNode object, Cipher cipher, SecretKey key) {
        object.fields().forEachRemaining(field -> {
            JsonNode value = field.getValue();
            if (value.isTextual()) {
                object.set(field.getKey(), new TextNode(transform(value.asText(), cipher, key)));
            } else if (value.isObject()) {
                object.set(field.getKey(), transformObject((ObjectNode) value, cipher, key));
            } else if (value.isArray()) {
                object.set(field.getKey(), transformArray((ArrayNode) value, cipher, key));
            }
        });
        return object;
    }

    /**
     * Transforms the elements of an ArrayNode recursively using the specified cipher and secret key.
     *
     * @param array  The ArrayNode whose elements are to be transformed.
     * @param cipher The Cipher object used for transformation.
     * @param key    The SecretKey used for encryption/decryption.
     * @return The transformed ArrayNode.
     */
    protected ArrayNode transformArray(ArrayNode array, Cipher cipher, SecretKey key) {
        return StreamSupport.stream(array.spliterator(), false).map(node -> {
            if (node.isTextual()) {
                return new TextNode(transform(node.asText(), cipher, key));
            } else if (node.isObject()) {
                return transformObject((ObjectNode) node, cipher, key);
            } else {
                return node;
            }
        }).collect(Collector.of(() -> mapper.createArrayNode(), ArrayNode::add, ArrayNode::addAll));
    }

    /**
     * Generates a secret key using the provided SECRET_KEY or pads it if necessary.
     * The generated secret key is based on the ENCRYPTION_ALGORITHM.
     *
     * @return The SecretKeySpec object representing the generated secret key.
     */
    protected SecretKeySpec generateSecretKey() {
        if (SECRET_KEY.length() % 16 != 0) { // AES needs a 16 character long key
            SECRET_KEY += "x".repeat(16 - SECRET_KEY.length() % 16); // If the key is not 16 characters long, pad it
        }
        return new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM); // Generate secret key from user input
    }
}
